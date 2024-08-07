package de.phoenixmitx.vetris.data

import de.phoenixmitx.vetris.data.Square.State

case class GameState(
	tile: Tile,
	nextTile: Tile,
	remainingTiles: Seq[Tile],
	tilePosition: (Int, Int),
	clearRowCallBack: Seq[Seq[Square]] => Unit = _ => (),
	gameField: Seq[Seq[Square]] = Seq.fill(20)(Seq.fill(10)(Square.empty)),
):

	def canPlace(tile: Tile, position: (Int, Int)): Boolean =
		val (rowPos, colPos) = position
		tile.shape.zipWithIndex.forall((row, rowOffset) =>
			row.zipWithIndex.forall((cell, cellOffset) =>
				if cell then
					val newRow = rowPos + rowOffset
					val newCol = colPos + cellOffset
					newRow < 20 && newCol >= 0 && newCol < 10 && (newRow < 0 || gameField(newRow)(newCol).state != Square.State.Static)
				else true
			)
		)

	def moveBy(deltaRow: Int, deltaCol: Int, nextPieceIfFail: Boolean): GameState =
		val (rowPos, colPos) = tilePosition
		val newPosition = (rowPos + deltaRow, colPos + deltaCol)
		if canPlace(tile, newPosition) then
			copy(tilePosition = newPosition).updateGameField
		else if nextPieceIfFail then
			nextPiece
		else
			this

	def rotate: GameState =
		var rotatedTile = tile
		var success = false
		for _ <- 0 until (tile.orientations - 1) do
			if !success then
				rotatedTile = rotatedTile.rotate
				if canPlace(rotatedTile, tilePosition) then
					success = true
		if success then
			copy(tile = rotatedTile).updateGameField
		else
			this

	def nextPiece: GameState =
		val (nextTile +: remainingTiles) = this.remainingTiles: @unchecked
		var gameField = this.gameField
		var removedRows: Seq[Seq[Square]] = Seq.empty
		gameField = gameField.filter: row =>
			val hasEmptySquares = row.exists(_.state == Square.State.Empty)
			if !hasEmptySquares then
				removedRows = removedRows :+ row.map(square => if square.state == Square.State.Moving then Square.empty else square)
			hasEmptySquares
		if removedRows.nonEmpty then
			clearRowCallBack(removedRows)
		gameField = gameField.map(_.map(square => if square.state == Square.State.Moving then square.copy(state = Square.State.Static) else square))
		if gameField.length < 20 then
			val emptyRow = Seq.fill(10)(Square.empty)
			gameField = Seq.fill(20 - gameField.length)(emptyRow) ++ gameField
		copy(
			tile = this.nextTile,
			nextTile = nextTile,
			remainingTiles = if remainingTiles.isEmpty then Tile.allTilesShuffled else remainingTiles,
			tilePosition = (-tile.height, 5-(tile.width+1)/2),
			gameField = gameField,
		)

	private def updateGameField: GameState =
		val (rowPos, colPos) = tilePosition
		val newGameField = gameField.zipWithIndex.map((row, rowOffset) =>
			row.zipWithIndex.map((cell, cellOffset) =>
				val tileRowPos = rowOffset - rowPos
				val tileColPos = cellOffset - colPos
				if tileRowPos >= 0 && tileRowPos < tile.height && tileColPos >= 0 && tileColPos < tile.width && tile.shape(tileRowPos)(tileColPos) then
					Square(Square.State.Moving, tile.color)
				else if cell.state == Square.State.Moving then
					Square.empty
				else
					cell
			)
		)
		copy(gameField = newGameField)

	def insertRowsAtBottom(rows: Seq[Seq[Square]]): GameState =
		val newGameField = gameField.drop(rows.length) :++ rows
		copy(gameField = newGameField, tilePosition = (tilePosition._1 + rows.length, tilePosition._2))

object GameState:

	def apply(): GameState =
		val (tile +: nextTile +: remainingTiles) = Tile.allTilesShuffled: @unchecked
		GameState(
			tile = tile,
			nextTile = nextTile,
			remainingTiles = remainingTiles,
			tilePosition = (-tile.height, 5-(tile.width+1)/2),
		)
