package id.emergence.wher.data.remote

import id.emergence.wher.data.remote.exceptions.ApiException
import id.emergence.wher.data.remote.exceptions.NoInternetException
import id.emergence.wher.data.remote.json.MessageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import logcat.logcat
import org.json.JSONException
import retrofit2.Response

open class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            logcat { "Response is " + response.body().toString() }
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let { errStr ->
                try {
                    logcat { "Error response is " + decodeErrorApiResponse(errStr) }
                    message.append(decodeErrorApiResponse(errStr))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            throw ApiException(message.toString())
        }
    }
}

inline fun <T> wrapFlowApiCall(crossinline function: suspend () -> Result<T>): Flow<Result<T>> =
    flow {
        try {
            emit(function())
        } catch (ex: ApiException) {
            emit(Result.failure(ex))
        } catch (ex: NoInternetException) {
            emit(Result.failure(ex))
        }
    }

private val json =
    Json {
        ignoreUnknownKeys = true
    }

fun decodeErrorApiResponse(str: String): String =
    json
        .decodeFromString(
            MessageResponse.serializer(),
            str,
        ).message
