package br.com.fisiosolutions.domain.model

import java.util.Locale

object UserNameFormatter {

    fun isGenericName(name: String?): Boolean {
        if (name.isNullOrBlank()) return true
        val normalized = name.trim().lowercase(Locale.ROOT)
        return normalized == "usuário do google" ||
               normalized == "usuario do google" ||
               normalized == "usuário google" ||
               normalized == "usuario google" ||
               normalized == "usuário cadastrado" ||
               normalized == "usuario cadastrado" ||
               normalized == "google user" ||
               normalized == "usuário" ||
               normalized == "usuario" ||
               normalized.contains("usuário do google") ||
               normalized.contains("usuario do google") ||
               normalized.contains("usuário google") ||
               normalized.contains("usuario google")
    }

    fun formatNameFromEmail(email: String): String {
        val handle = email.substringBefore("@").trim()
        if (handle.isBlank()) return "Usuário Cadastrado"

        val cleanHandle = handle.replace(Regex("[0-9]+"), " ")
            .replace(".", " ")
            .replace("_", " ")
            .replace("-", " ")

        val words = cleanHandle.split("\\s+".toRegex()).filter { it.isNotBlank() }
        if (words.isEmpty()) {
            return handle.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        }

        return words.joinToString(" ") { word ->
            word.lowercase(Locale.ROOT).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
        }
    }

    fun getBestDisplayName(displayName: String?, email: String): String {
        if (!isGenericName(displayName)) {
            return displayName!!.trim()
        }
        if (email.isNotBlank()) {
            return formatNameFromEmail(email)
        }
        return "Usuário Cadastrado"
    }
}
