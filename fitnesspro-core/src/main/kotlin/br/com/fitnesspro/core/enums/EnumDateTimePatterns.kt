package br.com.fitnesspro.core.enums

enum class EnumDateTimePatterns(val pattern: String) {
    DATE("dd/MM/yyyy"),
    DATE_SQL("yyyy-MM-dd"),
    DATE_ONLY_NUMBERS("ddMMyyyy"),
    TIME("HH:mm"),
    TIME_ONLY_NUMBERS("HHmm"),
    DATE_TIME("dd/MM/yyyy HH:mm"),
    DATE_TIME_SHORT("dd/MM/yy HH:mm"),
    DAY_MONTH("dd/MM"),
    DATE_TIME_SQLITE("yyyy-MM-dd HH:mm"),
    DATE_TIME_FILE_NAME("dd_MM_yyyy_HHmmss"),
    MONTH_YEAR("MMMM 'de' yyyy"),
    DATE_TIME_CACHE_KEY("dd_MM_yyyy_HHmmss_SSSS")
}