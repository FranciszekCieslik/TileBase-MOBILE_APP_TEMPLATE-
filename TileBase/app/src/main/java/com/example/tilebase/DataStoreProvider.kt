package com.example.tilebase

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DataStoreProvider {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    var data = mutableStateOf<TileList>(TileList())

    // Zapis danych użytkownika
    fun saveTileList() {
        val userId = auth.currentUser?.uid ?: return

        val tileList = data.value
        val data = mapOf("tiles" to tileList.getAllTiles())

        db.collection("users").document(userId)
            .set(data)
    }

    // Pobranie danych użytkownika
    fun loadTileList() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Pobranie listy map z dokumentu
                    val tilesList = document.get("tiles") as? List<Map<String, Any>>
                    if (tilesList != null) {
                        tilesList.forEach { tileMap ->
                            try {
                                val id = (tileMap["id"] as? Number)?.toInt() ?: return@forEach
                                val color = tileMap["color"] as? String ?: return@forEach
                                val tile = Tile(id, color)
                                val tiles: TileList = data.value
                                tiles.addTile(tile) // Dodanie do TileList
                                data.value = tiles
                            } catch (e: Exception) {
                                println("Błąd mapowania: ${e.message}")
                            }
                        }
                    }
                }
                }
    }




}
