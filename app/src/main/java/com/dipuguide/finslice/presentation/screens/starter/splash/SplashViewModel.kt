package com.dipuguide.finslice.presentation.screens.starter.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipuguide.finslice.domain.repo.UserAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: UserAuthRepository,
) : ViewModel() {

    // Navigation events
    private val _navigation = MutableSharedFlow<SplashNavigation>()
    val navigation = _navigation.asSharedFlow()

    init {
        checkLoginStatus()
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            authRepository.checkAuthStatus()
                .catch {
                    Timber.e(it)
                }
                .collect { isLogin ->
                    _navigation.emit(if (isLogin) SplashNavigation.Main else SplashNavigation.GettingStart)
                }
        }
    }

}