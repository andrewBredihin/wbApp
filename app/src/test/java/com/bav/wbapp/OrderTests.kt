package com.bav.wbapp

import androidx.core.util.PatternsCompat
import com.bav.core.basket.ProductEntity
import com.bav.wbapp.order.create.CreateOrderState
import com.bav.wbapp.order.create.OrderType
import com.bav.wbapp.order.create.OrderViewModel
import com.bav.wbapp.restaurants.RestaurantInfo
import com.yandex.mapkit.geometry.Point
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime

class OrderTests {

    private val createOrderStatesZeroFields = listOf(
        CreateOrderState(type = OrderType.DELIVERY),
        CreateOrderState(type = OrderType.TIME_IN),
        CreateOrderState(type = OrderType.PICKUP)
    )

    private val createOrderStatesTimeIn = listOf(
        CreateOrderState(
            name = "1",
            phone = "8 927 100-10-10",
            email = "test@mail.ru",
            address = "test",
            date = LocalDateTime.now(),
            type = OrderType.TIME_IN
        ),
        CreateOrderState(
            name = "1",
            phone = "8 927 100-10-10",
            email = "test@mail.ru",
            address = "",
            date = LocalDateTime.now(),
            type = OrderType.TIME_IN
        ),
        CreateOrderState(
            name = "1",
            phone = "8 927 100-10-10",
            email = "test@mail.ru",
            address = "test",
            date = null,
            type = OrderType.TIME_IN
        )
    )

    private val createOrderStatesPickup = listOf(
        CreateOrderState(
            name = "1",
            phone = "8 927 100-10-10",
            email = "test@mail.ru",
            date = LocalDateTime.now(),
            restaurant = RestaurantInfo(0, "", "", "", Point(0.0, 0.0)),
            type = OrderType.PICKUP
        ),
        CreateOrderState(
            name = "1",
            phone = "8 927 100-10-10",
            email = "test@mail.ru",
            date = null,
            restaurant = RestaurantInfo(0, "", "", "", Point(0.0, 0.0)),
            type = OrderType.PICKUP
        ),
        CreateOrderState(
            name = "1",
            phone = "8 927 100-10-10",
            email = "test@mail.ru",
            date = LocalDateTime.now(),
            restaurant = null,
            type = OrderType.PICKUP
        ),
    )

    private val createOrderStatesDelivery = listOf(
        CreateOrderState(
            name = "1",
            phone = "8 927 100-10-10",
            email = "test@mail.ru",
            address = "test",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "",
            phone = "8 927 100-10-10",
            email = "test@mail.ru",
            address = "test",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "1",
            phone = "",
            email = "test@mail.ru",
            address = "test",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "1",
            phone = "8 927 100-10-10",
            email = "",
            address = "test",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "1",
            phone = "8 927 100-10-10",
            email = "test@mail.ru",
            address = "",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "1",
            phone = "89271001010",
            email = "test@mail.ru",
            address = "test",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "1",
            phone = "+7 927 100-10-10",
            email = "test@mail.ru",
            address = "test",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "1",
            phone = "8927 100-10-10",
            email = "test@mail.ru",
            address = "test",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "1",
            phone = "927 100-10-10",
            email = "test@mail.ru",
            address = "test",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "1",
            phone = "+7927 100-10-10",
            email = "test@mail.ru",
            address = "test",
            type = OrderType.DELIVERY
        ),
        CreateOrderState(
            name = "1",
            phone = "+7927 100-10-10",
            email = "test",
            address = "test",
            type = OrderType.DELIVERY
        )
        ,
        CreateOrderState(
            name = "1",
            phone = "+7927 100-10-10",
            email = "test@mail",
            address = "test",
            type = OrderType.DELIVERY
        )
    )

    private val productsInBasket = listOf(
        ProductEntity(
            price = 100f,
            amountInBasket = 1,
            id = 0,
            productId = 0,
            amount = 1,
            title = "",
            type = "",
            available = true
        ),
        ProductEntity(
            price = 250.50f,
            amountInBasket = 3,
            id = 0,
            productId = 0,
            amount = 1,
            title = "",
            type = "",
            available = true
        ),
        ProductEntity(
            price = 900f,
            amountInBasket = 2,
            id = 0,
            productId = 0,
            amount = 1,
            title = "",
            type = "",
            available = true
        )
    )

