package br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO

interface IDeviceDTO : AuditableDTO {
    val model: String?
    val brand: String?
    val androidVersion: String?
    val firebaseMessagingToken: String?
    var zoneId: String?
    var active: Boolean
    var application: IApplicationDTO?
    var person: IPersonDTO?
}