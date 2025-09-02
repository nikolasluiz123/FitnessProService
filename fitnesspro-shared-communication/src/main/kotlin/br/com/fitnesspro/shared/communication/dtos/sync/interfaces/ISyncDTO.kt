package br.com.fitnesspro.shared.communication.dtos.sync.interfaces

interface ISyncDTO {
    fun isEmpty(): Boolean
    fun getMaxListSize(): Int
    fun getItemsCount(): Int
}