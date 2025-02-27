package jain.piyush.blooddonor.activities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodGroupScreen(navController: NavController,onNextClick:()->Unit){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pick your blood group") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    )

    {innerpadding ->
        Column (modifier = Modifier.fillMaxSize()
            .padding(innerpadding)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)){
           val bloodGroups = listOf("A","B","AB","O","A-","B-","O-","AB-")
            var selectedGroup by remember { mutableStateOf<String?>(null) }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(bloodGroups){group ->
                  BloodGroupCard(
                      group = group,
                      isSelected = group == selectedGroup,
                      onClick = {selectedGroup = group}
                  )
                }
            }
            Spacer(modifier = Modifier.weight(1f),
                )
            Button(onClick =onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC143C)
                ),
                shape = RoundedCornerShape(8.dp)
            )
                {
                Text("Continue")
            }
        }
    }
}
@Composable
fun BloodGroupCard(
    group:String,
    isSelected:Boolean,
    onClick:()->Unit
){
    Card (modifier = Modifier.fillMaxWidth().
    height(80.dp).
    clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Red else Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ){
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){
            Text(
                text = group,
                style = MaterialTheme.typography.headlineMedium,
                color = if (isSelected) Color.White else Color.Black
            )
        }
    }
}