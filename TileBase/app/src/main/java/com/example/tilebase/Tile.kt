package com.example.tilebase
import kotlinx.serialization.Serializable

@Serializable
data class Tile(
    var id: Int,
    var color: String // Reprezentacja koloru w formacie HEX (np. "#FF5733")
)

class TileList {
    private val tiles: MutableList<Tile> = mutableListOf()

    fun addTile(tile: Tile) {
        updateId()
//        if (tiles.any { it.id == tile.id }) {
//            throw IllegalArgumentException("Tile with ID ${tile.id} already exists.")
//        }
        tiles.add(tile)
        updateId()
    }

    fun removeTileById(tileId: Int) {
        val removed = tiles.removeAll { it.id == tileId }
        if (!removed) {
            println("Tile with ID $tileId not found.")
        }
        updateId()
    }

    fun getTileById(tileId: Int): Tile? {
        return tiles.find { it.id == tileId }
    }

    fun updateTileColor(id: Int, newColor: String) {
        val tile = getTileById(id)
        if (tile != null) {
            tile.color = newColor
        } else {
            println("Tile with ID $id not found.")
        }
    }

    fun swapTiles(fromIndex: Int, toIndex: Int) {
        if (fromIndex in tiles.indices && toIndex in tiles.indices) {
            tiles[fromIndex] = tiles[toIndex].also { tiles[toIndex] = tiles[fromIndex] }
        } else {
            println("Invalid indices for swapping: $fromIndex, $toIndex")
        }
    }

    fun getAllTiles(): List<Tile> = tiles.toList()

    private fun updateId(){
        for (i in 0 until tiles.size) {
            tiles[i].id = i
        }
    }

    fun clear() {
        tiles.clear()
    }
}
