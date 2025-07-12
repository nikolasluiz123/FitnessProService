package br.com.fitnesspro.repository.jpa.serviceauth

import br.com.fitnesspro.models.serviceauth.ServiceToken
import org.springframework.data.jpa.repository.JpaRepository

interface IServiceTokenRepository: JpaRepository<ServiceToken, String>