package fr.strada.smobile.ui_tablette.main_tablette

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.strada.sidemenuview.utils.MenuItemListner
import fr.strada.sidemenuview.utils.SubMenuItemListner
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.access_functionalities.Functionality
import fr.strada.smobile.databinding.ActivityMainTabletteBinding
import fr.strada.smobile.di.main.DIALOG_LOADING
import fr.strada.smobile.di_tablette.main_tablette.MainTabletteComponent
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.ui.main.TypeActivitiePointeuseAdapter
import fr.strada.smobile.ui_tablette.accueil.AccueilTabletteFragment
import fr.strada.smobile.ui_tablette.mes_activities_tablette.MesActivitiesTabletteFragment
import fr.strada.smobile.ui_tablette.pointeuse.PointeuseTabletteFragment
import fr.strada.smobile.utils.OPEN_ACCUEIL
import fr.strada.smobile.utils.OPEN_MES_ACTIVITES
import fr.strada.smobile.utils.OPEN_POINTEUSE
import fr.strada.smobile.utils.Status
import fr.strada.smobile.utils.ext.hide
import fr.strada.smobile.utils.ext.show
import fr.strada.smobile.utils.listner.OnClickListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main_tablette.*
import kotlinx.android.synthetic.main.navigation_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


class  MainTabletteActivity : BaseActivity(), MenuItemListner, SubMenuItemListner , OnClickListener {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var component: MainTabletteComponent
    lateinit var binding: ActivityMainTabletteBinding
    lateinit var viewModel: MainTabletteViewModel

    @Named(DIALOG_LOADING)
    @Inject
    internal lateinit var dialogLoading: Dialog

    @Inject
    internal lateinit var adapter : TypeActivitiePointeuseAdapter

