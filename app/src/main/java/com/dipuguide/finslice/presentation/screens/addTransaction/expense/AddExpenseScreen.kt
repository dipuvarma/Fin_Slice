package com.dipuguide.finslice.presentation.screens.addTransaction.expense

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.dipuguide.finslice.presentation.screens.main.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpenseScreen(
    addExpenseViewModel: AddExpenseViewModel,
    homeViewModel: HomeViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val uiState by addExpenseViewModel.addExpenseUiState.collectAsState()
    val event by addExpenseViewModel.addExpenseUiEvent.collectAsState(AddExpenseUiEvent.Idle)

    val selectedCategory = uiState.category
    val tags = addExpenseViewModel.expenseTagsByCategory[selectedCategory] ?: emptyList()

    // 🔁 Only collect once on first composition
    LaunchedEffect(Unit) {

        focusRequester.requestFocus()

        addExpenseViewModel.addExpenseUiEvent.collectLatest { event ->
            when (event) {
                is AddExpenseUiEvent.Success -> {
                    Log.d("AddExpenseScreen", "Expense added: ₹${uiState.amount}, ${uiState.note}")
                    Toast.makeText(
                        context,
                        event.message.ifBlank { context.getString(R.string.expense_success) },
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(Main) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                    addExpenseViewModel.clearForm()
                }

                is AddExpenseUiEvent.Error -> {
                    Log.e("AddExpenseScreen", "Error: ${event.message}")
                    Toast.makeText(
                        context,
                        event.message.ifBlank { context.getString(R.string.expense_error) },
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
            .verticalScroll(rememberScrollState())
    ) {

        WalletIconHeader(
            icon = R.drawable.expense_icon,
            iconDesc = R.string.cd_wallet_icon
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))

        CustomDatePicker(
            onDateSelected = addExpenseViewModel::setDate,
            modifier = Modifier.fillMaxWidth(),
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_icon),
                    contentDescription = stringResource(id = R.string.cd_select_date),
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

        FormLabel(text = stringResource(id = R.string.label_amount))

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = addExpenseViewModel::updatedAmount,
            placeholder = { Text(text = stringResource(id = R.string.hint_enter_amount)) },
            leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null) },
            trailingIcon = {
                if (uiState.amount.isNotEmpty()) {
                    IconButton(onClick = addExpenseViewModel::clearAmount) {
                        Icon(
                            Icons.Default.Cancel,
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
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

        FormLabel(text = stringResource(id = R.string.label_note_optional))

        OutlinedTextField(
            value = uiState.note.orEmpty(),
            onValueChange = addExpenseViewModel::updatedNote,
            placeholder = { Text(text = stringResource(id = R.string.hint_add_note)) },
            leadingIcon = { Icon(Icons.Default.NoteAlt, contentDescription = null) },
            trailingIcon = {
                if (!uiState.note.isNullOrEmpty()) {
                    IconButton(onClick = addExpenseViewModel::clearNote) {
                        Icon(
                            Icons.Default.Cancel,
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
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

        DropDownComp(
            menuName = stringResource(id = R.string.label_category),
            menuItemList = addExpenseViewModel.expenseCategories,
            onDropDownClick = addExpenseViewModel::setCategory,
            selectedText = uiState.category
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

        if (selectedCategory.isNotEmpty()) {
            FormLabel(text = stringResource(id = R.string.label_tags))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                tags.forEach { tag ->
                    ExpenseTagChip(
                        tag = tag,
                        isSelected = uiState.tag == tag,
                        onClick = { addExpenseViewModel.setTag(tag) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))
        }

        Button(
            onClick = {
                val error = addExpenseViewModel.validateAmount(homeUiState)
                if (error != null) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                } else {
                    addExpenseViewModel.addExpenseTransaction()
                }
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
            AnimatedContent(
                targetState = event is AddExpenseUiEvent.Loading,
                transitionSpec = { fadeIn() with fadeOut() }
            ) { isLoading ->
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.save_button),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))
    }
}





@Composable
private fun ExpenseTagChip(
    tag: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        label = "ChipBgColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        label = "ChipContentColor"
    )

    AssistChip(
        onClick = onClick,
        label = { Text(tag) },
        shape = MaterialTheme.shapes.medium,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = backgroundColor,
            labelColor = contentColor
        )
    )
}
