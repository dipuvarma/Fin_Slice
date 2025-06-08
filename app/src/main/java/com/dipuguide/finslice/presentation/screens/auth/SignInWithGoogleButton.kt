package com.dipuguide.finslice.presentation.screens.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dipuguide.finslice.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

@Composable
fun SignInWithGoogleButton(
    onSuccess: (FirebaseUser) -> Unit,
    onError: (Exception?) -> Unit,
    title: String
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
        onClick = {
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = MaterialTheme.colorScheme.background
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.google_icon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
        )
    }
}