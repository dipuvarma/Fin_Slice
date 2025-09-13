package com.dipuguide.finslice.presentation.navigation

import kotlinx.serialization.Serializable

//onBoard
@Serializable
object SplashRoute

@Serializable
object GettingStartRoute

@Serializable
object OnBoardRoute

//Auth
@Serializable
object SignUpRoute

@Serializable
object SignInRoute

@Serializable
object ForgetPasswordRoute

//Main
@Serializable
object MainRoute

@Serializable
object Home {
    const val route = "home"
}

@Serializable
object Categories {
    const val route = "categories"
}

@Serializable
object Report {
    const val route = "report"
}


@Serializable
object TransactionHistory{
    const val route = "transactionHistory"
}

@Serializable
object Setting{
    const val route = "setting"

}


@Serializable
object AddTransactionRoute
