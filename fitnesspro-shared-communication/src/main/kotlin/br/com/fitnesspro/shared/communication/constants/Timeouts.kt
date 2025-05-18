package br.com.fitnesspro.shared.communication.constants

object Timeouts {

    /**
     * Timeout 1 minuto, utilizado pelos clients para não ficarem tanto tempo tentando processar algo se o servidor
     * estiver ‘offline’, sobrecarregado ou algo do tipo.
     */
    const val OPERATION_VERY_LOW_TIMEOUT = 60

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
}