package br.com.fitnesspro.controller.constants

object EndPoints {
    private const val API_V1 = "/api/v1"

    const val PERSON_V1 = "$API_V1/person"
    const val PERSON_ACADEMY_TIME = "/academy/time"
    const val PERSON_BATCH = "/batch"

    const val ACADEMY_V1 = "$API_V1/academy"
    const val ACADEMY_BATCH = "/batch"

    const val AUTHENTICATION_V1 = "$API_V1/authentication"
    const val AUTHENTICATION_LOGIN = "/login"
    const val AUTHENTICATION_LOGOUT = "/logout"
}