package com.unfpa.appsistenciamaternaunfpa.utils.bot

import com.unfpa.appsistenciamaternaunfpa.utils.bot.Constants.OPEN_GOOGLE
import com.unfpa.appsistenciamaternaunfpa.utils.bot.Constants.OPEN_SEARCH
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {

    fun String.normalize(): String {
        val original = arrayOf("á", "é", "í", "ó", "ú")
        val normalized =  arrayOf("a", "e", "i", "o", "u")

        return this.map { it ->
            val index = original.indexOf(it.toString())
            if (index >= 0) normalized[index] else it
        }.joinToString("")
    }

    fun basicResponses(_message: String): String {

        val random = (0..0).random()
        val normalizedText = _message.normalize()
        println(normalizedText)

        val message =normalizedText.toLowerCase()



        return when {

            //Flips a coin
            message.contains("lanzar") && message.contains("moneda") -> {
                val r = (0..1).random()
                val result = if (r == 0) "cara" else "cruz"

                "Lancé una moneda y aterrizó en $result"
            }

            //Math calculations
            message.contains("resolver") -> {
                val equation: String? = message.substringAfterLast("resolver")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Lo siento, no puedo resolver eso."
                }
            }

            //Horario

            //Horario ciudad jardin
            message.contains("horario de atencion") -> {
                when (random) {
                    0 -> "Ciudad Jardín\n" +
                            "Lunes a Viernes:\n" +
                            "7:00 A.M. a 5:00 P.M.\n" +
                            "Sabados:\n" +
                            "8:00 A.M a 12:00 M.D.\n" +
                            "\n" +
                            "Los Robles\n" +
                            "Lunes a Viernes:\n" +
                            "7:00 A.M. a 5:00 P.M.\n" +
                            "Sabados:\n" +
                            "8:00 A.M a 12:00 M.D.\n" +
                            "\n" +
                            "Monseñor Lezcano\n" +
                            "Lunes a Viernes:\n" +
                            "7:00 A.M. a 5:00 P.M.\n" +
                            "Sabados:\n" +
                            "8:00 A.M a 12:00 M.D.\n" +
                            "\n" +
                            "Chinandega\n" +
                            "Lunes a Viernes: \n" +
                            "7:00 A.M. a 4:00 P.M.\n" +
                            "Sabados: \n" +
                            "7:30 A.M. a 12:00 M.D.\n" +
                            "\n" +
                            "Estelí\n" +
                            "Lunes a Viernes: \n" +
                            "7:00 A.M. a 5:00 P.M.\n" +
                            "Sabados: \n" +
                            "8:00 A.M a 12:00 M.D.\n" +
                            "\n" +
                            "Juigalpa\n" +
                            "Lunes a Viernes: \n" +
                            "8:00 A.M. a 5:00 P.M.\n" +
                            "Sabados: \n" +
                            "8:00 A.M a 12:00 M.D.\n" +
                            "\n" +
                            "Masaya\n" +
                            "Lunes a Viernes: \n" +
                            "7:00 A.M. a 4:00 P.M.\n" +
                            "Sabados: \n" +
                            "7:00 A.M a 12:00 M.D.\n" +
                            "\n" +
                            "Matagalpa\n" +
                            "Lunes a Viernes: \n" +
                            "7:00 A.M. a 5:00 P.M.\n" +
                            "Sabados: \n" +
                            "7:00 A.M a 12:00 M.D.\n" +
                            "\n" +
                            "Rivas\n" +
                            "Lunes a Viernes: \n" +
                            "7:00 A.M. a 5:00 P.M.\n" +
                            "Sabados: \n" +
                            "8:00 A.M a 12:00 M.D."
                    else -> "error"
                }
            }

            //Mi clinica direccin

            message.contains("clinica de profamilia") -> {
                when (random) {
                    0 -> "Ciudad Jardín\n" +
                            "De la gasolinera UNO Ciudad Jardín 70 vrs arriba, Ciudad Jardín, Managua.\n" +
                            "Tel: 2249-1095 & 2240-0322\n" +
                            "\n" +
                            "Los Robles\n" +
                            "De la Gasolinera UNO Plaza el Sol, una cuadra al sur, Los Robles, Managua.\n" +
                            "Tel: 2278-0805\n" +
                            "\n" +
                            "Monseñor Lezcano\n" +
                            "Estatua Monseñor Lezcano 3c. al lago 2c. abajo, Monseñor Lezcano, Managua.\n" +
                            "Tel: 2268-7680 & 2268-7681\n" +
                            "\n" +
                            "Chinandega\n" +
                            "De donde fue Hotel Glomar media cuadra abajo, Chinandega.\n" +
                            "Tel: 2341-2824\n" +
                            "\n" +
                            "Estelí\n" +
                            "Costado Sur Colegio Nuestra Sra del Rosario 2c. al Este. Estelí.\n" +
                            "Tel: 2713-7799\n" +
                            "\n" +
                            "Juigalpa\n" +
                            "Del zoológico 1 c. al oeste, Juigalpa\n" +
                            "Tel: 2512-2918\n" +
                            "\n" +
                            "Masaya\n" +
                            "Frente a la entrada a El Coyotepe, Masaya.\n" +
                            "Tel: 2522-4777\n" +
                            "\n" +
                            "Matagalpa\n" +
                            "Costado este, puente Colegio Santa Teresita, Matagalpa.\n" +
                            "Tel: 2772-3463 & 2772-7124\n" +
                            "\n" +
                            "Rivas\n" +
                            "Rotonda Jesús del Rescate 200mts al Norte, Rivas.\n" +
                            "Tel: 2563-3120"
                    else -> "error"
                }
            }

            //LLamar a profamilia
            message.contains("llamar a profamilia") -> {
                when (random) {
                    0 -> "Contactenos:\n" +
                            "\n" +
                            "Telefono: 2270-1531"
                    else -> "error"
                }
            }

            //Hello
            message.contains("hola") -> {
                when (random) {
                    0 -> "Hola puedo ayudarte en algo ?"
                    else -> "error" }
            }

            //How are you?
            message.contains("como estas") -> {
                when (random) {
                    0 -> "¡Bastante bien! ¿Qué hay de tí?"
                    else -> "error"
                }
            }

            //What time is it?
            message.contains("time") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Open Google
            message.contains("abrir") && message.contains("google")-> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("buscar")-> {
                OPEN_SEARCH
            }

            //When the programme doesn't understand...
            else -> {
                val randomUnderstand = (0..2).random()
                when (randomUnderstand) {
                    0 -> "No entiendo..."
                    1 -> "Intenta preguntarme algo diferente"
                    2 -> "No sé"
                    else -> "error"
                }
            }
        }
    }
}