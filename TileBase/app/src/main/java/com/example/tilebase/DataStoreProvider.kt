package com.example.tilebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DataStoreProvider {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var _data = MutableStateFlow(TileList()) // Strumień do obserwacji
    var data = _data.asStateFlow() // Eksponujemy tylko `StateFlow`

    fun updateData(newList: TileList) {
        _data.value = newList // Aktualizacja StateFlow
        saveTileList() // Zapisujemy do Firestore
    }

    fun saveTileList() {
        val userId = auth.currentUser?.uid ?: return

        val tileList = data.value
        val newData = mapOf("tiles" to tileList.getAllTiles())

        db.collection("users").document(userId)
            .set(newData) // `set()` nadpisze dane zamiast je dodawać
    }

    fun loadTileList() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val tilesList = document.get("tiles") as? List<Map<String, Any>>
                    if (tilesList != null) {
                        val newTileList = TileList() // Tworzymy nową listę
                        tilesList.forEach { tileMap ->
                            try {
                                val id = (tileMap["id"] as? Number)?.toInt() ?: return@forEach
                                val color = tileMap["color"] as? String ?: return@forEach
                                val tile = Tile(id, color)
                                newTileList.addTile(tile)
                            } catch (e: Exception) {
                                println("Błąd mapowania: ${e.message}")
                            }
                        }
                        data.value.clear();
                        data.value.overwriteWith(newTileList); // Nadpisujemy całą listę
                    }
                }
            }
            .addOnFailureListener{
                data.value.clear();
            }
    }

}