    @Test
    fun testBasketPrice() {
        val result = calculatePrice(productsInBasket)
        Assert.assertEquals(result, 2386)
    }

    @Test
    fun testCreateOrderButtonCheckZeroFields() {
        createOrderStatesZeroFields.forEach {
            val result = checkContinueButtonEnabled(it)
            Assert.assertEquals(result, false)
        }
    }

    @Test
    fun testCreateOrderButtonCheckPickup() {
        val d1 = createOrderStatesPickup[0]
        val r1 = checkContinueButtonEnabled(d1)
        Assert.assertEquals(r1, true)

        val d2 = createOrderStatesPickup[1]
        val r2 = checkContinueButtonEnabled(d2)
        Assert.assertEquals(r2, false)

        val d3 = createOrderStatesPickup[2]
        val r3 = checkContinueButtonEnabled(d3)
        Assert.assertEquals(r3, false)
    }

    @Test
    fun testCreateOrderButtonCheckTimeIn() {
        val d1 = createOrderStatesTimeIn[0]
        val r1 = checkContinueButtonEnabled(d1)
        Assert.assertEquals(r1, true)

        val d2 = createOrderStatesTimeIn[1]
        val r2 = checkContinueButtonEnabled(d2)
        Assert.assertEquals(r2, false)

        val d3 = createOrderStatesTimeIn[2]
        val r3 = checkContinueButtonEnabled(d3)
        Assert.assertEquals(r3, false)
    }

    @Test
    fun testCreateOrderButtonCheckDelivery() {
        val d1 = createOrderStatesDelivery[0]
        val r1 = checkContinueButtonEnabled(d1)
        Assert.assertEquals(r1, true)

        val d2 = createOrderStatesDelivery[1]
        val r2 = checkContinueButtonEnabled(d2)
        Assert.assertEquals(r2, false)

        val d3 = createOrderStatesDelivery[2]
        val r3 = checkContinueButtonEnabled(d3)
        Assert.assertEquals(r3, false)

        val d4 = createOrderStatesDelivery[3]
        val r4 = checkContinueButtonEnabled(d4)
        Assert.assertEquals(r4, false)

        val d5 = createOrderStatesDelivery[4]
        val r5 = checkContinueButtonEnabled(d5)
        Assert.assertEquals(r5, false)

        val d6 = createOrderStatesDelivery[5]
        val r6 = checkContinueButtonEnabled(d6)
        Assert.assertEquals(r6, false)

        val d7 = createOrderStatesDelivery[6]
        val r7 = checkContinueButtonEnabled(d7)
        Assert.assertEquals(r7, true)

        val d8 = createOrderStatesDelivery[7]
        val r8 = checkContinueButtonEnabled(d8)
        Assert.assertEquals(r8, true)

        val d9 = createOrderStatesDelivery[8]
        val r9 = checkContinueButtonEnabled(d9)
        Assert.assertEquals(r9, true)

        val d10 = createOrderStatesDelivery[9]
        val r10 = checkContinueButtonEnabled(d10)
        Assert.assertEquals(r10, true)

        val d11 = createOrderStatesDelivery[10]
        val r11 = checkContinueButtonEnabled(d11)
        Assert.assertEquals(r11, false)
        val d12 = createOrderStatesDelivery[11]
        val r12 = checkContinueButtonEnabled(d12)
        Assert.assertEquals(r12, false)

    }

    private fun checkContinueButtonEnabled(state: CreateOrderState): Boolean {
        val checkOrderTypeFields: Boolean = when (state.type) {
            OrderType.DELIVERY -> {
                state.address.isNotEmpty()
            }

            OrderType.TIME_IN  -> {
                state.address.isNotEmpty() && state.date != null
            }

            OrderType.PICKUP   -> {
                state.restaurant != null && state.date != null
            }
        }
        val checkPhone: Boolean = !OrderViewModel.PHONE_TEMPLATE.toRegex().find(state.phone)?.value.isNullOrEmpty()
        val checkEmail: Boolean = PatternsCompat.EMAIL_ADDRESS.matcher(state.email).matches()
        return checkOrderTypeFields &&
                checkPhone &&
                checkEmail &&
                state.name.isNotEmpty()
    }

    private fun calculatePrice(products: List<ProductEntity>): Int {
        var price = 0f
        val discountValue = 10
        products.forEach { product ->
            price += product.price * product.amountInBasket
        }
        price *= (100f - discountValue) / 100f
        return price.toInt()
    }
}