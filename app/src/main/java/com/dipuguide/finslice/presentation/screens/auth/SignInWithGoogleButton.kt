package com.dipuguide.finslice.presentation.screens.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

@Composable
fun SignInWithGoogleButton(
    modifier: Modifier = Modifier,
    onSuccess: (FirebaseUser) -> Unit,
    onError: (Exception?) -> Unit
) {
    val signInLauncher = rememberLauncherForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { result ->
        val response = result.idpResponse
        if (result.resultCode == Activity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let { onSuccess(it) }
                ?: onError(Exception("Unable to get user"))
        } else {
            onError(response?.error)
        }
    }

    Button(
        modifier = modifier,
        onClick = {
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }
    ) {
        Text(
            text = "Sign-in with Google",
            style = MaterialTheme.typography.titleLarge
        )
    }
}