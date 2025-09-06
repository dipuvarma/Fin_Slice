package com.dipuguide.finslice.presentation.screens.starter.signIn

sealed class SignInEvent {
    data class EmailChange(val email: String) : SignInEvent()
    data class PasswordChange(val password: String) : SignInEvent()
    object SignInClick : SignInEvent()
    object ForgetPasswordClick : SignInEvent()
    object SigInWithGoogleClick : SignInEvent()
    object SignUpClick : SignInEvent()
    object PasswordVisibility : SignInEvent()
}


enum class SignInNavigation {
    Main, SignUp, ForgetPassword
}
