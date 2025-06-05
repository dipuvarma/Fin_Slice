package com.dipuguide.finslice.presentation.screens.main.transaction

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dipuguide.finslice.presentation.component.DropDownComp
import com.dipuguide.finslice.presentation.component.FormLabel
import com.dipuguide.finslice.presentation.navigation.Home

@Composable
fun AddExpenseScreen(
    expenseVM: ExpenseTransactionViewModel,
    navController: NavController,
) {
    val focusManager = LocalFocusManager.current

    // States - you should lift them to a ViewModel in a real app
    val uiState by expenseVM.expenseUiState.collectAsState()
    val event by expenseVM.expenseEvent.collectAsState(IncomeUiEvent.Idle)
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    val selectedCategory = uiState.category

    val tags = expenseVM.expenseTagsByCategory[selectedCategory] ?: emptyList()
    LaunchedEffect(Unit) {

        //for request focus on amount textField
        focusRequester.requestFocus()

        // for event
        expenseVM.expenseEvent.collect { event ->
            when (event) {
                is ExpenseTransactionUiEvent.Success -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                        .show()
                    // navigate
                    navController.navigate(Home)
                    //clear input
                    expenseVM.clearForm()
                }

                is ExpenseTransactionUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {}
            }
        }
    }

    //start ui Design
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        item {
            // Wallet Icon Box
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = "Wallet Icon",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Amount Field
            FormLabel(text = "Amount")
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = { expenseVM.updatedAmount(it) },
                placeholder = { Text(text = "Add Amount") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = null)
                },
                trailingIcon = {
                    if (uiState.amount.isNotEmpty()) {
                        IconButton(onClick = { expenseVM.clearAmount() }) {
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
                shape = MaterialTheme.shapes.medium,
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
                onValueChange = { expenseVM.updatedNote(it) },
                placeholder = { Text(text = "Add Note") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.NoteAlt, contentDescription = null)
                },
                trailingIcon = {
                    if (!uiState.note.isNullOrEmpty()) {
                        IconButton(onClick = { expenseVM.clearNote() }) {
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
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selector
            DropDownComp(
                menuName = "Category",
                menuItemList = expenseVM.expenseCategories,
                onDropDownClick = {
                    expenseVM.setCategory(it)
                },
                selectedText = uiState.category,
            )
            Spacer(modifier = Modifier.height(16.dp))
            //tag

            if (uiState.category.isNotEmpty()) {
                FormLabel(text = "Tags")
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tags.forEach { tag ->
                    val isSelected = uiState.tag == tag

                    val backgroundColor by animateColorAsState(
                        targetValue = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        animationSpec = tween(300),
                        label = "TagBgAnim"
                    )

                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface,
                        animationSpec = tween(300),
                        label = "TagContentColor"
                    )

                    AssistChip(
                        onClick = {
                            expenseVM.setTag(tag)
                        },
                        label = {
                            Text(text = tag)
                        },
                        trailingIcon = {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = contentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        },
                        shape = MaterialTheme.shapes.medium,
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = backgroundColor,
                            labelColor = contentColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Save Button
            Button(
                onClick = {
                    expenseVM.addExpenseTransaction(
                        ExpenseTransactionUi(
                            amount = uiState.amount,
                            note = uiState.note,
                            category = uiState.category,
                            tag = uiState.tag
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .padding(horizontal = 88.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = uiState.amount.isNotEmpty() && uiState.category.isNotEmpty()
            ) {
                AnimatedContent(
                    targetState = event is ExpenseTransactionUiEvent.Loading
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
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}