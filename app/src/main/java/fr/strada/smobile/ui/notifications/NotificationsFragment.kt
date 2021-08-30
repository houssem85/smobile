package fr.strada.smobile.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.FragmentNotificationsBinding
import fr.strada.smobile.di.notifications.NotificationsComponent
import fr.strada.smobile.ui.activities.Utils
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui_tablette.main_tablette.MainTabletteActivity
import fr.strada.smobile.utils.listner.ItemNotificationListener
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_notifications.*
import javax.inject.Inject


/**
 * Notifications fragment
 *
 * @constructor Create empty Notifications fragment
 */
class NotificationsFragment : BaseFragment(), ItemNotificationListener {

    private lateinit var component: NotificationsComponent
    private lateinit var viewModel: NotificationsViewModel
    private lateinit var binding: FragmentNotificationsBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    internal lateinit var adapter: NotificationsAdapter

    internal lateinit var layoutManager: LinearLayoutManager

    /**
     * On create view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    /**
     * On view created
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // startAnimation()
        attachAdapterInRecycleView()
        setLayoutManagerInRecycleView()
        observeNotifications()
        if (savedInstanceState == null) {
            viewModel.getNotifications()
        }
        if (activity is MainTabletteActivity) {
            setupContainer()
        }

    }

    /**
     * Companion
     *
     * @constructor Create empty Companion
     */
    companion object {
        @JvmStatic
        fun newInstance() = NotificationsFragment()
    }

    /**
     * Init component
     *
     */
    override fun initComponent() {
        component = SmobileApp.instance!!.appComponent.notificationsComponent()
            .setContext(requireContext()).setOnClickListner(this).build()

    }

    /**
     * Inject dependencies
     *
     */
    override fun injectDependencies() {
        component.inject(this)
    }

    /**
     * Init view model
     *
     */
    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(NotificationsViewModel::class.java)
    }

    /**
     * Bind view model
     *
     */
    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    /**
     * Setup navigation
     *
     */
    override fun setupNavigation() {
        viewModel.pressBtnOpenMenuEvent.observe(this, {
            (activity as MainActivity).openDrawer()
        })
        viewModel.pressBtnReglageEvent.observe(this, {
            if (activity is MainActivity) {
                // (activity as MainActivity).setFirstFragment(ReglageFragment())
            } else {
                //(activity as MainTabletteActivity).setFirstFragment(ReglageTabletteFragment())
                // (activity as MainTabletteActivity).setMenuItemActive(4)

            }
        })
    }

    /**
     * Start animation
     *
     */
    private fun startAnimation() {
        val formRightAnimation = Utils.inFromRightAnimation()
        val formBottomAnimation = Utils.inFromBottomAnimation()
        view_top_notifications.startAnimation(formRightAnimation)
        recycle_notifications.startAnimation(formBottomAnimation)
    }

    /**
     * Attach adapter in recycle view
     *
     */
    private fun attachAdapterInRecycleView() {
        adapter = NotificationsAdapter(requireContext(),this)
        recycle_notifications.adapter = adapter
    }

    /**
     * Set layout manager in recycle view
     *
     */
    private fun setLayoutManagerInRecycleView() {
        layoutManager = LinearLayoutManager(requireContext())
        recycle_notifications.layoutManager = layoutManager
    }

    /**
     * Observe notifications
     *
     */
    private fun observeNotifications() {
        viewModel.notifications.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }

    /**
     * On press btn delete listener
     *
     * @param position
     */
    override fun OnPressBtnDeleteListener(position: Int) {
        adapter.items.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    /**
     * On click listner
     *
     * @param position
     */
    override fun onClickListner(position: Int) {
    }

    /**
     * Setup container
     *
     */
    private fun setupContainer() // Fix widht container
    {
        val widthScreen =
            fr.strada.smobile.ui_tablette.accueil.Utils.getWidthScreen(requireActivity())
        val widthClosedSideMenu =
            fr.strada.smobile.ui_tablette.accueil.Utils.fromDpToPixels(requireActivity(), 70)
        val layoutParams: ViewGroup.LayoutParams = frame_notification.layoutParams
        layoutParams.width = widthScreen - widthClosedSideMenu
        frame_notification.layoutParams = layoutParams
    }


}