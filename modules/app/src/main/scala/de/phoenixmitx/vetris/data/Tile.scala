package de.phoenixmitx.vetris.data

import scala.util.Random

// TODO remove all the false only rows and columns and implement a better way to render the tiles
enum Tile(val color: String, val unrotatedShape: Seq[Seq[Boolean]], val orientations: Int):
	case L(val rotation: Int) extends Tile("ff7f00", Seq(
		Seq(false, false, true),
		Seq(true, true, true),
		Seq(false, false, false), // false row
	), 4)
	case J(rotation: Int) extends Tile("00f", Seq(
		Seq(true, false, false),
		Seq(true, true, true),
		Seq(false, false, false), // false row
	), 4)
	case O(rotation: Int) extends Tile("ff0", Seq(
		Seq(true, true),
		Seq(true, true),
	), 1)
	case I(rotation: Int) extends Tile("0ff", Seq(
		Seq(false, false, true, false), // a lot of false
		Seq(false, false, true, false), // a lot of false
		Seq(false, false, true, false), // a lot of false
		Seq(false, false, true, false), // a lot of false
	), 4)
	case S(rotation: Int) extends Tile("0f0", Seq(
		Seq(false, true, true),
		Seq(true, true, false),
		Seq(false, false, false), // false row
	), 4)
	case Z(rotation: Int) extends Tile("f00", Seq(
		Seq(true, true, false),
		Seq(false, true, true),
		Seq(false, false, false), // false row
	), 4)
	case T(rotation: Int) extends Tile("a0a", Seq(
		Seq(false, true, false),
		Seq(true, true, true),
		Seq(false, false, false), // false row
	), 4)

	def rotation: Int

	def rotate: Tile = this match
		case L(rotation) => L(rotation + 1)
		case J(rotation) => J(rotation + 1)
		case O(rotation) => O(rotation)
		case I(rotation) => I(rotation + 1)
		case S(rotation) => S(rotation + 1)
		case Z(rotation) => Z(rotation + 1)
		case T(rotation) => T(rotation + 1)

	val shape: Seq[Seq[Boolean]] =
		(0 until (rotation % orientations)).foldLeft(unrotatedShape): (shape, _) =>
			shape.transpose.map(_.reverse)

	val height: Int = shape.size
	val width: Int = shape.head.size

object Tile:

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
