package fr.strada.smobile.utils.ext

import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.AuthenticationCallback
import com.auth0.android.result.Credentials
import fr.strada.smobile.BuildConfig
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun AuthenticationAPIClient.authWithEmail(email: String, password: String): Credentials =
    suspendCoroutine { cont ->
        val callback = object : AuthenticationCallback<Credentials> {
            override fun onFailure(error: AuthenticationException) {
                cont.resumeWithException(error)
            }

            override fun onSuccess(payload: Credentials) {
                cont.resume(payload)
            }
        }
        val request = this.login(email, password, "Username-Password-Authentication")
        request.setScope("openid offline_access")
        request.setAudience(String.format("https://%s/api/v2/", BuildConfig.AUTH0_DOMAIN))
        request.start(callback)
    }