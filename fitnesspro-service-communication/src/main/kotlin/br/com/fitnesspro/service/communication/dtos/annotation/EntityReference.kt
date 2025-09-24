package br.com.fitnesspro.service.communication.dtos.annotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class EntityReference(val entitySimpleName: String)