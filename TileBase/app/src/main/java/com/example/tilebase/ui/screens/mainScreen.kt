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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tilebase.DataStoreProvider
import com.example.tilebase.Tile
import com.example.tilebase.TileList
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun MainScreen(navController: NavController,
               dataStoreProvider: DataStoreProvider
) {

    LaunchedEffect(navController.currentBackStackEntry) {
        (dataStoreProvider.data)
    }

    DisposableEffect(Unit) {
        onDispose {
            dataStoreProvider.saveTileList()
        }
    }

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
                TileListScreen(dataStoreProvider.data)

        }
    }
}

//--------------Tiles-----------------------

@Composable
fun TileListScreen(tileListState: MutableState<TileList>) {
    // Utwórz SnapshotStateList<Tile>, który będzie obserwował kafelki
    val tiles = remember { mutableStateListOf<Tile>() }
    val tileList: TileList = tileListState.value

    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            // Zamień elementy na liście
            tiles.move(from.index, to.index)
            tileList.swapTiles(from.index, to.index) // Aktualizuj TileList
        }
    )

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

        // Przycisk dodawania kafelka
        IconButton(
            onClick = {
                val newTile = Tile(id = tiles.size + 1, color = "#ccd0e3") // Domyślnie biały kafelek
                tiles.add(newTile)
                tileList.addTile(newTile)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                .background(
                    Color(android.graphics.Color.parseColor("#ccd0e3")),
                    RoundedCornerShape(8.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "ADD"
            )
        }

        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .fillMaxSize()
                .reorderable(state),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(tiles, key = { _, tile -> tile.id }) { index, tile ->
                ReorderableItem(
                    state = state,
                    key = tile.id, // Klucz elementu
                    defaultDraggingModifier = Modifier
                ) { isDragging ->
                    DraggableTile(
                        tile = tile,
                        isDragging = isDragging,
                        onRemove = {
                            tiles.remove(tile)
                            tileList.removeTileById(tile.id) // Usuń z TileList
                        },
                        updateTileColor = { newColor ->
                            tiles[index] = tile.copy(color = newColor)
                            tileList.updateTileColor(tile.id, newColor) // Zaktualizuj kolor w TileList
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DraggableTile(
    tile: Tile,
    isDragging: Boolean,
    onRemove: () -> Unit,
    updateTileColor: (String) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }  // Stan dla okna dialogowego
    val alpha = if (isDragging) 0.6f else 1f
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(android.graphics.Color.parseColor(tile.color)), RoundedCornerShape(8.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .alpha(alpha) // Zastosuj przezroczystość
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
                onClick = { isDialogOpen = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "edit color",
                    tint = Color.White
                )
            }

            // Przycisk do usunięcia
            IconButton(
                onClick = onRemove
            ) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lista dostępnych kolorów
                val colors = listOf("#FF5733", "#33FF57", "#3357FF", "#FF33A1", "#FFD700")
                for (color in colors) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
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

// Funkcja pomocnicza do przesuwania elementów na liście
fun <T> MutableList<T>.move(fromIndex: Int, toIndex: Int) {
    if (fromIndex in indices && toIndex in indices) {
        val item = removeAt(fromIndex)
        add(toIndex, item)
    }
}


