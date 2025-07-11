package org.example.project

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.flow
import org.example.project.Data.Product
import org.example.project.apiClient.httpClient

class HomeRepository {

    suspend fun getProductsApi():List<Product> {
        val response = httpClient.get("https://fakestoreapi.com/products")
        return response.body()
    }

    fun getProducts() = flow {
        emit(getProductsApi())
    }
}
