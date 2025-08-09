package br.com.fitnesspro.authentication.repository.jpa

import br.com.fitnesspro.models.serviceauth.ServiceToken
import org.springframework.data.jpa.repository.JpaRepository

interface IServiceTokenRepository: JpaRepository<ServiceToken, String>