package br.com.fitnesspro.shared.communication.paging

data class CommonPageInfos(
    override val pageSize: Int = 100,
    override var pageNumber: Int = 0,
): PageInfos