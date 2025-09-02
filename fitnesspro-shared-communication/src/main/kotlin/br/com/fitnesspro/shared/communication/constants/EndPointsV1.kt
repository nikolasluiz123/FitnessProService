package br.com.fitnesspro.shared.communication.constants

object EndPointsV1 {
    private const val API_V1 = "/api/v1"

    const val PERSON = "person"
    const val PERSON_V1 = "$API_V1/$PERSON"
    const val PERSON_EMAIL = "/email"
    const val PERSON_LIST = "/list"
    const val PERSON_COUNT = "/count"

    const val ACADEMY = "academy"
    const val ACADEMY_V1 = "$API_V1/$ACADEMY"
    const val ACADEMY_LIST = "/list"
    const val ACADEMY_COUNT = "/count"

    const val AUTHENTICATION = "authentication"
    const val AUTHENTICATION_V1 = "$API_V1/$AUTHENTICATION"
    const val AUTHENTICATION_LOGIN = "/login"
    const val AUTHENTICATION_LOGOUT = "/logout"

    const val SCHEDULER = "scheduler"
    const val SCHEDULER_V1 = "$API_V1/$SCHEDULER"
    const val SCHEDULER_RECURRENT = "/recurrent"

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

    const val STORAGE = "storage"
    const val STORAGE_V1 = "$API_V1/$STORAGE"
    const val UPLOAD_REPORTS = "upload/report"
    const val UPLOAD_VIDEOS = "upload/video"

    const val SYNC = "sync"
    const val SYNC_V1 = "$API_V1/$SYNC"
    const val SYNC_IMPORT_GENERAL = "/common/import"
    const val SYNC_EXPORT_GENERAL = "/common/export"
    const val SYNC_IMPORT_SCHEDULER = "/scheduler/import"
    const val SYNC_EXPORT_SCHEDULER = "/scheduler/export"
    const val SYNC_IMPORT_WORKOUT = "/workout/import"
    const val SYNC_EXPORT_WORKOUT = "/workout/export"

}