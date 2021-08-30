package fr.strada.smobile.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.utils.KeyBoardUtils
import java.util.*

open class BaseNFragment : Fragment() {

    protected lateinit var locale: Locale

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocale()
    }

    private fun initLocale()
    {
        locale = Utils.getCurrentLocal(requireContext())
    }

    override fun onStart()
    {
        super.onStart()
        KeyBoardUtils.hideKeyboard(requireActivity())
    }
}