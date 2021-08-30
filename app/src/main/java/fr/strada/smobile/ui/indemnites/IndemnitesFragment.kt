package fr.strada.smobile.ui.indemnites


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.shawnlin.numberpicker.NumberPicker
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentIndemnitesBinding
import fr.strada.smobile.di.indemnites.IndemnityComponent
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_WEEK
import fr.strada.smobile.di.mes_activities.DIALOG_LOADER
import fr.strada.smobile.ui.activities.Utils.getCurrentLocal
import fr.strada.smobile.ui.activities.Utils.getDate
import fr.strada.smobile.ui.activities.Utils.getDisplayedValuesFromWeeksOfYear
import fr.strada.smobile.ui.activities.Utils.getWeeksOfYear
import fr.strada.smobile.ui.activities.mensuel.MesActivitiesMensuelFragment
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.indemnites.hebdomadaire.IndemniteHebdomadaireFragment
import fr.strada.smobile.ui.indemnites.journalier.IndemniteJournalierFragment
import fr.strada.smobile.ui.indemnites.mensuel.IndemniteMensuelleFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.KEY_MONTH
import fr.strada.smobile.utils.KEY_YEAR
import fr.strada.smobile.utils.listner.DialogChangeWeekListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_activities.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * A simple [Fragment] subclass.
 */
class IndemnitesFragment : BaseFragment(), DialogChangeWeekListner {

    lateinit var component: IndemnityComponent
    lateinit var viewModel: IndemniteViewModel
    lateinit var binding: FragmentIndemnitesBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Named(DIALOG_CHANGE_WEEK)
    @Inject
    internal lateinit var dialogChangeWeek: Dialog

    @Named(DIALOG_LOADER)
    @Inject
    internal lateinit var dialogLoader: Dialog

