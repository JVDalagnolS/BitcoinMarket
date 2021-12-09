package com.example.bitcoinmarket.Objetos

class MoedaAtivos(
    var high: String?,
    var low: String,
    var vol: String,
    var last: String,
    var buy: String,
    var sell: String,
    var opem: String,
    var data: String

) {
    override fun toString(): String {
        return data
    }

}