    lateinit var layoutManager : RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SmobileApp.instance!!.initAttributesPointeuse()
        SmobileApp.instance!!.refreshDataWidget()
        SmobileApp.instance!!.startShortCutService()
        setupBinding()
        initComponent()
        injectDepencencices()
        initViewModel()
        bindViewModel()
        setupNavigation()
        observeAttributesPointeuse()
        attachAdapterToRecyclerView()
        setLayoutManagerInRecycleView()
        observeTypeActivitiesPointeuse()
        setUpMenuAccordingToUserFonctionalities(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.fetchTypeActivitesPointeuseFromServer()
        }
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_tablette)
        binding.lifecycleOwner = this
    }

    private fun initComponent() {
        component = (application as SmobileApp).appComponent.mainTabletteComponent().setContext(this).setOnClickListener(
            this
        ).build()
    }

    private fun injectDepencencices() {
        component.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(MainTabletteViewModel::class.java)
    }

    private fun bindViewModel() {
        binding.viewModel = viewModel
    }

    private fun setUpMenuAccordingToUserFonctionalities(savedInstanceState: Bundle?)
    {
        lifecycleScope.launch {
            if(savedInstanceState == null)
            {
                val userFunctionalities = viewModel.getUserFunctionalities()
                if(userFunctionalities.status == Status.SUCCESS){
                    viewModel.menuItems = generateMenuItemFromUserFunctionalities(this@MainTabletteActivity,userFunctionalities.data!!)
                }
            }
            sideMenuView.setMenuItems(viewModel.menuItems)
            sideMenuView.setMenuItemListner(this@MainTabletteActivity)
            sideMenuView.setSubMenuItemListner(this@MainTabletteActivity)
            if(savedInstanceState == null && viewModel.menuItems.isNotEmpty())
            {
                startAppropriatedFragment()
            }
        }
    }

    private fun startAppropriatedFragment()
    {
        val fragment = when(intent.action)
        {
            OPEN_ACCUEIL -> {
                val indexOfItemMenu = viewModel.menuItems.indexOfFirst {
                    it.fragment is AccueilTabletteFragment
                }
                if(indexOfItemMenu != -1)
                {
                    sideMenuView.setMenuItemActive(indexOfItemMenu)
                    AccueilTabletteFragment()
                }else{
                    null
                }
            }
            OPEN_POINTEUSE -> {
                val indexOfItemMenu = viewModel.menuItems.indexOfFirst {
                    it.fragment is PointeuseTabletteFragment
                }
                if(indexOfItemMenu != -1)
                {
                    sideMenuView.setMenuItemActive(indexOfItemMenu)
                    PointeuseTabletteFragment.factory(true)
                }else{
                    null
                }
            }
            OPEN_MES_ACTIVITES -> {
                val indexOfItemMenu = viewModel.menuItems.indexOfFirst {
                    it.fragment is MesActivitiesTabletteFragment
                }
                if(indexOfItemMenu != -1)
                {
                    sideMenuView.setMenuItemActive(indexOfItemMenu)
                    MesActivitiesTabletteFragment()
                }else{
                    null
                }
            }
            else -> {
                AccueilTabletteFragment()
            }
        }
        fragment?.let {
            changeFragmentAndShowLoader(fragment)
        }
    }

    private fun setRequestedOrientationUnspecified()
    {
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    fun changeFragmentAndShowLoader(newFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container, newFragment).commit()
        setRequestedOrientationUnspecified()
        if(newFragment is MesActivitiesTabletteFragment){
            showLoading()
        }
        lifecycleScope.launch {
            val res = viewModel.getUserFunctionalities()
            if(res.status == Status.SUCCESS){
                val isAuthorised = res.data!!.any { it.code == Functionality.POINTEUSE.code}
                val isPointeuseFragment = newFragment is PointeuseTabletteFragment
                if(!isAuthorised){
                    btn_drag.hide()
                }else{
                    if(isPointeuseFragment){
                        btn_drag.hide()
                    }else{
                        btn_drag.show()
                    }
                }
            }
        }
    }

    fun addFragment(newFragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().add(R.id.container, newFragment, "newFragment")
            .addToBackStack(null).commit()
    }

    private fun setupNavigation() {
        btnStopPlay.setOnClickListener {
            val menuIsVisible = recyclerView.visibility == VISIBLE
            val isChronoStrated = (application as SmobileApp).isChronoStarted.value!!
            if(isChronoStrated && !menuIsVisible) //stop current activity
            {
                (application as SmobileApp).stop(this)

            }else if(!isChronoStrated && menuIsVisible) // hide menu
            {
                hideMenuPointeuse()
                (application as SmobileApp).isChronoStarted.value = (application as SmobileApp).isChronoStarted.value
            } else if(!isChronoStrated && !menuIsVisible) // show menu
            {
                showMenuPointeuse()
                (application as SmobileApp).isChronoStarted.value = (application as SmobileApp).isChronoStarted.value
            }
        }
    }
    private fun showMenuPointeuse()
    {
        recyclerView.visibility = VISIBLE
    }

    private fun hideMenuPointeuse()
    {
        recyclerView.visibility = View.GONE
    }

    //---------------------- Side Menu View Listner --------------------------//

    override fun onClickMenuItem(position:Int, fragment: Fragment?,activity: Activity?) {
        fragment?.let {
            changeFragmentAndShowLoader(fragment)
        }
        activity?.let {
            val intent = Intent(this, activity::class.java).apply {
                putExtra("from", "tablett")
            }
            startActivity(intent)
        }
        sideMenuView.closeDrawer()
    }

    override fun onClickMenuItemWithSubMenu() {
        sideMenuView.openDrawer()
    }

    override fun onClickSubMenuItem(indexItemMenu:Int,fragment: Fragment) {
        changeFragmentAndShowLoader(fragment)
        sideMenuView.closeDrawer()
        sideMenuView.setMenuItemActive(indexItemMenu)
    }

    private fun observeAttributesPointeuse()
    {
        SmobileApp.instance!!.isChronoStarted.observe(this, {
            if (!it) {
                val menuIsVisible = recyclerView.visibility == VISIBLE
                if (!menuIsVisible) {
                    imgStopPlay.setImageResource(R.drawable.ic_play_pointeuse)
                    circleView.circleColor = Color.parseColor("#EAEAEA")
                } else {
                    imgStopPlay.setImageResource(R.drawable.ic_stop_pointeuse)
                    circleView.circleColor = Color.parseColor("#385081")
                }
            } else {
                imgStopPlay.setImageResource(R.drawable.ic_stop_pointeuse)
                val activitiePointeuseColor =
                    (application as SmobileApp).codeCouleurActivitiePointeuseStarted
                try {
                    circleView.circleColor = Color.parseColor(activitiePointeuseColor.take(7))
                } catch (ex: Exception) {
                    circleView.circleColor = Color.parseColor("#385081")
                }
                hideMenuPointeuse()
            }
        })
        btn_drag.draggableSetup()
    }

    fun showLoading()
    {
        if(!dialogLoading.isShowing)
        {
            dialogLoading.show()
        }
    }

    fun dismissLoading()
    {
        if(dialogLoading.isShowing)
        {
            dialogLoading.dismiss()
        }
    }

    override fun onClick(position: Int)
    {
        lifecycleScope.launch (Dispatchers.Main){
            val typeActivityPointeuse = adapter.items[position].id
            val isActivitieStarted = SmobileApp.instance!!.start(this@MainTabletteActivity,typeActivityPointeuse)
            if(isActivitieStarted)
            {
                hideMenuPointeuse()
            }
        }
    }

    private fun attachAdapterToRecyclerView()
    {
        recyclerView.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView()
    {
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
    }

    private fun observeTypeActivitiesPointeuse()
    {
        viewModel.responseGetTypeActivitePointeuse.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
                    adapter.items = it.data!!
                    adapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}