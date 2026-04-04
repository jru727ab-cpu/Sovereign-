package com.sovereign.civiltas.domain.model

enum class ResourceType { ORE, STONE, KNOWLEDGE, ENERGY }

data class Resource(
    val type: ResourceType,
    val amount: Double,
    val perSecond: Double
)