    private var isOrientationChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_indemnites, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariableIsOriontationChanged(savedInstanceState)
        if (!isOrientationChanged) {
            startAppropriatedFragment()
        }
        observeCurrentFragment()
        setupDateCurrentJour()
        showDialogChangeWeekIfIsRecentlyVisible()

    }

    private fun initVariableIsOriontationChanged(savedInstanceState: Bundle?) {
        isOrientationChanged = savedInstanceState != null
    }

    private fun startAppropriatedFragment() {
        val isArgumentsExist = arguments != null
        if (!isArgumentsExist) {
            startJournalierFragment()
        } else {
            val year = requireArguments().getInt("year")
            val month = requireArguments().getInt("month")
            val week = requireArguments().getInt("week")
            startHebdomadaireFragment(year, month, week)
        }
    }

    private fun showDialogChangeWeekIfIsRecentlyVisible() {
        if (viewModel.isDialogHebdomadireShown) {
            dialogChangeWeek.show()
        }
    }

    private fun startMensuelFragment(year: Int, month: Int) {
        val args = Bundle()
        args.putInt(KEY_MONTH, month)
        args.putInt(KEY_YEAR, year)
        val fragment = MesActivitiesMensuelFragment()
        fragment.arguments = args
        viewModel.currentFragment.value = fragment
        setYearMonthDay(year, month, 1)
    }

    fun startHebdomadaireFragment(year: Int, month: Int, week: Int) {
        val args = Bundle()
        args.putInt("year", year)
        args.putInt("month", month)
        args.putInt("week", week)
        val fragment = IndemniteHebdomadaireFragment()
        fragment.arguments = args
        viewModel.currentFragment.value = fragment
        setYearMonthDay(year, month, 1)
    }

    private fun startJournalierFragment() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        viewModel.currentFragment.value = IndemniteJournalierFragment()
        setYearMonthDay(year, month, day)
    }

    fun startJournalierFragment(year: Int, month: Int, day: Int) {
        val fragment = IndemniteJournalierFragment()
        val args = Bundle()
        args.putInt("day", day)
        args.putInt("month", month)
        args.putInt("year", year)
        fragment.arguments = args
        viewModel.currentFragment.value = fragment
        setYearMonthDay(year, month, day)
    }

    private fun observeCurrentFragment() {
        viewModel.currentFragment.observe(viewLifecycleOwner, {
            if (!isOrientationChanged) {
                showLoading()
                changeFragment(it)
            } else {
                isOrientationChanged = false
            }
        })
    }

    private fun setupDateCurrentJour() {
        val cal = Calendar.getInstance()
        lblDateJour?.text = cal.get(Calendar.DAY_OF_MONTH).toString()
    }

    fun changeFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    //--------------------------------- custom Navigation ------------------------------------//

    override fun initComponent() {
        if (activity is MainActivity) {
            component = (activity as MainActivity).component
                .indemnityComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(null)
                .setDialogChangeWeekListner(this)
                .setDialogChangeYearListner(null)
                .build()
        } else {
            component = (activity as MainTabletteActivity).component
                .indemnityComponent()
                .setContext(requireContext())
                .setDialogChangeMonthListner(null)
                .setDialogChangeWeekListner(this)
                .setDialogChangeYearListner(null)
                .build()
        }
    }

    override fun injectDependencies() {
        component.injectIndemnites(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(IndemniteViewModel::class.java)
        // reset viewModel
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnHebdomadaireEvent.observe(viewLifecycleOwner, {
            it?.let {
                if (viewModel.currentFragment.value is IndemniteHebdomadaireFragment) {
                    startJournalierFragment()
                } else {
                    dialogChangeWeek.show()
                }
            }
        })
        viewModel.pressBtnMensuelEvent.observe(viewLifecycleOwner, {
            it?.let {
                if (viewModel.currentFragment.value is IndemniteMensuelleFragment) {
                    startJournalierFragment()
                } else {
                    startMensuelFragment(viewModel.year.value!!, viewModel.month.value!!)
                }
            }
        })
        viewModel.pressBtnOpenMenuEvent.observe(viewLifecycleOwner, {
            it?.let {
                (activity as MainActivity).openDrawer()
            }
        })
        viewModel.pressBtnCurrentDayEvent.observe(viewLifecycleOwner, {
            it?.let {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                setYearMonthDay(year, month, day)
                when (viewModel.currentFragment.value) {
                    is IndemniteJournalierFragment -> {
                        (viewModel.currentFragment.value as IndemniteJournalierFragment).calendarView.changeToToday()
                    }
                    is IndemniteHebdomadaireFragment -> {
                        (viewModel.currentFragment.value as IndemniteHebdomadaireFragment).calendarView.changeToToday()
                    }
                    is IndemniteMensuelleFragment -> {
                        (viewModel.currentFragment.value as IndemniteMensuelleFragment).calendarView.changeToToday()
                    }
                    else -> {

                    }
                }
            }

        })
    }

    //----------------------DialogChangeWeekListner------------------------//

    override fun onShowListner(dialog: Dialog, weekPicker: NumberPicker) {
        try {
            viewModel.isDialogHebdomadireShown = true
            val year = viewModel.year.value!!
            val month = viewModel.month.value!! + 1
            val day = viewModel.day.value!!
            val weeksOfYear = getWeeksOfYear(year, Calendar.MONDAY, locale)
            weekPicker.minValue = 0
            weekPicker.maxValue = weeksOfYear.weeksCount - 1
            weekPicker.displayedValues = getDisplayedValuesFromWeeksOfYear(
                weeksOfYear,
                requireContext()
            )
            // determine index
            for (i in 0 until weeksOfYear.weeksCount) {
                val date = getDate(year, month, day, locale)
                val startDate = getDate(
                    weeksOfYear.startDates[i].year,
                    weeksOfYear.startDates[i].month,
                    weeksOfYear.startDates[i].day,
                    locale
                )
                val endDate = getDate(
                    weeksOfYear.endDates[i].year,
                    weeksOfYear.endDates[i].month,
                    weeksOfYear.endDates[i].day,
                    locale
                )
                if ((date.before(endDate) && date.after(startDate)) || (date == startDate) || (date == endDate)) {
                    weekPicker.value = i
                    break
                }
            }
            //
        } catch (ex: Exception) {

        }
    }

    override fun onDismissListner(dialog: Dialog) {
        viewModel.isDialogHebdomadireShown = false
    }

    override fun onClickTerminerListner(dialog: Dialog, weekPicker: NumberPicker) {
        dialog.dismiss()
        val locale = getCurrentLocal(requireContext())
        val weeksOfYear = getWeeksOfYear(viewModel.year.value!!, Calendar.MONDAY, locale)
        val year = viewModel.year.value!!
        val month = weeksOfYear.endDates[weekPicker.value].month - 1
        val week = weeksOfYear.weeksNumbers[weekPicker.value]
        startHebdomadaireFragment(year, month, week)
    }

    override fun onClickAnnulerListner(dialog: Dialog) {
        dialog.dismiss()
    }

    //--------------------------------- Extra -------------------------------------//


    fun setYearMonthDay(year: Int, month: Int, day: Int) {
        viewModel.year.value = year
        viewModel.month.value = month
        viewModel.day.value = day
    }

    fun dismissLoading() {
        if (activity is MainTabletteActivity) {
            (activity as MainTabletteActivity).dismissLoading()
        } else {
            (activity as MainActivity).dismissLoading()
        }
    }

    fun showLoading() {
        if (activity is MainTabletteActivity) {
            (activity as MainTabletteActivity).showLoading()
        } else {
            (activity as MainActivity).showLoading()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }
}
