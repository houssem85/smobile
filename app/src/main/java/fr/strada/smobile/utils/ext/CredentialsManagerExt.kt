package fr.strada.smobile.utils.ext

import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun CredentialsManager.getToken(): String = suspendCoroutine { cont ->
   val callback = object :BaseCallback<Credentials , CredentialsManagerException>{
       override fun onSuccess(payload: Credentials) {
           cont.resume(payload.idToken!!)
       }

       override fun onFailure(error: CredentialsManagerException) {
           cont.resumeWithException(Exception(error.message))
       }
   }
    this.getCredentials(callback)
}