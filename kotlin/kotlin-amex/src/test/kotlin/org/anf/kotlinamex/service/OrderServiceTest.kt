import org.anf.kotlinamex.service.OrderService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

class OrderServiceTest {

    @Test
    fun `test empty order`() {
        val request = emptyMap<String,Int>()
        val order = OrderService().createOrder(request)
        assertEquals(0.0, order.cost)
    }

    @Test
    fun `test tiny order`() {
        val request = mapOf("apple" to 1, "orange" to 0)
        val order = OrderService().createOrder(request)
        assertEquals(0.60, order.cost)
    }

    @Test
    fun `test full order`() {
        val request = mapOf("apple" to 10, "orange" to 10)
        val order = OrderService().createOrder(request)
        assertEquals(8.50, order.cost)
    }

    @Test
    fun `test things we do not have `() {
        val request = mapOf("pineapple" to 10, "dragonfruit" to 10)
        val order = OrderService().createOrder(request)
        assertEquals(0.0, order.cost)
    }


    // Need to adjust for this use case...
    @Test
    fun `test negatives`() {
        //etc..
        assertEquals(0, 0)
    }

    @Test
    fun `test offer tiny order`() {
        val request = mapOf("apple" to 1, "orange" to 1)
        val order = OrderService().createOrderWithOffers(request)
        assertEquals(0.85, order.cost)
    }

    @Test
    fun `test offer order`() {
        val request = mapOf("apple" to 2, "orange" to 3)
        val order = OrderService().createOrderWithOffers(request)
        assertEquals(1.1, order.cost)
    }

    @Test
    fun `test random offer order`() {
        val request = mapOf("apple" to 11, "orange" to 11, "dsflkajsdf" to 99)
        val order = OrderService().createOrderWithOffers(request)
        assertEquals(5.60, order.cost)
    }

    @Test
    fun `test order retrieval`() {
        val svc = OrderService()
        val request = mapOf("apple" to 11, "orange" to 11, "dsflkajsdf" to 99)
        val storedOrder = svc.createOrderWithOffers(request)
        val retrievedOrder = svc.fetchOrder(storedOrder.id)
        assertEquals(storedOrder.id, retrievedOrder.id)
        assertEquals(storedOrder.orderItems, retrievedOrder.orderItems)
    }

    @Test
    fun `test all orders retrieval`() {
        val svc = OrderService()
        val o1 = svc.createOrderWithOffers(mapOf("apple" to 11, "orange" to 11, "sdf" to 99))
        val o2 = svc.createOrderWithOffers(mapOf("apple" to 22, "orange" to 22, "dd" to 99))
        val o3 = svc.createOrderWithOffers(mapOf("apple" to 33, "orange" to 33, "ff" to 99))

        val allOrders = svc.fetchOrders().map { o -> o.id to o }.toMap()
        assertEquals(3, allOrders.size)
        assertContains(allOrders, o1.id)
        assertContains(allOrders, o2.id)
        assertContains(allOrders, o3.id)
    }
}