package br.com.fitnesspro.repository.common.query

import jakarta.persistence.Query

fun Query.setParameters(params: List<Parameter>) {
    params.forEach { param -> setParameter(param.name, param.value) }
}