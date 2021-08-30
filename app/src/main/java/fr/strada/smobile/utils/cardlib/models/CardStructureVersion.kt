package fr.strada.smobile.utils.cardlib.models

import com.google.gson.annotations.SerializedName
class CardStructureVersion{
	@SerializedName("byte1") var byte1 : Int = 0
	@SerializedName("byte2") var byte2 : Int = 0
	constructor(byte1: Int, byte2: Int) {
		this.byte1 = byte1
		this.byte2 = byte2
	}
	constructor(byte1: ByteArray) {

		this.byte1 = byte1[2].toInt()
		this.byte2 = byte1[3].toInt()
	}


}