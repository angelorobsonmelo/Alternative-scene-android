package br.com.angelorobson.alternativescene.service.remote.userdevice

import br.com.angelorobson.alternativescene.domain.request.UserDeviceRequest
import br.com.angelorobson.alternativescene.service.commom.ResponseBase
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface UserDeviceApiDataSource {

    @POST("v1/userDevices")
    fun saveUserDevice(@Body userDeviceRequest: UserDeviceRequest): Observable<ResponseBase<Boolean>>
}