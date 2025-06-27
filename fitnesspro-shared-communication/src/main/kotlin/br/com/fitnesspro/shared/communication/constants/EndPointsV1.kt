package br.com.fitnesspro.shared.communication.constants

object EndPointsV1 {
    private const val API_V1 = "/api/v1"

    const val PERSON = "person"
    const val PERSON_V1 = "$API_V1/$PERSON"
    const val PERSON_ACADEMY_TIME = "/academy/time"
    const val PERSON_EXPORT = "/export"
    const val PERSON_EMAIL = "/email"
    const val PERSON_ACADEMY_TIME_EXPORT = "/academy/time/export"
    const val PERSON_IMPORT = "/import"
    const val PERSON_ACADEMY_TIME_IMPORT = "/academy/time/import"
    const val PERSON_USER_IMPORT = "/user/import"
    const val PERSON_LIST = "/list"
    const val PERSON_COUNT = "/count"

    const val ACADEMY = "academy"
    const val ACADEMY_V1 = "$API_V1/$ACADEMY"
    const val ACADEMY_EXPORT = "/export"
    const val ACADEMY_IMPORT = "/import"
    const val ACADEMY_LIST = "/list"
    const val ACADEMY_COUNT = "/count"

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

    const val LOGS = "logs"
    const val LOGS_V1 = "$API_V1/$LOGS"
    const val LOGS_COUNT = "/count"
    const val LOGS_PACKAGE = "/package"
    const val LOGS_PACKAGE_COUNT = "$LOGS_PACKAGE/count"

    const val CACHE = "cache"
    const val CACHE_V1 = "$API_V1/$CACHE"
    const val CACHE_LIST = "/list"
    const val CACHE_ENTRIES = "/entries"
    const val CACHE_CLEAR = "/clear"

    const val TOKEN = "token"
    const val TOKEN_V1 = "$API_V1/$TOKEN"
    const val TOKEN_INVALIDATE = "/invalidate/{id}"
    const val TOKEN_INVALIDATE_ALL = "/invalidate"
    const val TOKEN_SECRET = "/secret"
    const val TOKENS_COUNT = "/count"
    const val TOKEN_BY_ID = "/{id}"

    const val DEVICE = "device"
    const val DEVICE_V1 = "$API_V1/$DEVICE"
    const val DEVICE_COUNT = "/count"

    const val APPLICATION = "application"
    const val APPLICATION_V1 = "$API_V1/$APPLICATION"

    const val SCHEDULED_TASK = "scheduled/task"
    const val SCHEDULED_TASK_V1 = "$API_V1/$SCHEDULED_TASK"

    const val NOTIFICATION = "notification"
    const val NOTIFICATION_V1 = "$API_V1/$NOTIFICATION"
    const val NOTIFICATION_NOTIFY_ALL = "/all"

    const val EXERCISE: String = "exercise"
    const val EXERCISE_V1 = "$API_V1/$EXERCISE"
    const val EXERCISE_EXPORT = "/export"
    const val EXERCISE_IMPORT = "/import"

    const val REPORT: String = "report"
    const val REPORT_V1 = "$API_V1/$REPORT"
    const val REPORT_IMPORT = "/import"
    const val SCHEDULER_REPORT = "/scheduler"
    const val DELETE_SCHEDULER_REPORT = "${SCHEDULER_REPORT}/{reportId}"
    const val DELETE_ALL_SCHEDULER_REPORT = "${SCHEDULER_REPORT}/all"
    const val SCHEDULER_REPORT_EXPORT = "/$SCHEDULER/export"
    const val SCHEDULER_REPORT_IMPORT = "/$SCHEDULER/import"

}