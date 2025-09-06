package com.dipuguide.finslice.presentation.screens.starter.forgetPassword

sealed class ForgetEvent {
    data class EmailChange(val email: String) : ForgetEvent()
    object SendRestClick : ForgetEvent()
    object SignInClick : ForgetEvent()
}

enum class ForgetNavigation {
    SignIn
}