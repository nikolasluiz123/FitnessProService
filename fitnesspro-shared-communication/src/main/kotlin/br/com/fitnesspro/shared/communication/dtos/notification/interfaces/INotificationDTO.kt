package br.com.fitnesspro.shared.communication.dtos.notification.interfaces

interface INotificationDTO : IGlobalNotificationDTO {
    var devicesIds: List<String>
}