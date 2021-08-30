package fr.strada.smobile.ui.main

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import fr.strada.smobile.NavGraphDirections
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.access_functionalities.Functionality
import fr.strada.smobile.databinding.ActivityMainBinding
import fr.strada.smobile.di.main.DIALOG_LOADING
import fr.strada.smobile.di.main.MainComponent
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.ui.card.ReaderActivityKotlin
import fr.strada.smobile.ui.spi.MainActivitySpi
import fr.strada.smobile.utils.*
import fr.strada.smobile.utils.ext.hide
import fr.strada.smobile.utils.ext.show
import fr.strada.smobile.utils.listner.OnClickListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.navigation_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */
class MainActivity : BaseActivity(), OnClickListener {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    @Named(DIALOG_LOADING)
    @Inject
    internal lateinit var dialogLoading: Dialog
    lateinit var component: MainComponent
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    @Inject
    internal lateinit var adapter : TypeActivitiePointeuseAdapter

    @Inject
    internal lateinit var layoutManager : RecyclerView.LayoutManager

    lateinit var navHostFragment : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        initComponent()
        injectDepencencices()
        initViewModel()
        bindViewModel()
        initNavHostFragment()
        setupNavigation()
        manageStartButton()
        addDrawerListner()
        setAppropriatedFragment()
        attachAdapterToRecyclerView()
        setLayoutManagerInRecycleView()
        observeTypeActivitiesPointeuse()
        observeUserProfile()
        viewModel.fetchTypeActivitesPointeuseFromServer()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
    }

    private fun initNavHostFragment()
    {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.pointeuseFragment) {
                observeUserFunctionalities()
            } else {
                observeUserFunctionalities(true)
            }
        }
    }

    private fun initComponent() {
        component = (application as SmobileApp).appComponent.mainComponent().setContext(this)
            .setOnClickListener(this).build()
    }

    private fun injectDepencencices() {
        component.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(MainViewModel::class.java)
    }

    private fun bindViewModel() {
        binding.viewModel = viewModel
    }

    /**
     * Setup navigation
     *
     */
    private fun setupNavigation() {
        lifecycleScope.launch {
            val isUserModeBorne = userRepository.isUserModeBorne()
            if(isUserModeBorne){
                dexn_modeborne.visibility = VISIBLE
            }else
            {
                dexn_modeborne.visibility = GONE
            }
        }
        dexn_modeborne.setOnClickListener {
            deconnectionModeBorne()
        }
        menu_home.setOnClickListener {
            lifecycleScope.launch {
                closeDrawer()
                delay(500)
                navigateToHome()
            }
        }
        var openfrais = false
        txt_note_frais.setOnClickListener {

        }

        txt_indemnite_prime.setOnClickListener {
            lifecycleScope.launch {
                closeDrawer()
                delay(500)
                navigateToIndemnite()
            }

        }
        txt_note_frais.setOnClickListener {
            lifecycleScope.launch {
                closeDrawer()
                delay(500)
                navigateToMesFrais()
            }
        }
        menu_frais.setOnClickListener {
            if (openfrais) {
                img_expand_frais.setImageResource(R.drawable.ic_arrow_down)
                menu_frais_expand.visibility = GONE
                openfrais = false
            } else {
                img_expand_frais.setImageResource(R.drawable.ic_arrow_top)
                openfrais = true
                menu_frais_expand.visibility = VISIBLE
            }
        }
        menu_myactivities.setOnClickListener {
            lifecycleScope.launch {
                dialogLoading.show()
                closeDrawer()
                delay(500)
                navigateToMesActivities()
            }
        }
        menu_card_reader.setOnClickListener {
            closeDrawer()
            val intent = Intent(this, ReaderActivityKotlin::class.java).apply {
                putExtra("from", "Mob")
            }
            startActivity(intent)
        }
        menu_profile.setOnClickListener {
            lifecycleScope.launch {
                closeDrawer()
                delay(500)
                navigateToProfile()
            }
        }
        menu_infraction.setOnClickListener {
            lifecycleScope.launch {
                closeDrawer()
                delay(500)
                navigateToInfractions()
            }
        }
        menu_about_us.setOnClickListener {
            lifecycleScope.launch {
                closeDrawer()
                delay(500)
               // navigateToApropos()
                startActivity(Intent(this@MainActivity , MainActivitySpi::class.java))
                finish()
            }
        }
        menu_time_clock.setOnClickListener {
            lifecycleScope.launch {
                closeDrawer()
                delay(500)
                navigateToPointeuse()
            }
        }
        dexn_modeborne.setOnClickListener {

        }
        menu_reglage.setOnClickListener {
            lifecycleScope.launch {
                closeDrawer()
                delay(500)
                navigateToSettings()
            }
        }
        btnStopPlay.setOnClickListener {
            val menuIsVisible = recyclerView.visibility == VISIBLE
            val isChronoStrated = (application as SmobileApp).isChronoStarted.value!!
            if (isChronoStrated && !menuIsVisible) //stop current activity
            {
                (application as SmobileApp).stop(this)

            } else if (!isChronoStrated && menuIsVisible) // hide menu
            {
                hideMenuPointeuse()
                (application as SmobileApp).isChronoStarted.value =
                    (application as SmobileApp).isChronoStarted.value
            } else if (!isChronoStrated && !menuIsVisible) // show menu
            {
                showMenuPointeuse()
                (application as SmobileApp).isChronoStarted.value = (application as SmobileApp).isChronoStarted.value
            }
        }
    }

    private fun showMenuPointeuse() {
        recyclerView.visibility = VISIBLE
    }

    private fun hideMenuPointeuse() {
        recyclerView.visibility = GONE
    }

    /**
     * Manage start button
     *
     */
    private fun manageStartButton() {
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

    /**
     * Set appropriated fragment
     *
     */
    private fun setAppropriatedFragment()
    {
        when (intent.action)
        {
            OPEN_MES_ACTIVITES -> {
                navigateToMesActivities()
            }
            OPEN_POINTEUSE -> {
                navigateToPointeuse(true)
            }
        }
    }

    private fun attachAdapterToRecyclerView() {
        recyclerView.adapter = adapter
    }

    private fun setLayoutManagerInRecycleView() {
        recyclerView.layoutManager = layoutManager
    }

    /**
     * Observe type activities pointeuse
     *
     */
    private fun observeTypeActivitiesPointeuse() {
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

    /**
     * Observe user functionalities
     *
     */

    private fun observeUserFunctionalities(isPointeuseFragment:Boolean = false)
    {
        viewModel.getUserFunctionalities()
        viewModel.userFunctionalities.observe(this,{ resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val userFunctionalities = resource.data!!
                    userFunctionalities.forEach {
                        when(it.code){
                            Functionality.POINTEUSE.code ->{
                                menu_time_clock.show()
                                if(!isPointeuseFragment){
                                    btn_drag.show()
                                }else {
                                    btn_drag.hide()
                                }
                            }
                            Functionality.MES_ACTIVITES.code ->{
                                menu_myactivities.show()
                            }
                            Functionality.LECTURE_CARTE_CONDUCTEUR.code ->{
                                menu_card_reader.show()
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                }
                else -> Timber.e("")
            }
        })
    }

    /**
     * Observe user profile
     *
     */
    private fun observeUserProfile(){
        viewModel.userImage.asLiveData().observe(this) {
            if (it.isNotEmpty()) {
                val decodedBytes: ByteArray = Base64.decode(it, Base64.DEFAULT)
                val decodedByte: Bitmap =
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                img_profile.load(decodedByte) { transformations(CircleCropTransformation()) }
            }
        }
        viewModel.userName.asLiveData().observe(this) {
            txt_name.text = it
        }
    }

    /**
     * Super onback pressed
     *
     */

    fun superOnbackPressed() {
        super.onBackPressed()
    }

    override fun onBackPressed()
    {
        val isDrawerOpen = drawer_layout.isDrawerOpen(GravityCompat.START)
        if (isDrawerOpen) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }
    /**
     * Add drawer listner
     *
     */
    private fun addDrawerListner() {
        val mDrawerToggle = object :
            ActionBarDrawerToggle(this, drawer_layout, null, R.string.ouvrir, R.string.fermer) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                KeyBoardUtils.hideKeyboard(this@MainActivity)
                super.onDrawerSlide(drawerView, slideOffset)
            }
        }
        drawer_layout.addDrawerListener(mDrawerToggle)
    }
    /**
     * Open drawer
     *
     */
    fun openDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    /**
     * Show loading
     *
     */
    fun showLoading() {
        if (!dialogLoading.isShowing) {
            dialogLoading.show()
        }
    }

    /**
     * Dismiss loading
     *
     */
    fun dismissLoading() {
        if (dialogLoading.isShowing) {
            dialogLoading.dismiss()
        }
    }

    override fun onClick(position: Int) {
        lifecycleScope.launch (Dispatchers.Main){
            val typeActivityPointeuse = adapter.items[position].id
            val isActivitieStarted = SmobileApp.instance!!.start(this@MainActivity,typeActivityPointeuse)
            if(isActivitieStarted)
            {
                hideMenuPointeuse()
            }
        }
    }

    private fun navigateToHome() {
        val action = NavGraphDirections.actionGlobalHomeFragment()
        navHostFragment.navController.navigate(action)
    }

    private fun navigateToPointeuse(isMenuTypesActivitieOpned : Boolean = false) {
        val action = NavGraphDirections.actionGlobalPointeuseFragment(isMenuTypesActivitieOpned)
        navHostFragment.navController.navigate(action)
    }

    private fun navigateToIndemnite() {
        navHostFragment.navController.navigate(R.id.action_global_indemnitesFragment)
    }

    private fun navigateToMesFrais() {
        val action = NavGraphDirections.actionGlobalMesFraisFragment()
        navHostFragment.navController.navigate(action)
    }

    private fun navigateToMesActivities() {
        navHostFragment.navController.navigate(R.id.action_global_mesActivitiesFragment)
    }

    private fun navigateToProfile() {
        val action = NavGraphDirections.actionGlobalProfilFragment()
        navHostFragment.navController.navigate(action)
    }

    private fun navigateToApropos() {
        val action = NavGraphDirections.actionGlobalAProposFragment()
        navHostFragment.navController.navigate(action)
    }

    private fun navigateToInfractions() {
        val action = NavGraphDirections.actionGlobalInfractionsFragment()
        navHostFragment.navController.navigate(action)
    }

    private fun navigateToSettings() {
        val action = NavGraphDirections.actionGlobalReglageFragment()
        navHostFragment.navController.navigate(action)
    }
}