package utils

import kotlin.js.Date

actual object Date {
    actual fun today(): String {
        return Date().toDateString()
    }
}