package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName

class Ic(@SerializedName("cardChipIdentification") val cardChipIdentification: CardChipIdentification) {

    override fun toString(): String {
        return "Ic(cardChipIdentification=$cardChipIdentification)"
    }

}
