package org.anf.kotlinamex.controller

import org.anf.kotlinamex.domain.Order
import org.anf.kotlinamex.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * Urls:
 *  http://localhost:8080/swagger-ui/index.html
 */

@RestController
@RequestMapping("/order")
class OrderController(val orderService: OrderService) {

    data class OrderRequest(val items: Map<String,Int>)

    @GetMapping("/message")
    fun getMessage(): String {
        return "you got this!!!"
    }

    @PostMapping("/create")
    fun createOrder(@RequestBody order: OrderRequest): ResponseEntity<Order> {
        val order = orderService.createOrder(order.items)
        return ResponseEntity.ok(order)
    }

    @PostMapping("/create/offer")
    fun createOrderWithOffer(@RequestBody order: OrderRequest): ResponseEntity<Order> {
        val order = orderService.createOrderWithOffers(order.items)
        return ResponseEntity.ok(order)
    }

    @GetMapping("/get-all")
    fun getAllOrders(): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderService.fetchOrders())
    }

    @GetMapping("/get")
    fun getOrder(orderId: String): ResponseEntity<Order> {
        return ResponseEntity.ok(orderService.fetchOrder(orderId))
    }
}