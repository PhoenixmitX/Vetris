package de.phoenixmitx.vetris.data

import de.phoenixmitx.vetris.logic.Tile
import scala.util.Random

case class GameState(
	tile: Tile,
	nextTile: Tile,
	remainingTiles: Seq[Tile],
	tilePosition: (Int, Int),
	gameField: Seq[Row] = Seq.fill(20)(Row(Seq.fill(10)(Square("empty", "black")))),
)

object GameState:
	val allTiles = Seq(
		Tile.L(0),
		Tile.J(0),
		Tile.O(0),
		Tile.I(0),
		Tile.S(0),
		Tile.Z(0),
		Tile.T(0)
	)

	def allTilesShuffled: Seq[Tile] = Random.shuffle(allTiles)

	def apply(): GameState =
		val (tile +: nextTile +: remainingTiles) = allTilesShuffled: @unchecked
		GameState(
			tile = tile,
			nextTile = nextTile,
			remainingTiles = remainingTiles,
			tilePosition = (-tile.shape.size, 4)
		)
