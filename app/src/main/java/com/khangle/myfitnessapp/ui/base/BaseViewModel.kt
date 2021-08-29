package com.khangle.myfitnessapp.ui.base

import androidx.lifecycle.ViewModel
import com.khangle.myfitnessapp.data.network.ResponseMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

open class BaseViewModel: ViewModel() {

    suspend fun handleResponse(handle: (ResponseMessage) -> Unit, body:suspend ()  -> Unit) {
        try {
            body()
        } catch (error: HttpException) {
            val string = error.response()?.errorBody()?.string()
            withContext(Dispatchers.Main) {
                handle(ResponseMessage(error = string ?: "", id = null))
            }
        }
    }
}