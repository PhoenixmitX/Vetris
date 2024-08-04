package de.phoenixmitx.vetris

import de.phoenixmitx.vetris.data.GameState
import net.apiduck.ducktape.DT
import net.apiduck.ducktape.structure.Structure.*
import net.apiduck.ducktape.web.signals.Signal
import net.apiduck.ducktape.web.signals.Signal.*
import components.GameField

object App:
	def apply(): DT.DTX =

		val gameState = Signal.State(GameState())
		val rows = gameState.biMap(_.gameField, (gameState, rows) => gameState.copy(gameField = rows))

		div(style := "max-width: fit-content; margin-inline: auto")(
			settingControls,
			GameField(rows)
		)

	def settingControls: DT.DTX =
		div(style := "display: grid; grid-template-columns: auto max-content")(
			div(
				si"Square size (${Settings.squareSize}px): ",
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
