package com.dipuguide.finslice.presentation.screens.addTransaction.income

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.R
import com.dipuguide.finslice.presentation.component.CustomDatePicker
import com.dipuguide.finslice.presentation.component.DropDownComp
import com.dipuguide.finslice.presentation.component.FormLabel
import com.dipuguide.finslice.presentation.component.WalletIconHeader
import com.dipuguide.finslice.presentation.navigation.Main
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(
    addIncomeViewModel: AddIncomeViewModel,
    navController: NavController,
) {
    val focusManager = LocalFocusManager.current
    val uiState by addIncomeViewModel.addIncomeUiState.collectAsState()
    val event by addIncomeViewModel.addIncomeUiEvent.collectAsState(AddIncomeUiEvent.Idle)
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    // 🐛 FIX: Collect events safely once without leaking collection
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()

        addIncomeViewModel.addIncomeUiEvent.collectLatest { event ->
            when (event) {
                is AddIncomeUiEvent.Success -> {
                    Log.d(
                        "AddIncomeScreen",
                        "✅ Income Added Successfully: ₹${uiState.amount}, Note: ${uiState.note}"
                    )
                    Toast.makeText(
                        context,
                        event.message.ifBlank { context.getString(R.string.income_added_successfully) }, // 💬 Fallback message
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(Main) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                    addIncomeViewModel.clearForm()
                }

                is AddIncomeUiEvent.Error -> {
                    Log.e("AddIncomeScreen", "❌ Error adding income: ${event.message}")
                    Toast.makeText(
                        context,
                        event.message.ifBlank { context.getString(R.string.error_occurred) }, // 💬 Fallback message
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        // Wallet Icon Box
        WalletIconHeader(
            icon = R.drawable.income_icon,
            iconDesc = R.string.cd_income_icon
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large))) // ❗ Suggest: dimensionResource

        CustomDatePicker(
            onDateSelected = addIncomeViewModel::setDate,
            modifier = Modifier.fillMaxWidth(),
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_icon),
                    contentDescription = stringResource(id = R.string.cd_select_date),
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium))) // ❗ Suggest: dimensionResource

        FormLabel(text = stringResource(id = R.string.label_amount)) // ✅ Localized
        OutlinedTextField(
            value = uiState.amount,
            onValueChange = addIncomeViewModel::updatedAmount,
            placeholder = { Text(text = stringResource(id = R.string.hint_add_amount)) },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = null)
            },
            trailingIcon = {
                if (uiState.amount.isNotEmpty()) {
                    IconButton(onClick = addIncomeViewModel::clearAmount) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = stringResource(id = R.string.cd_clear)
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
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

        FormLabel(text = stringResource(id = R.string.label_note_optional))
        OutlinedTextField(
            value = uiState.note.orEmpty(),
            onValueChange = addIncomeViewModel::updatedNote,
            placeholder = { Text(text = stringResource(id = R.string.hint_add_note)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.NoteAlt, contentDescription = null)
            },
            trailingIcon = {
                if (!uiState.note.isNullOrEmpty()) {
                    IconButton(onClick = addIncomeViewModel::clearNote) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = stringResource(id = R.string.cd_clear)
                        )
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

        DropDownComp(
            menuName = stringResource(id = R.string.label_category),
            menuItemList = addIncomeViewModel.incomeCategories,
            onDropDownClick = addIncomeViewModel::setCategory,
            selectedText = uiState.category,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                Log.d(
                    "AddIncomeScreen",
                    "🟩 Save Clicked: ₹${uiState.amount} in ${uiState.category}"
                )
                addIncomeViewModel.addIncomeTransaction()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            shape = MaterialTheme.shapes.medium,
            enabled = uiState.amount.isNotEmpty() && uiState.category.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.background,
                containerColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            AnimatedContent(targetState = event is AddIncomeUiEvent.Loading) { loading ->
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.save),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

