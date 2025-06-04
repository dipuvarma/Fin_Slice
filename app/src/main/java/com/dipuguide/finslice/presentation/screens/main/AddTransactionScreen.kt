package com.dipuguide.finslice.presentation.screens.main
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRow
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.AssistChip
//import androidx.compose.material3.AssistChipDefaults
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Tab
//import androidx.compose.material3.TabRow
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//
//
//// MainScreen.kt
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TransactionScreen(
//
//) {
//   // val uiState = viewModel.uiState.collectAsState()
//
//    val tabTitles = listOf("Expense", "Income")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // TabRow: Expense / Income
//        TabRow(
//            selectedTabIndex = uiState.value.selectedTab,
//            containerColor = Color.Transparent,
//            contentColor = MaterialTheme.colorScheme.primary
//        ) {
//            tabTitles.forEachIndexed { index, title ->
//                Tab(
//                    selected = uiState.value.selectedTab == index,
//                    onClick = { viewModel.onTabSelected(index) },
//                    text = {
//                        Text(
//                            text = title,
//                            style = MaterialTheme.typography.labelLarge,
//                            color = if (uiState.value.selectedTab == index)
//                                MaterialTheme.colorScheme.primary
//                            else MaterialTheme.colorScheme.onBackground
//                        )
//                    }
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Amount Field
//        OutlinedTextField(
//            value = uiState.value.amount,
//            onValueChange = viewModel::onAmountChange,
//            label = { Text("Amount") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Note Field
//        OutlinedTextField(
//            value = uiState.value.note,
//            onValueChange = viewModel::onNoteChange,
//            label = { Text("Note (Optional)") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Category Selector (Placeholder)
//        OutlinedButton(
//            onClick = { /* Navigate to category screen */ },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Choose a Category ➡️")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Tags Section
//        Text("Tags", style = MaterialTheme.typography.labelMedium)
//        Spacer(modifier = Modifier.height(8.dp))
//
//        FlowRow(
//            mainAxisSpacing = 8.dp,
//            crossAxisSpacing = 8.dp
//        ) {
//            uiState.value.tags.forEach { tag ->
//                AssistChip(
//                    onClick = { viewModel.toggleTag(tag) },
//                    label = { Text(tag) },
//                    colors = AssistChipDefaults.assistChipColors(
//                        containerColor = if (tag in uiState.value.selectedTags)
//                            MaterialTheme.colorScheme.primaryContainer
//                        else
//                            MaterialTheme.colorScheme.surfaceVariant
//                    )
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // Save Button
//        Button(
//            onClick = { viewModel.onSave() },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text("Save")
//        }
//    }
//}
//
//
//
//
