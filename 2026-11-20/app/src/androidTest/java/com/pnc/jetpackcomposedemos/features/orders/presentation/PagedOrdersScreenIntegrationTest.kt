package com.pnc.jetpackcomposedemos.features.orders.presentation

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.pnc.jetpackcomposedemos.HiltTestActivity
import com.pnc.jetpackcomposedemos.features.auth.domain.SessionManager
import com.pnc.jetpackcomposedemos.features.auth.domain.User
import com.pnc.jetpackcomposedemos.testutil.builders.aMyOrder
import com.pnc.jetpackcomposedemos.testutil.builders.aMyOrderLine
import com.pnc.jetpackcomposedemos.testutil.builders.aMyOrdersResponse
import com.pnc.jetpackcomposedemos.testutil.fakes.FakeMyOrdersDataSource
import com.pnc.jetpackcomposedemos.ui.theme.KazooTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.performTouchInput

@HiltAndroidTest
class PagedOrdersScreenIntegrationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var session: SessionManager

    @Inject
    lateinit var ordersApi: FakeMyOrdersDataSource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun showScreen() {
        composeRule.setContent {
            KazooTheme {
                PagedOrdersScreen(useTwoPaneLayout = false)
            }
        }
    }

    private fun signInAs(id: String = "11000", name: String = "Cara Cole") {
        session.signIn(User(id = id, username = name, token = "test-token"))
    }

    // Paging loads data on a background thread, which Compose cannot see
    private fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(text, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun httpError(code: Int) = HttpException(
        Response.error<Any>(code, "".toResponseBody("application/json".toMediaType()))
    )

    @Test
    fun givenSignedInCustomerWithOrders_whenScreenOpens_thenTheOrdersAreListed() {
        signInAs()
        ordersApi.response = aMyOrdersResponse(
            11000,
            aMyOrder(orderNumber = 1001, lines = listOf(aMyOrderLine(productName = "Road Bike"))),
            aMyOrder(orderNumber = 1002, lines = listOf(aMyOrderLine(lineId = 2, productName = "Helmet")))
        )

        showScreen()

        waitForText("Order #:1001")
        composeRule.onNodeWithText("Order #:1002").assertIsDisplayed()
        composeRule.onNodeWithText("Road Bike").assertIsDisplayed()
        composeRule.onNodeWithText("Helmet").assertIsDisplayed()
    }

    @Test
    fun givenSignedInCustomer_whenScreenOpens_thenTheCustomersNameComesFromTheSession() {
        ordersApi.response = aMyOrdersResponse(11000, aMyOrder(1001), aMyOrder(1002))
        signInAs(name = "Cara Cole")

        showScreen()

        waitForText("Order #:1001")
        composeRule.onAllNodesWithText("Customer: Cara Cole").assertCountEquals(2)
    }

    @Test
    fun givenNoOrders_whenScreenOpens_thenTheEmptyMessageIsShown() {
        ordersApi.response = aMyOrdersResponse(11000)
        signInAs()

        showScreen()

        waitForText("No orders found")
    }

    @Test
    fun givenAnExpiredSession_whenScreenOpens_thenAnExplanationAndRetryAreShown() {
        signInAs()
        ordersApi.failure = httpError(401)

        showScreen()

        waitForText("Error: Your session has expired. Please sign in again.")
        composeRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun givenAFailedLoad_whenRetryIsTappedAfterTheServerRecovers_thenTheOrdersAppear() {
        ordersApi.failure = httpError(500)
        signInAs()
        showScreen()
        waitForText("Error: The server returned an error (500).")

        ordersApi.failure = null
        ordersApi.response = aMyOrdersResponse(11000, aMyOrder(1001))
        composeRule.onNodeWithText("Retry").performClick()

        waitForText("Order #:1001")
    }


    @Test
    fun givenOrdersInTwoMonths_whenABarIsTapped_thenItsMonthAndTotalAreShown() {
        ordersApi.response = aMyOrdersResponse(
            11000,
            aMyOrder(orderNumber = 1001, orderDate = "2026-01-15T00:00:00", lines = listOf(aMyOrderLine(quantity = 1, unitPrice = 100.0))),
            aMyOrder(orderNumber = 1002, orderDate = "2026-03-15T00:00:00", lines = listOf(aMyOrderLine(quantity = 1, unitPrice = 300.0)))
        )
        signInAs()
        showScreen()
        waitForText("Order totals by month")

        // Two bars share the chart's width, so a tap in the left quarter lands on the first one (January).
        composeRule.onNode(hasContentDescription("Bar chart", substring = true))
            .performTouchInput { click(Offset(width * 0.25f, height * 0.5f)) }

        waitForText("January 2026: $100.00")
    }

}



