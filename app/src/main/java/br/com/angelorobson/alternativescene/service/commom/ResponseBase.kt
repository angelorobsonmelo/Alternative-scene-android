package br.com.angelorobson.alternativescene.service.commom

import com.angelomelo.alternative.application.service.remote.commom.ContentObjects


class ResponseBase<T> {

    val data: T? = null

    var errors: List<String>? = null
        get() {
            if (field == null) {
                this.errors = ArrayList()
            }
            return field
        }

}


class ResponseListBase<T> {

    val data: ContentObjects<T>? = null

    var errors: List<String>? = null
        get() {
            if (field == null) {
                this.errors = ArrayList()
            }
            return field
        }

}