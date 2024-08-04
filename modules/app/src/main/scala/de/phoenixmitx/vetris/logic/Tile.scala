package de.phoenixmitx.vetris.logic

enum Tile(val color: String, val unrotatedShape: Seq[Seq[Boolean]], val orientations: Int):
	case L(val rotation: Int) extends Tile("ff7f00", Seq(
		Seq(false, false, true),
		Seq(true, true, true)
	), 4)
	case J(rotation: Int) extends Tile("00f", Seq(
		Seq(true, false, false),
		Seq(true, true, true)
	), 4)
	case O(rotation: Int) extends Tile("ff0", Seq(
		Seq(true, true),
		Seq(true, true)
	), 1)
	case I(rotation: Int) extends Tile("0ff", Seq(
		Seq(true),
		Seq(true),
		Seq(true),
		Seq(true)
	), 2)
	case S(rotation: Int) extends Tile("0f0", Seq(
		Seq(false, true, true),
		Seq(true, true, false)
	), 2)
	case Z(rotation: Int) extends Tile("f00", Seq(
		Seq(true, true, false),
		Seq(false, true, true)
	), 2)
	case T(rotation: Int) extends Tile("a0a", Seq(
		Seq(false, true, false),
		Seq(true, true, true)
	), 4)

	def rotate: Tile = this match
		case L(rotation) => L(rotation + 1)
		case J(rotation) => J(rotation + 1)
		case O(rotation) => O(rotation)
		case I(rotation) => I(rotation + 1)
		case S(rotation) => S(rotation + 1)
		case Z(rotation) => Z(rotation + 1)
		case T(rotation) => T(rotation + 1)

	def rotation: Int
	val shape: Seq[Seq[Boolean]] =
		(0 until (rotation % orientations)).foldLeft(shape): (shape, _) =>
			shape.transpose.map(_.reverse)

