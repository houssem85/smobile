package fr.strada.smobile.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.strada.smobile.R
import fr.strada.smobile.ui.activities.Utils.getCurrentLocal
import fr.strada.smobile.utils.KeyBoardUtils
import java.util.*

abstract class BaseFragment : Fragment() {

    protected lateinit var locale: Locale
    protected var savedInstanceState: Bundle? = null
    protected var isTableteSize = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.savedInstanceState = savedInstanceState
        initLocale()
        initSize()
        initComponent()
        injectDependencies()
        initViewModel()
        bindViewModel()
        setupNavigation()
        initArguments()
        initArguments(savedInstanceState)
        KeyBoardUtils.hideKeyboard(requireActivity())
    }

    fun initLocale()
    {
        locale = getCurrentLocal(requireContext())
    }

    private fun initSize(){
        isTableteSize = resources.getBoolean(R.bool.isTablet)
    }

    open fun initArguments(){}

    open fun initArguments(savedInstanceState: Bundle?){}

    abstract fun initComponent()

    abstract fun injectDependencies()

    abstract fun initViewModel()

    abstract fun bindViewModel()

    abstract fun setupNavigation()

    override fun onStart()
    {
        super.onStart()
        KeyBoardUtils.hideKeyboard(requireActivity())
    }
}