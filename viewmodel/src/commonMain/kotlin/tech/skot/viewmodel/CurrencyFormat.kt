package tech.skot.viewmodel

expect class CurrencyFormat(iso:String) {
    fun format(double:Double):String
}