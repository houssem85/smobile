package fr.strada.smobile.ui.auth.borne

import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.ActivityAuthBorneBinding
import fr.strada.smobile.di.auth.AuthBorneComponent
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.listUserBorn
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_auth_borne.*
import javax.inject.Inject

class AuthBorne :BaseActivity() {
    private lateinit var component: AuthBorneComponent
    private lateinit var viewModel: AuthBorneViewModel
    private lateinit var binding: ActivityAuthBorneBinding

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        setupBinding()
        injectDependencies()
        initViewModel()
        bindViewModel()
        initAutoComplete()
        /*
        imageView11logoborne.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Mode Borne ")
            builder.setMessage("Vous allez ètre déconnecter du Mode Borne ")
            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                lifecycleScope.launch {
                    credentialsManager.clearCredentials()
                    refreshSession()
                }
            }
            builder.show()

        }
        viewModel.pressBtnSeconnecterEvent.observe(this, {
            val tabletSize = resources.getBoolean(R.bool.isTablet)
            if (!isEmpty()) {
                val name: String = input_nom_authmodeborne.text.toString()
                val code: String = inpute_code_modeborne.text.toString()
                for (t in listUserBorn) {
                    Timber.tag("CONNECT").e("the1")
                    if (t.nomBornUser == name && code == t.codeBorneUser) {
                        Timber.tag("CONNECT").e("the2")
                        cardStorage.setModeBorneLogin(true)
                        if (tabletSize)
                        {
                            val intent = Intent(this, MainTabletteActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.action = OPEN_ACCUEIL
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.action = OPEN_ACCUEIL
                            startActivity(intent)
                            finish()
                        }

                    }
                }
            }
        })
         */
    }

    private fun initAutoComplete(){
        val list : ArrayList<String> = ArrayList()
        for (s in listUserBorn){
            list.add(s.nomBornUser)
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line, list
        )
        input_nom_authmodeborne.setAdapter(adapter)
    }
    private fun initComponent()
    {
        component = (application as SmobileApp).appComponent.authBorneComponent().setContext(this).build()
    }

    private fun setupBinding()
    {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth_borne)
        binding.lifecycleOwner = this    }


    private fun injectDependencies()
    {
        component.inject(this)
    }

     fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(AuthBorneViewModel::class.java)
    }

     fun bindViewModel() {
        binding.viewModel = viewModel
    }

    private fun isEmpty(): Boolean {
        var empty = false
        if (TextUtils.isEmpty(input_nom_authmodeborne.text)) {
            empty = true
            input_nom_authmodeborne.error = "Champ Vide"
        }
        if (TextUtils.isEmpty(inpute_code_modeborne.text)) {
            empty = true
            inpute_code_modeborne.error = "Champ Vide"
        }
        return empty
    }


}