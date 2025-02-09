package br.com.fitnesspro.controller.constants

object Timeouts {

    /**
     * Timeout baixo de 3 minutos, deve ser usado na maior parte dos casos
     */
    const val LOW_TIMEOUT = 180

    /**
     * Timeout médio de 5 minutos
     */
    const val MEDIUM_TIMEOUT = 300

    /**
     * Maior timeout aceitável de 10 minutos, deve ser usado em casos extremos, como batchs
     */
    const val HIGH_TIMEOUT = 600
}