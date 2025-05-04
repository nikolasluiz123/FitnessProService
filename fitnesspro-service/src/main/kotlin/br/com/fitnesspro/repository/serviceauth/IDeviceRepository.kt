package br.com.fitnesspro.repository.serviceauth

import br.com.fitnesspro.models.serviceauth.Device
import br.com.fitnesspro.repository.common.IAuditableFitnessProRepository


interface IDeviceRepository: IAuditableFitnessProRepository<Device> {

    fun findByFirebaseMessagingTokenIn(tokens: List<String>): List<Device>

    fun findByIdIn(ids: List<String>): List<Device>

    fun findByPersonIdInAndActiveIsTrue(personIds: List<String>): List<Device>

    fun findByActiveIsTrueAndFirebaseMessagingTokenIsNotNull(): List<Device>

    fun findByPersonIdAndActiveIsTrue(personId: String): Device
}