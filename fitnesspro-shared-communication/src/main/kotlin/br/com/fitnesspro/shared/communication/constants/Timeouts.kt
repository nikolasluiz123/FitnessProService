package br.com.fitnesspro.shared.communication.constants

object Timeouts {

    /**
     * Timeout baixo de 3 minutos, deve ser usado na maior parte dos casos
     */
    const val OPERATION_LOW_TIMEOUT = 180

    /**
     * Timeout médio de 5 minutos
     */
    const val OPERATION_MEDIUM_TIMEOUT = 300

    /**
     * Maior timeout aceitável de 10 minutos, deve ser usado em casos extremos, como batchs
     */
    const val OPERATION_HIGH_TIMEOUT = 600

    /**
     * Timeout de conexão de 1 minuto, pode ser usado por clients.
     */
    const val CONNECT_TIMEOUT = 60
}