package fr.strada.smobile.ui_tablette.main_tablette

import android.content.Context
import androidx.core.content.ContextCompat
import fr.strada.sidemenuview.data.MenuItem
import fr.strada.sidemenuview.data.SubMenuItem
import fr.strada.smobile.R
import fr.strada.smobile.data.models.access_functionalities.Functionality
import fr.strada.smobile.data.models.access_functionalities.sortedFunctionalities
import fr.strada.smobile.data.models.userinfo.FunctionalityRow
import fr.strada.smobile.ui.apropos.AProposFragment
import fr.strada.smobile.ui.card.ReaderActivityKotlin
import fr.strada.smobile.ui.profil.ProfilFragment
import fr.strada.smobile.ui.reglage.ReglageFragment
import fr.strada.smobile.ui_tablette.accueil.AccueilTabletteFragment
import fr.strada.smobile.ui_tablette.infractions_tablette.InfractionsTabletteFragment
import fr.strada.smobile.ui_tablette.mes_activities_tablette.MesActivitiesTabletteFragment
import fr.strada.smobile.ui_tablette.mes_frais_tablette.MesFraisTabletteFragment
import fr.strada.smobile.ui_tablette.mes_indemnites_tablette.MesIndemnitesTabletteFragment
import fr.strada.smobile.ui_tablette.pointeuse.PointeuseTabletteFragment

fun generateMenuItemFromUserFunctionalities(context : Context, userFunctionalities : List<FunctionalityRow>) : ArrayList<MenuItem>{
    val menuItems = ArrayList<MenuItem>()
    val icAccueil = ContextCompat.getDrawable(context, R.drawable.ic_accueil_tablet)
    val accueil = MenuItem(icAccueil!!,context.resources.getString(R.string.accueil),AccueilTabletteFragment(),null)
    menuItems.add(accueil)
    sortedFunctionalities.forEachIndexed { index, functionality ->
        val isFunctionalityExist = userFunctionalities.any {
            it.code == functionality.code
        }
        if(isFunctionalityExist){
            when(functionality.code){
                Functionality.POINTEUSE.code ->{
                    val icPointeuse = ContextCompat.getDrawable(context, R.drawable.ic_pointeuse_tablet)
                    val icClockMenu = ContextCompat.getDrawable(context, R.drawable.ic_clock_menu)
                    val fragment = PointeuseTabletteFragment.factory(false)
                    val pointeuse = MenuItem(icPointeuse!!,context.getString(R.string.pointeuse),fragment,null,icClockMenu)
                    menuItems.add(pointeuse)
                }
                Functionality.MES_ACTIVITES.code ->{
                    val icMesActivites = ContextCompat.getDrawable(context, R.drawable.ic_mes_activites_tablet)
                    val mesActivites = MenuItem(icMesActivites!!,context.getString(R.string.mes_activites), MesActivitiesTabletteFragment.newInstance(),null)
                    menuItems.add(mesActivites)
                }
                Functionality.LECTURE_CARTE_CONDUCTEUR.code -> {
                    val cardPicture = ContextCompat.getDrawable(context, R.drawable.ic_group_6976)
                    val card = MenuItem(
                        cardPicture!!,
                        context.getString(R.string.card),
                        null,
                        ReaderActivityKotlin()
                    )
                    menuItems.add(card)
                }
            }
        }
    }
    val icInfractions = ContextCompat.getDrawable(context, R.drawable.ic_infractions_tablette)
    val infractions = MenuItem(
        icInfractions!!,
        context.getString(R.string.infractions),
        InfractionsTabletteFragment.newInstance(),
        null
    )
    menuItems.add(infractions)
    //
    val icMesFrais = ContextCompat.getDrawable(context, R.drawable.ic_mes_frais_tablette)
    val subMenuItems = ArrayList<SubMenuItem>()
    subMenuItems.add(
        SubMenuItem(
            context.getString(R.string.Mes_notes_de_frais),
            MesFraisTabletteFragment.newInstance()
        )
    )
    subMenuItems.add(
        SubMenuItem(
            context.getString(R.string.Indemnités_et_primes),
            MesIndemnitesTabletteFragment.newInstance()
        )
    )
    val mesFrais = MenuItem(
        icMesFrais!!,
        context.getString(R.string.mes_frais),
        null,
        null,
        null,
        subMenuItems,
        false
    )
    menuItems.add(mesFrais)
    //
    val icregelage = ContextCompat.getDrawable(context, R.drawable.ic_twotone_settings_24)
    val regelage =
        MenuItem(
            icregelage!!,
            context.getString(R.string.reglage),
            ReglageFragment.newInstance(),
            null
        )
    menuItems.add(regelage)
    //
    val icProfil = ContextCompat.getDrawable(context, R.drawable.ic_profil_tablet)
    val profil =
        MenuItem(icProfil!!, context.getString(R.string.profil), ProfilFragment.newInstance(), null)
    menuItems.add(profil)
    val icAPropos = ContextCompat.getDrawable(context, R.drawable.ic_a_propos_tablet)
    val aPropos = MenuItem(
        icAPropos!!,
        context.getString(R.string.a_propos),
        AProposFragment.newInstance(),
        null
    )
    menuItems.add(aPropos)
    return menuItems
}