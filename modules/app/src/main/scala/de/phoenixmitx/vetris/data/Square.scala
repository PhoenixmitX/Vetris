package de.phoenixmitx.vetris.data

case class Square(
	state: "moving" | "static" | "empty",
	color: String
)
