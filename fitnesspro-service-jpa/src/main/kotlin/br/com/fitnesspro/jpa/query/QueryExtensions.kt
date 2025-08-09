package br.com.fitnesspro.jpa.query

import jakarta.persistence.Query

fun Query.setParameters(params: List<Parameter>) {
    params.forEach { param -> setParameter(param.name, param.value) }
}

@Suppress("UNCHECKED_CAST", "UNUSED")
fun <T> Query.getResultList(resultType: Class<T>): List<T> {
    return resultList as List<T>
}