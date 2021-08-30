package fr.strada.smobile.ui.pointeuse

import android.app.Activity
import android.content.Intent
import fr.strada.smobile.R
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.OPEN_POINTEUSE

class OpenPointeuseActivity : Activity() {

    override fun onResume() {
        super.onResume()
        finish()
        val tabletSize = this.resources.getBoolean(R.bool.isTablet)
        if(tabletSize) {
            startMainTabletteActivity()
        }else {
            startMainActivity()
        }
    }

    private fun startMainActivity()
    {
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = OPEN_POINTEUSE
        startActivity(intent)
    }

    private fun startMainTabletteActivity()
    {
        val intent = Intent(this,MainTabletteActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = OPEN_POINTEUSE
        startActivity(intent)
    }
}
