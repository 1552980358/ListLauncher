package sakuraba.saki.list.launcher.util

import java.util.IllegalFormatException

fun Int.getPower(power: Int): Int {
    var tmp = 1
    repeat(power) {
        tmp *= this
    }
    return tmp
}

class IllegalFormatException(message: String): IllegalArgumentException(message)

val String.isHexStr: Boolean get() {
    repeat(length) { index ->
        if (this[index] !in '0' .. '9' && this[index] !in 'A' .. 'F' && this[index] !in 'a' .. 'f') {
            return false
        }
    }
    return true
}

val String.hexStrToIntOrThrow: Int get() = if (!isHexStr) { throw IllegalFormatException("This is not a hex string") } else hexStrToInt

val String.hexStrToInt: Int get() {
    var tmp = 0
    repeat(length) { index ->
        tmp += when {
            this[index] in '0' .. '9' -> {
                (this[index].code - 48) * (16).getPower(lastIndex - index)
            }
            this[index] in 'A' .. 'F' -> {
                (this[index].code - 55) * (16).getPower(lastIndex - index)
            }
            this[index] in 'a' .. 'f' -> {
                (this[index].code - 87) * (16).getPower(lastIndex - index)
            }
            else -> throw IllegalFormatException("Unknown character: ${this[index]}")
        }
    }
    return tmp
}