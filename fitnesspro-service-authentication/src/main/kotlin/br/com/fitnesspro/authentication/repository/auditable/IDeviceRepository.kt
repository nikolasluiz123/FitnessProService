package br.com.fitnesspro.authentication.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.serviceauth.Device

interface IDeviceRepository: IAuditableFitnessProRepository<Device> {

    fun findByFirebaseMessagingTokenIn(tokens: List<String>): List<Device>

    fun findByIdIn(ids: List<String>): List<Device>

    fun findByPersonIdInAndActiveIsTrue(personIds: List<String>): List<Device>

    fun findByActiveIsTrueAndFirebaseMessagingTokenIsNotNull(): List<Device>

    fun findByPersonIdAndActiveIsTrue(personId: String): Device?
}