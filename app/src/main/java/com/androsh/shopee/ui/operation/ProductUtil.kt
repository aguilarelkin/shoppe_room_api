package com.androsh.shopee.ui.operation

import androidx.compose.runtime.saveable.Saver
import com.androsh.shopee.domain.models.ProductModel

object ProductUtil {

    val productSaver = Saver<ProductModel, Map<String, Any?>>(

        save = { product ->
            mapOf(
                "id" to product.id,
                "title" to product.title,
                "description" to product.description,
                "price" to product.price,
                "discountPercentage" to product.discountPercentage,
                "rating" to product.rating,
                "stock" to product.stock,
                "brand" to product.brand,
                "category" to product.category,
                "thumbnail" to product.thumbnail,
                "images" to product.images
            )
        },
        restore = { map ->
            ProductModel(
                id = map["id"] as Int,
                title = map["title"] as String,
                description = map["description"] as String,
                price = map["price"] as Double,
                discountPercentage = map["discountPercentage"] as Double,
                rating = map["rating"] as Double,
                stock = map["stock"] as Int,
                brand = map["brand"] as String,
                category = map["category"] as String,
                thumbnail = map["thumbnail"] as String,
                images = map["images"] as List<String>
            )
        }
    )
}