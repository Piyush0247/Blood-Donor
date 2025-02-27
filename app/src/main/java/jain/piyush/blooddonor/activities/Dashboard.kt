package jain.piyush.blooddonor.activities

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import jain.piyush.blooddonor.R
import jain.piyush.blooddonor.navigation.Screen
import jain.piyush.blooddonor.roomdatabase.Person
import jain.piyush.blooddonor.viewmodel.PersonDatabaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DashBoard(navController: NavController,
              viewModel: PersonDatabaseViewModel) {
    val person by viewModel.persons.collectAsState(initial = emptyList())


    var showDialog by remember { mutableStateOf(false) }
    var refreshing  by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val pullrefersh = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            scope.launch {
                refreshing = true
                viewModel.refreshState()
                delay(2000)
                refreshing = false
            }
        }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Profile", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text("Profile") },
                        selected = false,
                        onClick = { navController.navigate(Screen.CreateProfile.route)

                        }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text("Locate Nearby Hospitals") },
                        selected = false,
                        onClick = {
                            Toast.makeText(
                                context,
                                "This function is under developed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text("Blood Donated") },
                        selected = false,
                        onClick = {
                           navController.navigate(Screen.DonorCompleted.route)
                        }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text("Dashboard") },
                        selected = false,
                        onClick = {
                            navController.navigate(Screen.Dashboard.route)
                        }
                    )
                }
            }
                        }, gesturesEnabled = false
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("DashBoard") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            Firebase.auth.signOut()
                            Toast.makeText(context, "Sign Out Successfully", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(Screen.LoginActivity.route) {
                                popUpTo("dashboard") {
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(
                                painterResource(R.drawable.log_out),
                                contentDescription = "log_out"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(30)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "add")
                }
            }
        ) { innerpadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding)
                    .pullRefresh(pullrefersh)
            ) {
                LazyColumn( modifier = Modifier.fillMaxSize()) {
                    items(person) { persons ->
                        PersonItem(
                            person = persons,
                            onDelete = { viewModel.deletePerson(persons) },
                            onCompleted = {viewModel.markPersonAsCompleted(persons)
                                Toast.makeText(context, "Person moved to Completed Donors", Toast.LENGTH_LONG).show()
                            }
                        )

                    }
                }
                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullrefersh,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
        if (showDialog) {
            PersonDialogBox(
                onDismiss = { showDialog = false },
                onAdd = { persons ->
                    viewModel.addPerson(persons)
                    showDialog = false
                }
            )
        }
    }
}
@Composable
fun PersonDialogBox(onDismiss:()->Unit,onAdd:(Person) -> Unit){
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Enter the details") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Name") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = age,
                    onValueChange = { age = it},
                    placeholder = { Text("Age") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    placeholder = { Text("Mobile number") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = bloodGroup,
                    onValueChange = { bloodGroup = it },
                    placeholder = { Text("Blood Group") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    placeholder = { Text("Location") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
       confirmButton = {
           Button(onClick = {
               if (name.isNotEmpty()
                   && age.isNotEmpty()
                   && mobile.isNotEmpty()
                   && bloodGroup.isNotEmpty()
                   && location.isNotEmpty()
               ) {
                   onAdd(Person( 0,name, age, mobile, bloodGroup, location))
               }

           }) { Text("Add") }
       },
        dismissButton = {
            Button(onClick = {onDismiss()}) {
                Text("Cancel")
            }
        }
    )
}
@Composable
fun PersonItem(person: Person,onDelete:()->Unit,onCompleted:()->Unit){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Name: ${person.name}")
            Text("Age: ${person.age}")
            Text("Mobile: ${person.mobile}")
            Text("Need Blood: ${person.bloodGroup}")
            Text("Location: ${person.location}")
            Row (horizontalArrangement = Arrangement.SpaceBetween){
                IconButton(onClick = {onDelete()}){
                    Icon(Icons.Filled.Delete, contentDescription = "delete")
                }
                IconButton(onClick = {

                    onCompleted()
                }){
                    Icon(Icons.Filled.Check, contentDescription = "done")
                }

            }
            }

        }
    }



