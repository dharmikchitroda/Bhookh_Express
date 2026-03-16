package com.example.fooddelivery.ui

import FlashApi
import InternetData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class FoodViewModel : ViewModel() {
    private val _uisate = MutableStateFlow(UiState_Model())
    val uistate: StateFlow<UiState_Model> = _uisate.asStateFlow()

    private val _IsVisible = MutableStateFlow(true)
    val IsVisible = _IsVisible.asStateFlow()

    // Yeh empty list wala StateFlow add ho gaya for add to cart screen
    private val _emptyListFlow = MutableStateFlow<List<InternetData>>(emptyList())
    val emptyListFlow: StateFlow<List<InternetData>> = _emptyListFlow.asStateFlow()



// this sealed class say i have 3 work only if you can use when your need according one of the three's
    sealed class ItemUiState {
        data class Success(var items: List<InternetData>) : ItemUiState()
        object Loading : ItemUiState()
        data class Error(val message: String) : ItemUiState()
    }
// now itemUiState will dicide which i shoud to use one
    var itemUiState: ItemUiState by mutableStateOf(ItemUiState.Loading)
        private set


    fun EventChange(name: String) {
        _uisate.value = _uisate.value.copy(MoveText = name)
    }


    fun getItems() {
        viewModelScope.launch {
            // first of all loading assing kar diya
            itemUiState = ItemUiState.Loading
            try {
                // jab kuch aya to ae kar diya
                val listResult = FlashApi.retrofitService.getItems()
                itemUiState = ItemUiState.Success(listResult)

            } catch (ex: Exception) {
                itemUiState = ItemUiState.Error(ex.message?: "Something went wrong")
            }
        }
    } /* ---> ab bad me iitemUiState me konsa object hain iske according hum ui change kar lenege which ka use karke */

    // for cart screen add remove function
    fun addToCart(item: InternetData) {
        _emptyListFlow.value = _emptyListFlow.value + item
    }

    fun removeFromCart(item: InternetData) {
        _emptyListFlow.value = _emptyListFlow.value - item
    }

    init {
        viewModelScope.launch {
            delay(3000)
            _IsVisible.value = false
            getItems()
        }
    }
}


