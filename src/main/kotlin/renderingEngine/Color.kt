package renderingEngine

class Color {
    var r: Float
    var g: Float
    var b: Float
    var a: Float

    constructor(r: Float, g: Float, b: Float, a: Float = 1f) {
        require(r in 0.0..1.0 && g in 0.0..1.0 && b in 0.0..1.0 && a in 0.0..1.0) { "Invalid color" }
        this.r = r
        this.g = g
        this.b = b
        this.a = a
    }

    constructor(r: Int, g: Int, b: Int, a: Int = 255) : this(
        r.toFloat() / 255,
        g.toFloat() / 255,
        b.toFloat() / 255,
        a.toFloat() / 255
    )

    constructor(hexColor: String) {
        require(hexColor.length >= 7 || hexColor.first() != '#') { "Invalid hex color" }
        r = hexColor.substring(1, 3).toInt(radix = 16).toFloat() / 255
        g = hexColor.substring(3, 5).toInt(radix = 16).toFloat() / 255
        b = hexColor.substring(5, 7).toInt(radix = 16).toFloat() / 255
        a = if (hexColor.length == 9) hexColor.substring(7, 9).toInt(radix = 16).toFloat() / 255 else 1f
    }
}

val WHITE = Color(255, 255, 255)
val white = WHITE

val BLACK = Color(0, 0, 0)
val black = BLACK

val RED = Color(255, 0, 0)
val red = RED

val GREEN = Color(0, 255, 0)
val green = GREEN

val BLUE = Color(0, 0, 255)
val blue = BLUE

val YELLOW = Color(255, 255, 0)
val yellow = YELLOW

val CYAN = Color(0, 255, 255)
val cyan = CYAN

val MAGENTA = Color(255, 0, 255)
val magenta = MAGENTA

val GRAY = Color(128, 128, 128)
val gray = GRAY

val SILVER = Color(192, 192, 192)
val silver = SILVER

val MAROON = Color(128, 0, 0)
val maroon = MAROON

val OLIVE = Color(128, 128, 0)
val olive = OLIVE

val LIME = Color(0, 255, 0)
val lime = LIME

val AQUA = Color(0, 255, 255)
val aqua = AQUA

val TEAL = Color(0, 128, 128)
val teal = TEAL

val NAVY = Color(0, 0, 128)
val navy = NAVY

val FUCHSIA = Color(255, 0, 255)
val fuchsia = FUCHSIA

val PURPLE = Color(128, 0, 128)
val purple = PURPLE

val ORANGE = Color(255, 165, 0)
val orange = ORANGE

val PINK = Color(255, 192, 203)
val pink = PINK
