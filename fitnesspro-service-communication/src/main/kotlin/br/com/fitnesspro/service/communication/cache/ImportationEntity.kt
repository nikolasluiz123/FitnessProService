package br.com.fitnesspro.service.communication.cache

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ImportationEntity(val entitySimpleName: String)