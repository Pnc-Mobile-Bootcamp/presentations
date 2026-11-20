package com.pnc.jetpackcomposedemos.features.todo.presentation

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

// Tracks one long-press drag in a LazyColumn
// The list reports each swap through [onMove]
// (so the caller can update its list)
// and the end of the drag through [onDragEnd]
// (so the caller can save the new order)
// The dragged row is identified by its list key
// (not the index within the list)
// b/c the list key does not change when rows swap
@Stable
class DragDropState internal constructor(
    private val listState: LazyListState,
    private val onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    private val onDragEnd: () -> Unit
) {
    var draggingItemKey by mutableStateOf<Any?>(null)

    private var draggedDistance by mutableFloatStateOf(0f)
    private var draggingItemInitialOffset by mutableIntStateOf(0)

    private var swappedFromIndex: Int?  = null

    // item layout info for the item being dragged
    private val draggingItemLayoutInfo: LazyListItemInfo?
        get() = listState.layoutInfo.visibleItemsInfo.firstOrNull {
            it.key == draggingItemKey
        }

    // how many pixels the dragged row must be shifted from where
    // the list currently has it laid out
    val draggingItemOffset: Float
        get() = draggingItemLayoutInfo?.let { item ->
            draggingItemInitialOffset + draggedDistance - item.offset
        } ?: 0f


    internal fun onDragStart(key: Any) {
        listState.layoutInfo.visibleItemsInfo
            .firstOrNull { it.key == key }
            ?.let { item ->
                draggingItemKey = key
                draggingItemInitialOffset = item.offset
            }
    }

    internal fun onDrag(delta: Offset) {
        draggedDistance += delta.y

        val dragging = draggingItemLayoutInfo ?: return
        if (dragging.index == swappedFromIndex) return
        swappedFromIndex = null

        // only swap when the middle of the dragged row
        // crosses into another row
        val middle = dragging.offset + draggingItemOffset + dragging.size / 2f
        val target = listState.layoutInfo.visibleItemsInfo
            .firstOrNull { item ->
                item.key != draggingItemKey
                        && middle.toInt() in item.offset..(item.offset + item.size)
            } ?: return

        onMove(dragging.index, target.index)
        swappedFromIndex = dragging.index
    }

    internal fun onDragFinished() {
        val wasDragging = draggingItemKey != null
        draggedDistance = 0f
        draggingItemInitialOffset = 0
        swappedFromIndex = null
        draggingItemKey = null
        if (wasDragging) onDragEnd()
    }

}




@Composable
fun rememberDragDropState(
    listState: LazyListState,
    onMove: (fromIndex: Int, toIndex: Int) -> Unit,
    onDragEnd: () -> Unit
): DragDropState {
    val currentOnMove by rememberUpdatedState(onMove)
    val currentOnDragEnd by rememberUpdatedState(onDragEnd)

    return remember(listState) {
        DragDropState(
            listState = listState,
            onMove = { from, to -> currentOnMove(from, to)},
            onDragEnd = { currentOnDragEnd() }
        )
    }
}


// Put this modifier on each ROW (not on the LazyColumn)
// a long press on a row starts a drag.
// (A handler on the row sees the touch before the list's scrolling does,
// so it can take the drag. A handler on the list would not
// recognize the drag, long-press, b/c it would start scrolling first)
fun Modifier.dragHandle(dragDropState: DragDropState, key: Any): Modifier =
    pointerInput(dragDropState, key) {
        detectDragGesturesAfterLongPress(
            onDragStart = { dragDropState.onDragStart(key) },
            onDrag = { change, dragAmount ->
                change.consume()
                dragDropState.onDrag(dragAmount)
            },
            onDragEnd = { dragDropState.onDragFinished() },
            onDragCancel = { dragDropState.onDragFinished() }
        )
    }



