package br.com.fitnesspro.shared.communication.constants

object EndPointsV1 {
    private const val API_V1 = "/api/v1"

    const val PERSON = "person"
    const val PERSON_V1 = "$API_V1/$PERSON"
    const val PERSON_ACADEMY_TIME = "/academy/time"
    const val PERSON_EXPORT = "/export"
    const val PERSON_ACADEMY_TIME_EXPORT = "/academy/time/export"
    const val PERSON_IMPORT = "/import"
    const val PERSON_ACADEMY_TIME_IMPORT = "/academy/time/import"
    const val PERSON_USER_IMPORT = "/user/import"

    const val ACADEMY = "academy"
    const val ACADEMY_V1 = "$API_V1/$ACADEMY"
    const val ACADEMY_EXPORT = "/export"
    const val ACADEMY_IMPORT = "/import"

    const val AUTHENTICATION = "authentication"
    const val AUTHENTICATION_V1 = "$API_V1/$AUTHENTICATION"
    const val AUTHENTICATION_LOGIN = "/login"
    const val AUTHENTICATION_LOGOUT = "/logout"

    const val SCHEDULER = "scheduler"
    const val SCHEDULER_V1 = "$API_V1/$SCHEDULER"
    const val SCHEDULER_EXPORT = "/export"
    const val SCHEDULER_IMPORT = "/import"
    const val SCHEDULER_CONFIG = "/config"
    const val SCHEDULER_CONFIG_EXPORT = "/config/export"
    const val SCHEDULER_CONFIG_IMPORT = "/config/import"

    const val WORKOUT = "workout"
    const val WORKOUT_V1 = "$API_V1/$WORKOUT"
    const val WORKOUT_EXPORT = "/export"
    const val WORKOUT_IMPORT = "/import"
    const val WORKOUT_GROUP = "/group"
    const val WORKOUT_GROUP_EXPORT = "/group/export"
    const val WORKOUT_GROUP_IMPORT = "/group/import"

    const val LOGS_V1 = "$API_V1/logs"
}