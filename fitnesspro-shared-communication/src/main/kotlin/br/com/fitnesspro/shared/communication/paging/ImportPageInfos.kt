package br.com.fitnesspro.shared.communication.paging

data class ImportPageInfos(
    override val pageSize: Int = 200,
): SyncPageInfos
