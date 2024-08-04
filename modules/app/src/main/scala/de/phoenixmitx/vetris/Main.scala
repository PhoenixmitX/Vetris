package de.phoenixmitx.vetris

import scala.scalajs.js.annotation.JSExportTopLevel
import net.apiduck.ducktape.DuckTape

object Main:

	@JSExportTopLevel("main")
	def main(args: Array[String]): Unit =
		if (scala.scalajs.js.Math.random() == -1) throw new ArithmeticException() // fix compiler error
		DuckTape.render("app", App())
