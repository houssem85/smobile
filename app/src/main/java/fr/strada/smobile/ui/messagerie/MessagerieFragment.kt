package fr.strada.smobile.ui.messagerie

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import fr.strada.smobile.R
import fr.strada.smobile.databinding.FragmentMessagerieBinding
import fr.strada.smobile.di.messagerie.MessagerieComponent
import fr.strada.smobile.ui.activities.Utils.inFromRightAnimation
import fr.strada.smobile.ui.base.BaseFragment
import fr.strada.smobile.ui.gerer_absence.TabFragmentAdapter
import fr.strada.smobile.ui.main.MainActivity
import fr.strada.smobile.ui.messagerie.Utils.slideView
import fr.strada.smobile.ui.messagerie.boite_de_reception.BoiteReceptionFragment
import fr.strada.smobile.ui.messagerie.message_predefini.MessagePredefiniFragment
import fr.strada.smobile.utils.Toggle
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_absence_request.*
import kotlinx.android.synthetic.main.fragment_messagerie.*
import kotlinx.android.synthetic.main.fragment_messagerie.toolBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class MessagerieFragment : BaseFragment() {

    private lateinit var component: MessagerieComponent
    private lateinit var viewModel: MessagerieViewModel
    private lateinit var binding: FragmentMessagerieBinding
    lateinit var tabFragmentAdapter: TabFragmentAdapter


    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messagerie, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
        initTabFragmentAdapter()
        setupViewPager()
        setAdapterInViewPager()
        associateViewPagerWithTabLayout()
    }


    override fun initComponent() {
        component = (activity as MainActivity).component.messagerieComponent()
            .setContext(requireContext())
            .build()
    }

    override fun injectDependencies() {
        component.inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(MessagerieViewModel::class.java)
    }

    override fun bindViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupNavigation() {
        viewModel.pressBtnOpenMenuEvent.observe(this, {
            (activity as MainActivity).openDrawer()
        })

        viewModel.pressBtnFiltreMessagesEvent.observe(this, {
            Toggle.toggleLayout(menu_filtre_expand.visibility== View.GONE, img_expand_filtre, menu_filtre_expand)
        })

        viewModel.pressBtnAddMessageEvent.observe(this, {

           //  (activity as MainActivity).addFragment(NouveauMessageFragment())
        })

        viewModel.pressTxtFiltreTousEvent.observe(this, {
            edit_filtre_message.setText(txt_tous.text)
            Toggle.toggleLayout(false, img_expand_filtre, menu_filtre_expand)
        })

        viewModel.pressTxtFiltreMessageLuEvent.observe(this, {
            edit_filtre_message.setText(txt_message_lu.text)
            Toggle.toggleLayout(false, img_expand_filtre, menu_filtre_expand)
        })

        viewModel.pressTxtFiltreMessageNonLuEvent.observe(this, {
            edit_filtre_message.setText(txt_message_non_lu.text)
            Toggle.toggleLayout(false, img_expand_filtre, menu_filtre_expand)
        })
    }

    private fun startAnimation() {
        val formRightAnimation = inFromRightAnimation()
        view_filtre_messagerie.startAnimation(formRightAnimation)
        btn_add_message.startAnimation(formRightAnimation)
    }

    private fun initTabFragmentAdapter() {
        tabFragmentAdapter = TabFragmentAdapter(childFragmentManager)
        tabFragmentAdapter.addFragment(
            BoiteReceptionFragment(),
            resources.getString(R.string.boite_de_reception)
        )
        tabFragmentAdapter.addFragment(
            MessagePredefiniFragment(),
            resources.getString(R.string.message_predefini)
        )
    }

    private fun setupViewPager() {
        viewPager.offscreenPageLimit = 2
    }

    private fun setAdapterInViewPager() {
        viewPager.adapter = tabFragmentAdapter
    }

    private fun associateViewPagerWithTabLayout() {
        tabLayout_messagerie.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val display = activity?.windowManager?.defaultDisplay
                val size = Point()
                display?.getSize(size)
                val width: Int = size.x
                val x1 = width * 1
                val x2 = 0
                val currentHeight = 135 * context?.resources?.displayMetrics?.density!!
                val newHeight = 65 * context?.resources?.displayMetrics?.density!!

                if (position == 0) {

                    GlobalScope.launch(Dispatchers.Main) {
                        slideView(toolBar, newHeight.toInt(), currentHeight.toInt())
                        this@MessagerieFragment.view_filtre_messagerie.visibility = View.VISIBLE
                        this@MessagerieFragment.view_filtre_messagerie.animate().translationX(-x2.toFloat()).duration = 400
                        delay(3000)
                    }

                } else {
                    GlobalScope.launch(Dispatchers.Main) {
                        slideView(toolBar, currentHeight.toInt(), newHeight.toInt())
                        this@MessagerieFragment.view_filtre_messagerie.animate().translationX(x1.toFloat()).duration = 400
                        delay(3000)
                    }
                }

            }
        })
    }


}