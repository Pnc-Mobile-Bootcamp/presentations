package com.pnc.jetpackcomposedemos.features.orders.domain

import com.pnc.jetpackcomposedemos.features.orders.data.FakeOrderRepository
import com.pnc.jetpackcomposedemos.testutil.builders.anOrder
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDateTime

class GetOrdersUseCaseTest {

    private val jan = LocalDateTime.of(2024, 1, 1, 0, 0)
    private val mar = LocalDateTime.of(2024, 3, 1, 0, 0)
    private val jun = LocalDateTime.of(2024, 6, 1, 0, 0)

    @Test
    fun `when getting orders, then asks the repository exactly once`() = runTest {
        val repo = FakeOrderRepository(listOf(anOrder()))

        GetOrdersUseCase(repo)()

        assertEquals(1, repo.getOrdersCalls)
    }

    @Test
    fun `given unsorted orders, when getting orders, then returns newest first`() = runTest {
        val oldest = anOrder { id = 1; orderDate = jan }
        val newest = anOrder { id = 2; orderDate = jun }
        val middle = anOrder { id = 3; orderDate = mar }

        val repo = FakeOrderRepository(listOf(oldest, newest, middle))
        val useCase = GetOrdersUseCase(repo)

        val result = useCase()

        assertEquals(listOf(2, 3, 1), result.map { it.id })

    }


}