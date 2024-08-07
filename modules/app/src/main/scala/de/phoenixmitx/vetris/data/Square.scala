package de.phoenixmitx.vetris.data

case class Square(
	state: Square.State,
	color: String,
)

object Square:
	enum State:
		case Moving
		case Static
		case Empty

	val empty = Square(State.Empty, "000")
