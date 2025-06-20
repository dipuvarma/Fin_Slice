package com.dipuguide.finslice.presentation.screens.addTransaction.expense

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.NoteAlt
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
import com.dipuguide.finslice.presentation.screens.main.home.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpenseScreen(
    addExpenseViewModel: AddExpenseViewModel,
    homeViewModel: HomeViewModel,
    navController: NavController,
) {
    val focusManager = LocalFocusManager.current
    val homeUiState by homeViewModel.homeUiState.collectAsState()

    // States - you should lift them to a ViewModel in a real app
    val uiState by addExpenseViewModel.addExpenseUiState.collectAsState()

    val event by addExpenseViewModel.addExpenseUiEvent.collectAsState(AddExpenseUiEvent.Idle)
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    val selectedCategory = uiState.category

    val tags = addExpenseViewModel.expenseTagsByCategory[selectedCategory] ?: emptyList()

    LaunchedEffect(Unit) {
        //for request focus on amount textField
        focusRequester.requestFocus()

        // for event
        addExpenseViewModel.addExpenseUiEvent.collect { event ->
            when (event) {
                is AddExpenseUiEvent.Success -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
                        .show()
                    // navigate
                    navController.navigate(Main) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                    //clear input
                    addExpenseViewModel.clearForm()
                }

                is AddExpenseUiEvent.Error -> {
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
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.expense_icon),
                        contentDescription = "Wallet Icon",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            CustomDatePicker(
                onDateSelected = { millis ->
                    addExpenseViewModel.setDate(millis)
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
                onValueChange = { addExpenseViewModel.updatedAmount(it) },
                placeholder = { Text(text = "Add Amount") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.CurrencyRupee, contentDescription = null)
                },
                trailingIcon = {
                    if (uiState.amount.isNotEmpty()) {
                        IconButton(onClick = { addExpenseViewModel.clearAmount() }) {
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
                onValueChange = { addExpenseViewModel.updatedNote(it) },
                placeholder = { Text(text = "Add Note") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.NoteAlt, contentDescription = null)
                },
                trailingIcon = {
                    if (!uiState.note.isNullOrEmpty()) {
                        IconButton(onClick = { addExpenseViewModel.clearNote() }) {
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
                menuItemList = addExpenseViewModel.expenseCategories,
                onDropDownClick = {
                    addExpenseViewModel.setCategory(it)
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
                            addExpenseViewModel.setTag(tag)
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
                    val errorMessage = addExpenseViewModel.validateAmount(homeUiState)
                    if (errorMessage != null) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    } else {
                        addExpenseViewModel.addExpenseTransaction()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .padding(horizontal = 88.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = uiState.amount.isNotEmpty() && uiState.category.isNotEmpty()
            ) {
                AnimatedContent(
                    targetState = event is AddExpenseUiEvent.Loading
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