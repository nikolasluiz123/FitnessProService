package br.com.fitnesspro.controller.common.responses

class ReadServiceResponse<DTO>(
    var values: List<DTO>,
    override var code: Int,
    override var success: Boolean,
    override var error: String? = null
) : IFitnessProServiceResponse