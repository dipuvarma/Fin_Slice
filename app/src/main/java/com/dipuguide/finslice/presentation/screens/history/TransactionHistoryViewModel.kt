package com.dipuguide.finslice.presentation.screens.history

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


class TransactionHistoryViewModel: ViewModel() {

    var selectedTab by mutableStateOf("Expense")

    fun getIncomeCategoryIcon(category: String): ImageVector {
        return when (category) {
            "Salary" -> Icons.Default.AttachMoney
            "Freelance" -> Icons.Default.Work
            "Investments" -> Icons.Default.TrendingUp
            "Rental Income" -> Icons.Default.HomeWork
            "Gifts" -> Icons.Default.CardGiftcard
            "Business" -> Icons.Default.Business
            "Interest" -> Icons.Default.Savings
            "Dividends" -> Icons.Default.ShowChart
            "Selling Assets" -> Icons.Default.ShoppingCart
            "Refunds" -> Icons.Default.ReceiptLong
            "Others" -> Icons.Default.Receipt
            else -> Icons.Default.Receipt
        }
    }

}