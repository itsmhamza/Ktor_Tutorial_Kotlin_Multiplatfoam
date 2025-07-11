package org.example.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.Data.Product

class HomeViewModel:ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(listOf())
    val products = _products.asStateFlow()


    private val homeRepository = HomeRepository()

    init {
        viewModelScope.launch {
            homeRepository.getProducts().collect { products ->
                _products.update {
                    it + products
                }
            }
        }
    }
}
