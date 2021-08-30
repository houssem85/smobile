package fr.strada.smobile.ui.mes_frais.nouvelle_depense

object Utils {
    fun isDecimalNumber(str:String):Boolean{
        val strRex="^[0-9]*\\.?[0-9]+$"
        return str.matches(strRex.toRegex())
    }
}