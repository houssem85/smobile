package fr.strada.smobile.ui.auth.auth0


import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.Auth0Binding
import fr.strada.smobile.di.auth.AuthComponent
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.ext.show
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.auth0.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthActivity : BaseActivity() {

    val EMAIL_REGEX = "[a-zA-Z0-9-.!#$%&'*+–/=?^_`{|}~]{1,64}@[a-z0-9-.]{1,255}"
    private lateinit var component: AuthComponent
    private lateinit var binding: Auth0Binding
    lateinit var viewModel: Auth0ViewModel
    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth0)
        initComponent()
        injectDependencies()
        initViewModel()
        setupBinding()
        bindViewModel()
        observeMsgError()
        observeCredentials()
        observeUserInfo()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.auth0)
        binding.lifecycleOwner = this
    }

    private fun initComponent() {
        component = (application as SmobileApp).appComponent.authComponent()
            .setContext(this)
            .build()
    }

    private fun injectDependencies() {
        component.inject(this)
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this, providerFactory).get(Auth0ViewModel::class.java)
    }

    fun bindViewModel() {
        binding.viewModel = viewModel
    }

    private fun observeCredentials()
    {
        viewModel.credentials.observe(this,{
            viewModel.saveCredentials(it)
        })
    }

    private fun observeUserInfo(){
        viewModel.userInfo.observe(this,{
            lifecycleScope.launch {
                val email = it.email
                viewModel.saveUser(it)
                if(email == "houssemeddine.daoud@addinn.com"){ // mode borne
                    viewModel.isModeBorne(true)
                }else {
                    viewModel.isModeBorne(false)
                }
                loader.visibility = INVISIBLE
                checkUserSession()
            }
        })
    }

    private fun observeMsgError()
    {
        viewModel.msgError.observe(this,{
            loader.visibility = INVISIBLE
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        })
    }

    fun pressBtnIdentifier(view : View) {
        val emailInput = input_nom_email.text.toString()
        val passwordInput = inpute_password.text.toString()
        val isEmailNotEmpty = emailInput.isNotEmpty()
        val isEmailValide = emailInput.matches(EMAIL_REGEX.toRegex())
        val isPasswordNotEmpty = passwordInput.isNotEmpty()
        val isFormValide = isEmailNotEmpty && isEmailValide && isPasswordNotEmpty
        if (isFormValide) {
            viewModel.loginWithEmail(emailInput,passwordInput)
            loader.show()
        } else {
            if (!isEmailNotEmpty) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.entrer_votre_Email),
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            if (!isEmailValide) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.format_email_incorrecte),
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            if (!isPasswordNotEmpty) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.entrer_votre_mot_de_passe),
                    Toast.LENGTH_LONG
                ).show()
                return
            }
        }
    }
}
