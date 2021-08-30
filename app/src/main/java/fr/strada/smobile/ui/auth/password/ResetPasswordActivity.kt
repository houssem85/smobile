package fr.strada.smobile.ui.auth.password

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.lock.views.ValidatedInputView.EMAIL_REGEX
import fr.strada.smobile.BuildConfig
import fr.strada.smobile.R
import fr.strada.smobile.SmobileApp
import fr.strada.smobile.databinding.ActivityResetPasswordAuth0Binding
import fr.strada.smobile.di.auth.ResetPasswordComponent
import fr.strada.smobile.ui.auth.auth0.AuthActivity
import fr.strada.smobile.ui.base.BaseActivity
import fr.strada.smobile.utils.OPEN_ACCUEIL
import fr.strada.smobile.utils.multi_binding.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_reset_password_auth0.*
import timber.log.Timber
import javax.inject.Inject

class ResetPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityResetPasswordAuth0Binding
    private lateinit var component: ResetPasswordComponent
    lateinit var viewModel: ResetPasswordViewModel
    lateinit var auth0: Auth0
    lateinit var authenticationAPIClient: AuthenticationAPIClient

    @Inject
    internal lateinit var providerFactory: ViewModelProviderFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_auth0)
        initComponent()
        injectDependencies()
        initViewModel()
        setupBinding()
        initAuth0()
        initAuthenticationAPIClient()
        bindViewModel()
        viewModel.pressBtnContinuerEvent.observe(this, {
            pressBtnContinuer()
        })

    }
    private fun initAuth0() {
        auth0 = Auth0(
            BuildConfig.AUTH0_CLIENT_ID, BuildConfig.AUTH0_DOMAIN
        )
        auth0.let {
            it.isOIDCConformant = true
            it.isLoggingEnabled = true
        }
    }
    private fun initAuthenticationAPIClient() {
        authenticationAPIClient = AuthenticationAPIClient(auth0)
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password_auth0)
        binding.lifecycleOwner = this
    }
    private fun injectDependencies() {
        component.inject(this)
    }
    private fun initComponent() {
        component =
            (application as SmobileApp).appComponent.resetPasswordComponent().setContext(this)
                .build()
    }

    fun initViewModel() {
        viewModel =
            ViewModelProvider(this, providerFactory).get(ResetPasswordViewModel::class.java)
    }

    fun bindViewModel() {
        binding.viewModel = viewModel
    }

    fun pressBtnContinuer() {

            val emailInput = input_nom_email_reset.text.toString()
            val isEmailNotEmpty = emailInput.isNotEmpty()
            val isEmailValide = emailInput.matches(EMAIL_REGEX.toRegex())
            val isFormValide = isEmailNotEmpty && isEmailValide
            if (!isEmpty()) {
                if (isFormValide) {
                    val request = authenticationAPIClient.resetPassword(
                        emailInput,
                        "Username-Password-Authentication"
                    )
                    request.start(callback)

                } else {
                    if (!isEmailNotEmpty) {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.entrer_votre_Email),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    if (!isEmailValide) {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.format_email_incorrecte),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }


    }
    private fun isEmpty(): Boolean {
        var empty = false
        if (TextUtils.isEmpty(input_nom_email_reset.text)) {
            empty = true
            input_nom_email_reset.error = "Champ Vide"
        }

        return empty
    }
 var callback = object : BaseCallback<Void, AuthenticationException>{
     override fun onFailure(error: AuthenticationException?) {
         Timber.tag("Callback").e(error!!)

     }

     override fun onSuccess(payload: Void?) {
         this@ResetPasswordActivity.runOnUiThread {
             Timber.tag("Callback").e(payload.toString())
             val builder = AlertDialog.Builder(this@ResetPasswordActivity)
             //set title for alert dialog
             builder.setTitle("Restoration du mot de passe")
             //set message for alert dialog
             builder.setMessage("Vous venez de changer votre mot de passe")
             builder.setIcon(android.R.drawable.ic_dialog_alert)
             //performing positive action
             builder.setPositiveButton("Yes") { dialogInterface, which ->
               /*  Toast.makeText(applicationContext, "clicked yes", Toast.LENGTH_LONG)
                     .show() */
                 start()

             }
             //performing cancel action

             // Create the AlertDialog
             val alertDialog = builder.create()
             // Set other dialog properties
             alertDialog.setCancelable(false)
             alertDialog.show()

         }

     }

}
    private fun start(){
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }

    private  fun startActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = OPEN_ACCUEIL
        startActivity(intent)
        finish()
    }


  /*  private var lockCallback = object : AuthenticationCallback() {
        override fun onAuthentication(credentials: Credentials?) {
            Timber.tag("TAG_HOUSSEM").i(credentials!!.idToken.toString())
            credentialsManager.saveCredentials(credentials)
            Timber.tag("TAG_HOUSSEM").i(credentials.idToken!!)
            credentialsManager.saveCredentials(credentials)
            refreshSession()
            (application as SmobileApp).initAttributesPointeuse()
            (application as SmobileApp).refreshPointeuseActivites()
            (application as SmobileApp).refreshDataWidget()
            Timber.tag("borne").e("auth true ")
            getProfile(credentials.accessToken!!, authenticationAPIClient)
        }

        override fun onCanceled() {
            finish()
        }

        override fun onError(error: LockException?) {

        }
    }*/

}


