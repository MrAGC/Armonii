import java.time.LocalDateTime

data class Chat(
    val idChat: Int,
    val mensajes: List<Mensaje>,
    val idMusico: Int,
    val idLocal: Int
               )
