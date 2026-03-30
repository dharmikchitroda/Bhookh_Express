package com.example.fooddelivery.ui

import FlashApi
import InternetData
import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
    private val context_variable = application.applicationContext


    //create a key beacause datastore in key-value pair
    private val CartIteamKey = stringPreferencesKey("Cart_Iteam")

    // for fierbase use
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: MutableStateFlow<FirebaseUser?> get() = _user

    private val _PhoneNumber = MutableStateFlow("")
    val PhoneNumber: MutableStateFlow<String> get() = _PhoneNumber


    // for otp
    private var _otp = MutableStateFlow("")
    val otp: MutableStateFlow<String> get() = _otp

    // for verificationId

    private var _verificationId = MutableStateFlow("")
    val verificationId: MutableStateFlow<String> get() = _verificationId

    private var _UserImage = MutableStateFlow<Uri?>(null)
    val UserImage: MutableStateFlow<Uri?> get() = _UserImage

    private var _Timer = MutableStateFlow(60L)
    val Timer: MutableStateFlow<Long> get() = _Timer

    private var _isLoading = MutableStateFlow(false)
    val isLoadding: MutableStateFlow<Boolean> get() = _isLoading

    // use for dialog show or not
    private val _DialogStatus = MutableStateFlow(false)
    val DialogStatus: MutableStateFlow<Boolean> get() = _DialogStatus

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
        context_variable.dataStore.edit { variable_preferences ->
            // add daatabase for need to data should be obj->json
            variable_preferences[CartIteamKey] = Gson().toJson(_cartItems.value)
        }
    }

    //DataStore se cart items nikal na
    private suspend fun loadCartItemsFromDataStore() {
        val fullData = context_variable.dataStore.data.first()
        val cartItemsJson = fullData[CartIteamKey]
        if (!cartItemsJson.isNullOrEmpty()) {
            // fatch data from daatabase for need to data should be json -> obj

            val type = object : TypeToken<List<InternetData>>() {}.type
            val cartList: List<InternetData> = Gson().fromJson(cartItemsJson, type)

            _cartItems.value = cartList
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


    // setPhoneNumber
    fun setPhoneNumber(phoneNumber: String) {
        _PhoneNumber.value = phoneNumber
    }

    // set otp
    fun setOtp(value: String) {
        _otp.value = value
    }

    fun setUser(value: FirebaseUser?) {
        _user.value = value
    }

    fun ClearUser() {
        _user.value = null
        _PhoneNumber.value = ""
        _otp.value = ""
        _verificationId.value = ""
    }

// for verificationId add by click btn and run firebase callback auto

    fun setverificationId(value: String) {
        _verificationId.value = value
    }

    fun setImage(uri: Uri?) {
        _UserImage.value = uri
    }

    private var timerJob: Job? = null

    // use for time 60 to 0
    fun startTimer() {
        timerJob?.cancel()
        _Timer.value =
            60L // first var loop chalshe 0 thai jashe to biji vcar click karshe tyare 60 karvu padshe ne
        timerJob = viewModelScope.launch {
            while (_Timer.value > 0) {
                delay(1000)
                _Timer.value--
            }
        }
    }

    // for when otp come untill disable and shw btn again
    fun setLoading(value: Boolean) {
        _isLoading.value = value
    }
// set dialog status true fasle

    fun setDialogStatus(value: Boolean) {
        _DialogStatus.value = value
    }


    init {
        viewModelScope.launch {
            delay(3000)
            _IsVisible.value = false
            getItems()
        }
    }
}




