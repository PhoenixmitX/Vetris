package de.phoenixmitx.vetris

import de.phoenixmitx.vetris.data.GameState
import net.apiduck.ducktape.DT
import net.apiduck.ducktape.structure.Structure.*
import net.apiduck.ducktape.web.signals.Signal
import net.apiduck.ducktape.web.signals.Signal.*
import org.scalajs.dom.*

import components.GameField
import de.phoenixmitx.vetris.data.Square

object App:
	def apply(): DT.DTX =

		val leftGameState = Signal.State(GameState())
		val rightGameState = Signal.State(GameState())

		def insertRowsLeft(row: Seq[Seq[Square]]): Unit = if row.length >= 2 then leftGameState.modify(_.insertRowsAtBottom(row))
		def insertRowsRight(rows: Seq[Seq[Square]]): Unit = if rows.length >= 2 then rightGameState.modify(_.insertRowsAtBottom(rows))

		leftGameState.modify(_.copy(clearRowCallBack = insertRowsRight))
		rightGameState.modify(_.copy(clearRowCallBack = insertRowsLeft))

		val allGameStates = Seq(leftGameState, rightGameState)

		window.setInterval(() => allGameStates.foreach(_.modify(_.moveBy(1, 0, nextPieceIfFail = true))), 1000)

		document.addEventListener("keydown", (event: KeyboardEvent) =>
			event.key match
				case "w" => leftGameState.modify(_.rotate)
				case "a" => leftGameState.modify(_.moveBy(0, -1, nextPieceIfFail = false))
				case "s" => leftGameState.modify(_.moveBy(1, 0, nextPieceIfFail = true))
				case "d" => leftGameState.modify(_.moveBy(0, 1, nextPieceIfFail = false))
				case "ArrowUp" => rightGameState.modify(_.rotate)
				case "ArrowLeft" => rightGameState.modify(_.moveBy(0, -1, nextPieceIfFail = false)) // TODO rename modify to update
				case "ArrowDown" => rightGameState.modify(_.moveBy(1, 0, nextPieceIfFail = true))
				case "ArrowRight" => rightGameState.modify(_.moveBy(0, 1, nextPieceIfFail = false))
		)

		div(style := "max-width: fit-content; margin-inline: auto")(
			settingControls,
			div(style := si"display: grid; grid-template-columns: 1fr 1fr; gap: ${Settings.squareSize.get()*2}px")(
				GameField(leftGameState.map(_.gameField)),
				GameField(rightGameState.map(_.gameField)),
			),
		)

	def settingControls: DT.DTX =
		div(style := "display: grid; grid-template-columns: auto max-content")(
			div(
				si"Square size: ${Settings.squareSize}px",
				br,
				input(
					`type` := "range",
					min := 10,
					max := 50,
					value := Settings.squareSize.map(_.toString), // TODO value should also accept numbers
					onChange := { event =>
						val target = event.target.asInstanceOf[org.scalajs.dom.html.Input]
						Settings.squareSize := target.value.toInt
					}
				),
			),
			div(
				si"Gap size: ${Settings.gap}px",
				br,
				input(
					`type` := "range",
					min := 1,
					max := 10,
					value := Settings.gap.map(_.toString),
					onChange := { event =>
						val target = event.target.asInstanceOf[org.scalajs.dom.html.Input]
						Settings.gap := target.value.toInt
					}
				),
			),
		)
