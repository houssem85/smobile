package fr.strada.smobile.data.models.activites.day

enum class TypeActiviteConduite(val libelle:String,val couleur:String,val height:Float) {
    TRAVAIL("Travail","#E5647A",3F),
    ROPOS("Repos","#4D65E0",2F),
    COUPURE("Coupure","#4D65E0",2F),
    CONDUITE("Conduite","#51CF9D",5F),
    DE("Double Équipage","#c6cad6",5F),
    MAD("Disponibilité","#fece3b",5F),
    AC("Double Équipage","#ff4fff",5F),



}

val listOfTypesActiviteConduite = listOf(TypeActiviteConduite.TRAVAIL, TypeActiviteConduite.ROPOS, TypeActiviteConduite.CONDUITE,TypeActiviteConduite.COUPURE, TypeActiviteConduite.MAD,TypeActiviteConduite.AC,TypeActiviteConduite.DE)