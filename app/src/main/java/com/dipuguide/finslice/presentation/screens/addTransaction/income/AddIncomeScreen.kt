package com.dipuguide.finslice.presentation.screens.addTransaction.income

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.component.CustomDatePicker
import com.dipuguide.finslice.presentation.component.DropDownComp
import com.dipuguide.finslice.presentation.component.FormLabel
import com.dipuguide.finslice.presentation.navigation.Main
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionUi
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeUiEvent

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(
    addIncomeViewModel: AddIncomeViewModel,
    navController: NavController,
) {

    val focusManager = LocalFocusManager.current

    // States - you should lift them to a ViewModel in a real app
    val uiState by addIncomeViewModel.addIncomeUiState.collectAsState()
    val event by addIncomeViewModel.addIncomeUiEvent.collectAsState(IncomeUiEvent.Idle)
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        //for request focus on amount textField
        focusRequester.requestFocus()

        // for event
        addIncomeViewModel.addIncomeUiEvent.collect { event ->
            when (event) {
                is AddIncomeUiEvent.Success -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                        .show()
                    // navigate
                    navController.navigate(Main) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                    //clear input
                    addIncomeViewModel.clearForm()

                }

                is AddIncomeUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {}
            }
        }
    }

    //start ui Design
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Top,
    ) {

        // Wallet Icon Box
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.income_icon),
                    contentDescription = "Income Icon",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        CustomDatePicker(
            onDateSelected = { millis ->
                addIncomeViewModel.setDate(millis)
            },
            modifier = Modifier
                .fillMaxWidth(),
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Amount Field
        FormLabel(text = "Amount")
        OutlinedTextField(
            value = uiState.amount,
            onValueChange = { addIncomeViewModel.updatedAmount(it) },
            placeholder = { Text(text = "Add Amount") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = null)
            },
            trailingIcon = {
                if (uiState.amount.isNotEmpty()) {
                    IconButton(onClick = { addIncomeViewModel.clearAmount() }) {
                        Icon(imageVector = Icons.Default.Cancel, contentDescription = "Clear")
                    }
                }

            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .focusRequester(focusRequester),
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Note Field
        FormLabel(text = "Note (Optional)")
        OutlinedTextField(
            value = uiState.note.orEmpty(),
            onValueChange = { addIncomeViewModel.updatedNote(it) },
            placeholder = { Text(text = "Add Note") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.NoteAlt, contentDescription = null)
            },
            trailingIcon = {
                if (!uiState.note.isNullOrEmpty()) {
                    IconButton(onClick = { addIncomeViewModel.clearNote() }) {
                        Icon(imageVector = Icons.Default.Cancel, contentDescription = "Clear")
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Category Selector
        DropDownComp(
            menuName = "Category",
            menuItemList = addIncomeViewModel.incomeCategories,
            onDropDownClick = {
                addIncomeViewModel.setCategory(it)
            },
            selectedText = uiState.category,
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Save Button
        Button(
            onClick = {
                addIncomeViewModel.addIncomeTransaction()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .padding(horizontal = 88.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = uiState.amount.isNotEmpty() && uiState.category.isNotEmpty()
        ) {
            AnimatedContent(
                targetState = event is AddIncomeUiEvent.Loading
            ) { loading ->
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
