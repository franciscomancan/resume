package org.anf.kotlinamex.domain

import java.util.UUID

data class Order(val orderItems: List<Item>, val cost: Double, val id: String = UUID.randomUUID().toString())