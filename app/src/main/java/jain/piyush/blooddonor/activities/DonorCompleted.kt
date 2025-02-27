package jain.piyush.blooddonor.activities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import jain.piyush.blooddonor.viewmodel.PersonDatabaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorCompleted(viewModel: PersonDatabaseViewModel,
                   navController: NavController){
    val completedDonors by viewModel.completedPersons.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Completed Donors") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn {
                items(completedDonors) { donor ->
                    PersonItem(
                        person = donor,
                        onDelete = { /* Optional: Allow removing from completed list */ },
                        onCompleted = { /* No action needed */ }
                    )
                }
            }
        }
    }
}

