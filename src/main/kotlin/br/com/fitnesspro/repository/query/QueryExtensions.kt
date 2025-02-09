package br.com.fitnesspro.repository.query

import jakarta.persistence.Query

fun Query.setParameters(params: List<Parameter>) {
    params.forEach { param -> setParameter(param.name, param.value) }
}