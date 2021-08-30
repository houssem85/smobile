package fr.strada.smobile.utils.listner

interface ItemDemandeListener {
    fun onClickDemandeAccepteeListner(position:Int)

    fun onPressBtnDeleteListener (position: Int)

    fun onClickDemandeNonAccepteeListener(position: Int)
}