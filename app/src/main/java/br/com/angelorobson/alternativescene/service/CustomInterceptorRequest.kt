package br.com.angelorobson.alternativescene.service

import br.com.angelorobson.alternativescene.application.AlternativeSceneApplication
import br.com.angelorobson.alternativescene.application.commom.utils.handlerstatuscode.HandlerErrorStatusCode
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CustomInterceptorRequest : Interceptor {

    private val noInternetConnectionErrorCodeEnum = 600
    private val unProcessableEntityStatusCode = 422
    private val socketTimoutStatusCode = 1000

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response? = null
        val original = chain.request()
        val requestBuilder: Request.Builder

        requestBuilder = getRequestbuilder(original)
        response = getResponse(response, chain, requestBuilder)
        validateStatusCode(response!!)

        return response
    }

    private fun getResponse(response: Response?, chain: Interceptor.Chain, requestBuilder: Request.Builder): Response? {
        var newResponse = response
        try {
            newResponse = chain.proceed(requestBuilder.build())
        } catch (ex: Exception) {
            when (ex) {
                is UnknownHostException -> {
                    handlerException(noInternetConnectionErrorCodeEnum)
                }
                is SocketTimeoutException -> {
                    handlerException(socketTimoutStatusCode)
                }
                is ConnectException -> {
                    handlerException(noInternetConnectionErrorCodeEnum)
                }
                is IOException -> {
                    handlerException(noInternetConnectionErrorCodeEnum)
                }
                else -> handlerException(0)
            }
        }
        return newResponse
    }

    private fun getRequestbuilder(original: Request): Request.Builder {
        return original.newBuilder()
    }

    private fun validateStatusCode(response: Response) {
        val successStatusCodeRange = 200..298
        val isStatusCodeRangeSuccess = response.code() in successStatusCodeRange
        val isNotStatusCodeRangeSuccess = !isStatusCodeRangeSuccess

        when {
            isNotStatusCodeRangeSuccess -> {
                if (response.code() != unProcessableEntityStatusCode) {
                    handlerException(response.code())
                }
            }
        }
    }

    private fun handlerException(statusCode: Int) {
        val messageFromStringResource = HandlerErrorStatusCode.fromInt(statusCode)
        throw Throwable(messageFromStringResource?.getMessageFromResourceString().toString())
    }

}
