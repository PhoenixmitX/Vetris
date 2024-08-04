package de.phoenixmitx.vetris.components

import de.phoenixmitx.vetris.data.Row
import de.phoenixmitx.vetris.data.Square
import net.apiduck.ducktape.DT
import net.apiduck.ducktape.structure.Structure.*
import net.apiduck.ducktape.web.signals.Signal
import net.apiduck.ducktape.web.signals.Signal.*
import de.phoenixmitx.vetris.Settings

object GameField:

	def apply(rows: Signal[Seq[Row]]): DT.DTX =
		div(style := si"display: flex; flex-direction: column; gap: ${Settings.gap}px"):
			SForEach(rows):
				row

	private def row(row: Signal[Row]): DT.DTX =
		div(style := si"display: flex; gap: ${Settings.gap}px"):
			SForEach(row.map(_.squares)): square =>
				div(style := si"background-color: ${square.map(_.color)}; width: ${Settings.squareSize}px; height: ${Settings.squareSize}px")()
