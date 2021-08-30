package fr.strada.smobile.ui.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shawnlin.numberpicker.NumberPicker
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.FragmentActivitiesBinding
import fr.strada.smobile.di.mes_activities.DIALOG_CHANGE_WEEK
import fr.strada.smobile.di.mes_activities.MesActivitiesComponent
import fr.strada.smobile.ui.activities.Utils.getCurrentLocal
import fr.strada.smobile.ui.activities.Utils.getDate
import fr.strada.smobile.ui.activities.Utils.getDisplayedValuesFromWeeksOfYear
import fr.strada.smobile.ui.activities.Utils.getWeeksOfYear
import fr.strada.smobile.ui.activities.hebdomadaire.MesActivitiesHebdomadaireFragment
import fr.strada.smobile.ui.activities.journalier.MyDailyActivitiesFragment
import fr.strada.smobile.ui.activities.mensuel.MesActivitiesMensuelFragment
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.KEY_MONTH
import fr.strada.smobile.utils.KEY_YEAR
import fr.strada.smobile.utils.listner.DialogChangeWeekListner
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.colaborateur_list_view.*
import kotlinx.android.synthetic.main.fragment_activities.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class MesActivitiesFragment : BaseFragment(), DialogChangeWeekListner {

    lateinit var component: MesActivitiesComponent
    lateinit var viewModel: MesActivitiesViewModel
    lateinit var binding: FragmentActivitiesBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    @Named(DIALOG_CHANGE_WEEK)
    @Inject
    internal lateinit var dialogChangeWeek: Dialog

    private var isOriontationChanged = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activities, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariableIsOriontationChanged(savedInstanceState)
        if (!isOriontationChanged) {
            startAppropriatedFragment()
        }
        observeCurrentFragment()
        setupDateCurrentJour()
        showDialogChangeWeekIfIsRecentlyVisible()
        teammanagerClick()
    }

    private fun teammanagerClick() {
        val list = listOf(
            CollaborateurItem(
                "User 1",
                "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
            ),
            CollaborateurItem(
                "User 2 ",
                "https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/gigs/49189614/original/135e88ee173e0e999adedf39263917453983b357/make-best-profile-pic-for-you.jpg"
            ),
            CollaborateurItem(
                "User 3",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQrKnU3Kyv4fzZnjeDg2WZl0QTxXtUTy1Fww&usqp=CAU"
            ),
            CollaborateurItem(
                "User 4",
                "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
            ),
            CollaborateurItem(
                "User 5",
                "https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/gigs/49189614/original/135e88ee173e0e999adedf39263917453983b357/make-best-profile-pic-for-you.jpg"
            ),
            CollaborateurItem(
                "User 6",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQrKnU3Kyv4fzZnjeDg2WZl0QTxXtUTy1Fww&usqp=CAU"
            ), CollaborateurItem(
                "User 7",
                "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
            ),
            CollaborateurItem(
                "User 8",
                "https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/gigs/49189614/original/135e88ee173e0e999adedf39263917453983b357/make-best-profile-pic-for-you.jpg"
            ),

        )
        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.colaborateur_list_view)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var selectedItem: CollaborateurItem?
        val colaborateursAdapter = ColaborateursAdapter(list) { it ->
            list.forEach {
                it.isSelected = false
            }
            list[it].isSelected = true
            selectedItem = list[it]
            texte_nameColl_appbar?.text = selectedItem!!.name
            imageselectedteammember?.load(selectedItem!!.image) {
                transformations(CircleCropTransformation())
            }
            dialog.recyclercollaborateur.adapter!!.notifyDataSetChanged()
            dialog.dismiss()
        }
        dialog.recyclercollaborateur.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = colaborateursAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
        team_managmentblock?.setOnClickListener {
            dialog.show()
        }
    }


    private fun initVariableIsOriontationChanged(savedInstanceState: Bundle?) {
        isOriontationChanged = savedInstanceState != null
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
        val fragment = MesActivitiesHebdomadaireFragment()
        fragment.arguments = args
        viewModel.currentFragment.value = fragment
        setYearMonthDay(year, month, 1)
    }

    private fun startJournalierFragment() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        viewModel.currentFragment.value = MyDailyActivitiesFragment()
        setYearMonthDay(year, month, day)
    }

    fun startJournalierFragment(year: Int, month: Int, day: Int) {
        val fragment = MyDailyActivitiesFragment()
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
            if (!isOriontationChanged) {
                showLoading()
                changeFragment(it)
            } else {
                isOriontationChanged = false
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
        when (activity) {
            is MainActivity -> {
                component = (activity as MainActivity).component
                    .mesActivitiesComponent()
                    .setContext(requireContext())
                    .setDialogChangeMonthListner(null)
                    .setDialogChangeWeekListner(this)
                    .setDialogChangeYearListner(null)
                    .build()
            }
            is MainTabletteActivity -> {
                component = (activity as MainTabletteActivity).component
                    .mesActivitiesComponent()
                    .setContext(requireContext())
                    .setDialogChangeMonthListner(null)
                    .setDialogChangeWeekListner(this)
                    .setDialogChangeYearListner(null)
                    .build()
            }
            else ->{
                component = SmobileApp.instance!!.appComponent.mainComponent().setContext(requireActivity())
                    .setOnClickListener(null).build()
                    .mesActivitiesComponent()
                    .setContext(requireContext())
                    .setDialogChangeMonthListner(null)
                    .setDialogChangeWeekListner(this)
                    .setDialogChangeYearListner(null)
                    .build()
            }
        }
    }

    override fun injectDependencies() {
        component.injectActivities(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(MesActivitiesViewModel::class.java)
        // reset viewModel
        viewModel.resetViewModel()
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnHebdomadaireEvent.observe(viewLifecycleOwner, {
            it?.let {
                if (viewModel.currentFragment.value is MesActivitiesHebdomadaireFragment) {
                    startJournalierFragment()
                } else {
                    dialogChangeWeek.show()
                }
            }
        })
        viewModel.pressBtnMensuelEvent.observe(viewLifecycleOwner, {
            it?.let {
                if (viewModel.currentFragment.value is MesActivitiesMensuelFragment) {
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
                    is MyDailyActivitiesFragment -> {
                        (viewModel.currentFragment.value as MyDailyActivitiesFragment).calendarView.changeToToday()
                    }
                    is MesActivitiesHebdomadaireFragment -> {
                        (viewModel.currentFragment.value as MesActivitiesHebdomadaireFragment).calendarView?.changeToToday()
                    }
                    is MesActivitiesMensuelFragment -> {
                        (viewModel.currentFragment.value as MesActivitiesMensuelFragment).calendarView.changeToToday()
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
        when (activity) {
            is MainTabletteActivity -> {
                (activity as MainTabletteActivity).dismissLoading()
            }
            is MainActivity -> {
                (activity as MainActivity).dismissLoading()
            }
            is MainActivitySpi -> {
                (activity as MainActivitySpi).dismissLoading()
            }
        }
    }

    fun showLoading() {
        when (activity) {
            is MainTabletteActivity -> {
                (activity as MainTabletteActivity).showLoading()
            }
            is MainActivity -> {
                (activity as MainActivity).showLoading()
            }
            is MainActivitySpi -> {
                (activity as MainActivitySpi).showLoading()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }
}
