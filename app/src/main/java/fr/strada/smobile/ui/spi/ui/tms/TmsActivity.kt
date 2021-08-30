package fr.strada.smobile.ui.spi.ui.tms

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import javax.inject.Inject

class TmsActivity : BaseActivity() {

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: TmsActivityViewModel


    private fun injectDepencencices() {
        SmobileApp.instance!!.appComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(TmsActivityViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tms)
        injectDepencencices()
        initViewModel()
    }


}