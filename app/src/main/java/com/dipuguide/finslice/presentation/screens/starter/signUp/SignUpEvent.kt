package com.dipuguide.finslice.presentation.screens.starter.signUp

sealed class SignUpEvent {
    data class NameChange(val name: String) : SignUpEvent()
    data class EmailChange(val email: String) : SignUpEvent()
    data class PasswordChange(val password: String) : SignUpEvent()
    object SignUpClick : SignUpEvent()
    object SignUpWithGoogle : SignUpEvent()
    object SignInClick : SignUpEvent()
    object PasswordVisibility : SignUpEvent()
}


enum class SignUpNavigation {
    OnBoard, SignIn
}
