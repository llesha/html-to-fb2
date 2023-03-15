package utils

import java.time.LocalDateTime


actual object Date {
    actual fun today(): String {
        return LocalDateTime.now().toLocalDate().toString()
    }
}