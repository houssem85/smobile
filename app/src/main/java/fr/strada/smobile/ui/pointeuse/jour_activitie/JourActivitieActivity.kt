package fr.strada.smobile.ui.pointeuse.jour_activitie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.strada.smobile.R
import fr.strada.smobile.data.models.pointeuse.JourActivite
import fr.strada.smobile.ui.pointeuse.PointeuseFragment.Companion.EXTRA_JOUR_ACTIVITE

class JourActivitieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jour_activitie)
        if(savedInstanceState == null){
            setJourActivitieFragment()
        }
    }

    private fun setJourActivitieFragment(){
        if(intent.hasExtra(EXTRA_JOUR_ACTIVITE))
        {
            val jourActivitie : JourActivite = intent.getParcelableExtra(EXTRA_JOUR_ACTIVITE)!!
            val fragment = JourActivitieFragment.factory(jourActivitie)
            supportFragmentManager.beginTransaction().add(R.id.fragment_container_view, fragment).commit()
        }else
        {
            finish()
        }
    }
}