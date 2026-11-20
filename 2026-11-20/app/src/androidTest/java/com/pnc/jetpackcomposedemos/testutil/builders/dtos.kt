package com.pnc.jetpackcomposedemos.testutil.builders

import com.pnc.jetpackcomposedemos.features.artists.data.ArtistDto
import com.pnc.jetpackcomposedemos.features.orders.data.MyOrderDto
import com.pnc.jetpackcomposedemos.features.orders.data.MyOrderLineDto
import com.pnc.jetpackcomposedemos.features.orders.data.MyOrderResponseDto


fun anArtistDto(
    id: Int = 1,
    name: String = "Test Artist",
    genre: String = "Rock",
    location: String = "New York, NY",
    imageUrl: String = "",
    description: String = "A test artist",
    tags: String = ""
) = ArtistDto(id, name, genre, location, imageUrl, description, tags)


fun aMyOrderLine(
    lineId: Int = 1,
    productName: String = "Road Bike",
    quantity: Int = 2,
    unitPrice: Double = 50.0
) = MyOrderLineDto(
    lineId = lineId, productId = 7, productName = productName, quantity = quantity,
    unitPrice = unitPrice, unitPriceDiscount = 0.0, lineTotal = quantity * unitPrice
)

fun aMyOrder(
    orderNumber: Int = 1001,
    orderDate: String = "2014-05-01T00:00:00",
    shipDate: String? = "2014-05-08T00:00:00",
    lines: List<MyOrderLineDto> = listOf(aMyOrderLine())
) = MyOrderDto(
    orderNumber = orderNumber, orderReference = "SO$orderNumber", orderDate = orderDate,
    shipDate = shipDate, status = 5, statusName = "Shipped", subTotal = 100.0, taxAmt = 8.0,
    freight = 5.0, totalDue = 113.0, lines = lines
)

fun aMyOrdersResponse(customerId: Int = 11000, vararg orders: MyOrderDto) = MyOrderResponseDto(
    customerId = customerId,
    page = 1,
    pageSize = 100,
    totalOrders = orders.size,
    orders = orders.toList()
)




