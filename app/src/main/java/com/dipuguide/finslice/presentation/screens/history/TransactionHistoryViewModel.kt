package com.dipuguide.finslice.presentation.screens.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


class TransactionHistoryViewModel: ViewModel() {

    var selectedTab by mutableStateOf("Expense")

}