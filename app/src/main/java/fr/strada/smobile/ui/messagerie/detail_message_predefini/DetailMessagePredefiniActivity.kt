package fr.strada.smobile.ui.messagerie.detail_message_predefini

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.ActivityDetailMessagePredefiniBinding
import fr.strada.smobile.di.messagerie.detailMessagePredefini.DetailMessagePredefiniComponent
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import javax.inject.Inject

class DetailMessagePredefiniActivity : BaseActivity() {

    private lateinit var component: DetailMessagePredefiniComponent
    private lateinit var viewModel: DetailMessagePredefiniViewModel
    private lateinit var binding: ActivityDetailMessagePredefiniBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        initComponent()
        injectDependencies()
        initViewModel()
        bindViewModel()
        setupNavigation()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_message_predefini)
        binding.lifecycleOwner = this
    }

    fun initComponent() {
        component = (application as SmobileApp).appComponent.detailMessagePredefiniComponent()
            .setContext(this)
            .build()
    }

    fun injectDependencies() {
        component.inject(this)
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            providerFactory
        ).get(DetailMessagePredefiniViewModel::class.java)
    }

    fun bindViewModel() {
        binding.viewModel = viewModel
        viewModel.message.value = intent.getStringExtra("messagePredefini")
        viewModel.objet.value = intent.getStringExtra("objet")
    }

    fun setupNavigation() {

        viewModel.pressBtnBackEvent.observe(this, {
            super.onBackPressed()
        })
    }


}