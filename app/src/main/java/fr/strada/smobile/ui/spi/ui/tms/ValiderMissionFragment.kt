package fr.strada.smobile.ui.spi.ui.tms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.strada.smobile.R


/**
 * A simple [Fragment] subclass.
 * Use the [ValiderMissionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ValiderMissionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_valider_mission, container, false)
    }

    companion object {

        fun newInstance() = ValiderMissionFragment()

    }
}