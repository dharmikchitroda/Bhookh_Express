package com.example.fooddelivery.ui

import FlashApi
import InternetData
import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val _uisate = MutableStateFlow(UiState_Model())
    val uistate: StateFlow<UiState_Model> = _uisate.asStateFlow()

    private val _IsVisible = MutableStateFlow(true)
    val IsVisible = _IsVisible.asStateFlow()

    // Yeh empty list wala StateFlow add ho gaya for add to cart screen
    private val _cartItems = MutableStateFlow<List<InternetData>>(emptyList())
    val emptyListFlow: StateFlow<List<InternetData>> = _cartItems.asStateFlow()

    // use for datastore by prefernce type
    private val Context.dataStore by preferencesDataStore("cart")
    private val context = application.applicationContext

    //create a key beacause datastore in key-value pair
    private val CartIteamKey = stringPreferencesKey("Cart_Iteam")


    // this sealed class say I have 3 work only if you can use when your need according one of the three's
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

    // add data into the key
    private suspend fun saveCartItemsToDataStore() {
        context.dataStore.edit { variable_preferences ->
            variable_preferences[CartIteamKey] = Json.encodeToString(_cartItems.value)
        }
    }

    //DataStore se cart items nikal na
    private suspend fun loadCartItemsFromDataStore() {
        val fullData = context.dataStore.data.first()
        val cartItemsJson = fullData[CartIteamKey]
        if (!cartItemsJson.isNullOrEmpty()){
            _cartItems.value = Json.decodeFromString(cartItemsJson)
        }
    }

    fun getItems() {
        viewModelScope.launch {
            // first of all loading assing kar diya
            itemUiState = ItemUiState.Loading
            try {
                // jab kuch aya to ae kar diya
                val listResult = FlashApi.retrofitService.getItems()
                itemUiState = ItemUiState.Success(listResult)
                // aek bar add karne ke vo data hum fatch karlenge dusri bar datastore mese aa jaye iss liye
                loadCartItemsFromDataStore()

            } catch (ex: Exception) {
                itemUiState = ItemUiState.Error(ex.message ?: "Something went wrong")
            }
        }
    } /* ---> ab bad me iitemUiState me konsa object hain iske according hum ui change kar lenege which ka use karke */

    // for cart screen add remove function
    fun addToCart(item: InternetData) {
        _cartItems.value = _cartItems.value + item
        // use the add data into preferncedataset
        viewModelScope.launch {
            saveCartItemsToDataStore()
        }
    }

    fun removeFromCart(item: InternetData) {
        _cartItems.value = _cartItems.value - item
        viewModelScope.launch {
            saveCartItemsToDataStore()
        }
    }

    init {
        viewModelScope.launch {
            delay(3000)
            _IsVisible.value = false
            getItems()
        }
    }
}


