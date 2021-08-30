package fr.strada.smobile.ui.pointeuse

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.work.WorkInfo
import androidx.work.WorkManager
import coil.load
import dagger.android.support.DaggerFragment
import fr.strada.smobile.BuildConfig
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.pointeuse.JourActivite
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.pointeuse.adapter.JourActiviteAdapter
import fr.strada.smobile.ui.pointeuse.dialog_type_activitie_pointeuse.DialogSelectTypeActivitiePointeuse
import fr.strada.smobile.ui.pointeuse.jour_activitie.JourActivitieActivity
import fr.strada.smobile.ui.pointeuse_graph.PointeuseGraphActivity
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.ui_tablette.pointeuse.PointeuseTabletteFragment
import fr.strada.smobile.utils.Animations.animateGuideLine
import fr.strada.smobile.utils.IS_MENU_TYPE_ACTIVITIE_POINTEUSE_OPNED
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.ext.hide
import fr.strada.smobile.utils.ext.show
import fr.strada.smobile.utils.listner.OnClickListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main_spi.*
import kotlinx.android.synthetic.main.fragment_time_clock.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class PointeuseFragment : DaggerFragment(), OnClickListener {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: PointeuseViewModel
    private lateinit var adapter: JourActiviteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_clock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupNavigation()
        openMenuTypeActivitiePointeuseIfEssential()
        setUpRecyclerActivitiesPointeuse()
        observePointeuseAttributes()
        observeJourActivities()
        if (activity is MainActivitySpi)
        {
            btnOpenMenu?.visibility = View.INVISIBLE
            (activity as MainActivitySpi).imageprofile_spi_menu.visibility = View.INVISIBLE
            (activity as MainActivitySpi).image_back_main.visibility = View.VISIBLE

        }

        if(savedInstanceState == null){
            val currentJour = getCurrentDate()
            viewModel.getJourActivities(currentJour)
        }
        observePointeuseWorker()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(PointeuseViewModel::class.java)
    }

    private fun setupNavigation() {
        val isTableteSize = resources.getBoolean(R.bool.isTablet)
        if(!isTableteSize){
            if (activity is MainActivity)
            btnOpenMenu?.setOnClickListener {
                (activity as MainActivity).openDrawer()
            }
        }
        btnOpenViewGraphic?.setOnClickListener {
            startActivity(Intent(activity, PointeuseGraphActivity::class.java))
        }
        btn_start.setOnClickListener {
            val isChronoStrated = (requireActivity().application as SmobileApp).isChronoStarted.value!!
            if (isChronoStrated) // stop current activity
            {
                SmobileApp.instance!!.stop(requireContext())
            } else
            {
                showDialogSelectTypeActivitiepointeuse()
            }
        }
    }

    private fun setUpRecyclerActivitiesPointeuse()
    {
        adapter = JourActiviteAdapter(requireContext(), this::onClickItem)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recycler_activities.adapter = adapter
        recycler_activities.layoutManager = linearLayoutManager
        recycler_activities.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(adapter.items.isNotEmpty()){
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.items.size - 1 && newState == SCROLL_STATE_IDLE) {
                        val lastDate = adapter.items[linearLayoutManager.findLastCompletelyVisibleItemPosition()].jourActivite!!.take(10)
                        val date = decrementeDateByOne(lastDate)
                        viewModel.getJourActivities(date)
                    }
                }
            }
        })
    }

    private fun onClickItem(jourActivite: JourActivite) {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val intent = Intent(requireContext(), JourActivitieActivity::class.java)
            intent.putExtra(EXTRA_JOUR_ACTIVITE, jourActivite)
            startActivity(intent)
        }else
        {
            (parentFragment as PointeuseTabletteFragment).setJourActivitieFragment(jourActivite)
        }
    }

    private fun observeJourActivities(){
        viewModel.jourActivities.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    val data = resource.data!!
                    adapter.setData(data)
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun openMenuTypeActivitiePointeuseIfEssential() {
        arguments?.let {
            if (it.containsKey(IS_MENU_TYPE_ACTIVITIE_POINTEUSE_OPNED)) {
                val isMenuTypeActivitiePointeuseOpned =
                    it.getBoolean(IS_MENU_TYPE_ACTIVITIE_POINTEUSE_OPNED)
                if (isMenuTypeActivitiePointeuseOpned) {
                    showDialogSelectTypeActivitiepointeuse()
                }
            }
        }
    }

    private fun observePointeuseWorker()
    {
        val workManager = WorkManager.getInstance(requireContext())
        val workInfos = workManager.getWorkInfosByTagLiveData(PointeuseWorker.TAG)
        workInfos.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it != null){
                if(it.isNotEmpty())
                {
                    val workInfo = it.first()
                    when(workInfo.state)
                    {
                        WorkInfo.State.ENQUEUED -> {
                            showMessageNoInternetConnection()
                        }
                        WorkInfo.State.RUNNING -> {
                            lifecycleScope.launch(Dispatchers.Main){
                                val currentJour = getCurrentDate()
                                viewModel.getJourActivities(currentJour)
                                hideMessageNoInternetConnection()
                                showMessageConnectionRetablie()
                                delay(3000)
                                hideMessageConnectionRetablie()
                            }
                        }
                        else -> {

                        }
                    }
                }
            }
        })
    }

    private fun showMessageNoInternetConnection()
    {
        animateGuideLine(no_internet_guide_line,1.5f,0.98f)
    }

    private fun hideMessageNoInternetConnection()
    {
        animateGuideLine(no_internet_guide_line,0.98f,1.5f)
    }

    private fun showMessageConnectionRetablie()
    {
        animateGuideLine(sucess_guide_line,1.5f,0.98f)
    }

    private fun hideMessageConnectionRetablie()
    {
        animateGuideLine(sucess_guide_line,0.98f,1.5f)
    }

    private fun observePointeuseAttributes()
    {
        SmobileApp.instance!!.duration.observe(viewLifecycleOwner,{
            val hours: Int = it / 3600
            val minutes: Int = (it / 60) % 60
            val seconds: Int = it % 60
            txt_timeclock_chrono.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        })
        SmobileApp.instance!!.isChronoStarted.observe(viewLifecycleOwner, {
            val color = SmobileApp.instance!!.codeCouleurActivitiePointeuseStarted
            val icon = SmobileApp.instance!!.iconActivitiePointeuseStarted
            if (!it) {
                imgStopPlayTime.setImageResource(R.drawable.ic_play_blue)
                round_corner_layout?.backgroundColor = Color.parseColor("#FFFFFF")
                img_type_activitie.hide()
            } else {
                imgStopPlayTime.setImageResource(R.drawable.ic_stop_blue)
                round_corner_layout?.backgroundColor = Color.parseColor(color.take(7))
                img_type_activitie.show()
                img_type_activitie.load(BuildConfig.URL_BASE_TIME + icon)
            }
        })
    }

    private val refreshListActivitiesPointeuseReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val currentJour = getCurrentDate()
            viewModel.getJourActivities(currentJour)
        }
    }

    private fun registerRefreshListActivitiesPointeuseReceiver() {
        val filter = IntentFilter("refreshListActivitiesPointeuseReceiver")
        activity?.registerReceiver(refreshListActivitiesPointeuseReceiver, filter)
    }

    private fun unregisterRefreshListActivitiesPointeuseReceiver() {
        activity?.unregisterReceiver(refreshListActivitiesPointeuseReceiver)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        registerRefreshListActivitiesPointeuseReceiver()
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterRefreshListActivitiesPointeuseReceiver()
    }

    companion object {

        fun factory(isMenuActivitiePointeuseOpned: Boolean = false): PointeuseFragment {
            val fragment = PointeuseFragment()
            val args = Bundle()
            args.putBoolean(IS_MENU_TYPE_ACTIVITIE_POINTEUSE_OPNED, isMenuActivitiePointeuseOpned)
            fragment.arguments = args
            return fragment
        }
        const val EXTRA_JOUR_ACTIVITE = "EXTRA_JOUR_ACTIVITE"
    }

    override fun onClick(position: Int) {

    }

    private fun showDialogSelectTypeActivitiepointeuse()
    {
        val dialog = DialogSelectTypeActivitiePointeuse.newInstance()
        dialog.onSubmitListner = {
            lifecycleScope.launch (Dispatchers.Main){
                SmobileApp.instance!!.start(requireContext(),it)
            }
        }
        dialog.show(childFragmentManager,DialogSelectTypeActivitiePointeuse.TAG)
    }

    //---------------------------------manage loader---------------------------------//
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
}

