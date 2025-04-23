package br.com.fitnesspro.service.repository.serviceauth

import br.com.fitnesspro.service.models.serviceauth.ServiceToken
import org.springframework.data.jpa.repository.JpaRepository

interface IServiceTokenRepository: JpaRepository<ServiceToken, String>