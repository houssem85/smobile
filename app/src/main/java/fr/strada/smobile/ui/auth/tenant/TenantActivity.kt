package fr.strada.smobile.ui.auth.tenant

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.data.models.userinfo.Company
import fr.strada.smobile.databinding.ActivityTenantBinding
import fr.strada.smobile.di.auth.TenantActivityComponent
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_tenant.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Tenant activity
 *
 * @constructor Create empty Tenant activity
 */
class TenantActivity : BaseActivity() {

    private lateinit var component: TenantActivityComponent
    private lateinit var viewModel: TenantActivityViewModel
    private lateinit var binding: ActivityTenantBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setupBinding()
        injectDependencies()
        initViewModel()
        bindViewModel()
        initRecycler()
    }
    private fun initRecycler() {
        var selectedItem: String? = null
        var mListName: List<Company>
        val list : ArrayList<Companyitem> = arrayListOf()
            userRepository.getUserFlow().asLiveData().observe(this ,  { userInfo ->

                mListName= userInfo?.companies!!
                list.clear()
                mListName.forEach {
                    if(it.tenants.isNotEmpty()){
                        list.add(Companyitem(it.name,it.tenants[0].namespace))
                    }
                }
                val companyAdapter = CompanyAdapter(list) {
                    list.forEach { companyitem ->
                        companyitem.isSelected = false
                    }
                    list[it].isSelected = true
                    selectedItem = list[it].tenant
                    binding.companyRecycler.adapter?.notifyDataSetChanged()
                }
                binding.companyRecycler.apply {
                    layoutManager = LinearLayoutManager(this@TenantActivity)
                    adapter = companyAdapter
                }
                btn_auth_tenant.setOnClickListener {
                    if (selectedItem != null){
                        lifecycleScope.launch {
                            userRepository.savetenant(selectedItem!!)
                            checkUserSession()
                        }
                    }
                }
            })
    }

    private fun initComponent() {
        component = (application as SmobileApp).appComponent.tenantComponent().setContext(this).build()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tenant)
        binding.lifecycleOwner = this
    }

    private fun injectDependencies() {
        component.inject(this)
    }

    /**
     * Init view model
     *
     */
    fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(TenantActivityViewModel::class.java)
    }

    /**
     * Bind view model
     *
     */
    fun bindViewModel() {
        binding.viewModel = viewModel
    }
}