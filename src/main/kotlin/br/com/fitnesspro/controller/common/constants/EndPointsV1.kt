package br.com.fitnesspro.controller.common.constants

object EndPointsV1 {
    private const val API_V1 = "/api/v1"

    const val PERSON_V1 = "$API_V1/person"
    const val PERSON_ACADEMY_TIME = "/academy/time"
    const val PERSON_BATCH = "/batch"

    const val ACADEMY_V1 = "$API_V1/academy"
    const val ACADEMY_BATCH = "/batch"

    const val AUTHENTICATION_V1 = "$API_V1/authentication"
    const val AUTHENTICATION_LOGIN = "/login"
    const val AUTHENTICATION_LOGOUT = "/logout"

    const val SCHEDULER_V1 = "$API_V1/scheduler"
    const val SCHEDULER_BATCH = "/batch"
    const val SCHEDULER_CONFIG = "/config"
    const val SCHEDULER_CONFIG_BATCH = "/config/batch"
}