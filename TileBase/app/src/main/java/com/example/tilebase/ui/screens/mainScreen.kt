package com.example.tilebase.ui.screens

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tilebase.Tile
import com.example.tilebase.TileList


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController, selectedRoute = "main")
        },
        topBar = {
            TopAppBar(
                title = {Text("Tile Base", fontSize = 20.sp, fontWeight = FontWeight.Bold)},
                backgroundColor = Color.White,
                contentColor = Color.Black,
                elevation = 4.dp,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val tileList = TileList()

            // Dodaj kafelki
            tileList.addTile(Tile(1, "#FF5733"))
            tileList.addTile(Tile(2, "#33FF57"))
            tileList.addTile(Tile(3, "#3357FF"))

            TileListScreen(tileList)

        }
    }
}

//--------------Tiles-----------------------

@Composable
fun TileListScreen(tileList: TileList) {
    val tiles = remember { mutableStateListOf<Tile>().apply { addAll(tileList.getAllTiles()) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tytuł ekranu
        Text(
            text = "Tile List",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

            IconButton(onClick = {
                val newTile = Tile(id = tiles.size + 1, color = "#ccd0e3") // Domyślnie biały kafelek
                tiles.add(newTile)
                tileList.addTile(newTile)
            },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                    .background(Color(android.graphics.Color.parseColor("#ccd0e3")), RoundedCornerShape(8.dp))

            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "ADD"
                )
            }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(tiles, key = { _, tile -> tile.id }) { index, tile ->
                DraggableTile(
                    tile = tile,
                    onRemove = {
                        tiles.remove(tile)
                        tileList.removeTileById(tile.id) // Usunięcie z TileList
                    },
                    updateTileColor = { newColor ->
                        tiles[index] = tile.copy(color = newColor)
                        tileList.updateTileColor(tile.id, newColor) // Aktualizacja koloru w TileList
                    },
                    onDragEnd = { fromIndex, toIndex ->
                        if (toIndex in tiles.indices) {
                            val movedTile = tiles.removeAt(fromIndex)
                            tiles.add(toIndex, movedTile)
                            tileList.swapTiles(fromIndex, toIndex) // Zaktualizuj TileList
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun DraggableTile(
    tile: Tile,
    onRemove: () -> Unit,
    updateTileColor: (String) -> Unit,
    onDragEnd: (Int, Int) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }  // Stan dla okna dialogowego

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(android.graphics.Color.parseColor(tile.color)), RoundedCornerShape(8.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tile ${tile.id}",
                color = Color.White,
                fontSize = 16.sp
            )
                // Przycisk do zmiany koloru
                IconButton(
                    onClick = { isDialogOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit color",
                        tint = Color.White
                    )
                }

                // Przycisk do usunięcia
                IconButton(
                    onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete tile",
                        tint = Color.White
                    )
                }

        }
    }

    // Wyświetlanie dialogu z paletą kolorów
    if (isDialogOpen) {
        ColorPalette(
            onDismiss = { isDialogOpen = false },
            onColorSelected = { selectedColor ->
                tile.color = selectedColor // Zaktualizuj kolor kafelka
                updateTileColor(selectedColor) // Przekaż zmianę koloru do rodzica
                isDialogOpen = false
            }
        )
    }
}

@Composable
fun ColorPalette(
    onDismiss: () -> Unit,
    onColorSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Color") },
        text = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
                // Lista dostępnych kolorów
                val colors = listOf("#FF5733", "#33FF57", "#3357FF", "#FF33A1", "#FFD700")
                for (color in colors) {
                    Box(
                        modifier = Modifier
                            .size(40.dp) // Określamy rozmiar okręgu
                            .clip(CircleShape) // Nadajemy kształt okręgu
                            .background(Color(android.graphics.Color.parseColor(color)))
                            .border(2.dp, Color.Black, CircleShape)
                            .clickable {
                                onColorSelected(color) // Wybór koloru
                            }

                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
