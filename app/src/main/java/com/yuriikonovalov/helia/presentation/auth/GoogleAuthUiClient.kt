package com.yuriikonovalov.helia.presentation.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.yuriikonovalov.helia.R
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException


class GoogleAuthUiClient(private val context: Context) {
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    suspend fun getIntentSenderRequest(): IntentSenderRequest? {
        return try {
            val result = oneTapClient.beginSignIn(buildSignInRequest()).await()
            val intentSender = result?.pendingIntent?.intentSender

            IntentSenderRequest.Builder(intentSender!!).build()

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
    }

    fun getToken(intent: Intent): String {
        return oneTapClient.getSignInCredentialFromIntent(intent).googleIdToken!!
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}