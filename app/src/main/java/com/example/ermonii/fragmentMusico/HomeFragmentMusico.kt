package com.example.ermonii.fragmentMusico

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.clases.Evento
import com.example.ermonii.clases.Local
import com.example.ermonii.R
import java.time.LocalDateTime

class HomeFragmentMusico : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        // Inflar la vista del fragmento (lo que representa el layout)
        val view = inflater.inflate(R.layout.fragment_home_musico, container, false)


       /* val localList = listOf(
            Local(
                1,
                "Restaurante A",
                "restaurantea@example.com",
                "password123",
                "1234567890",
                40.7128,
                -74.0060,
                LocalDateTime.now(),  // Asigna la fecha y hora actuales
                true,
                emptyList(),
                4.5,
                "Comida italiana deliciosa",
                R.drawable.profile_icon,
                40.7128,
                -74.0060,
                5
                 ),
            Local(
                2,
                "Restaurante B",
                "restauranteb@example.com",
                "password123",
                "1234567899",
                40.7128,
                -74.0060,
                LocalDateTime.now(),
                true,
                emptyList(),
                4.2,
                "Comida mexicana auténtica",
                R.drawable.profile_icon,
                40.7128,
                -74.0060,
                4
                 ),
            Local(
                3,
                "Café Central",
                "cafecentral@example.com",
                "password123",
                "1234567899",
                34.0522,
                -118.2437,
                LocalDateTime.now(),
                true,
                emptyList(),
                4.7,
                "Café y postres caseros",
                R.drawable.profile_icon,
                34.0522,
                -118.2437,
                5
                 ),
            Local(
                4,
                "Pizzería Napoli",
                "pizzerianapoli@example.com",
                "password123",
                "1234567899",
                41.9028,
                12.4964,
                LocalDateTime.now(),
                true,
                emptyList(),
                4.9,
                "Pizza al estilo de Nápoles",
                R.drawable.profile_icon,
                41.9028,
                12.4964,
                5
                 ),
            Local(
                5,
                "Taco Bell",
                "tacobell@example.com",
                "password123",
                "1234567899",
                36.1627,
                -86.7816,
                LocalDateTime.now(),
                true,
                emptyList(),
                3.8,
                "Tacos y burritos rápidos",
                R.drawable.profile_icon,
                36.1627,
                -86.7816,
                3
                 ),
            Local(
                6,
                "La Taquería",
                "lataqueria@example.com",
                "password123",
                "1234567899",
                19.4326,
                -99.1332,
                LocalDateTime.now(),
                true,
                emptyList(),
                5.0,
                "Tacos auténticos mexicanos",
                R.drawable.profile_icon,
                19.4326,
                -99.1332,
                5
                 ),
            Local(
                7,
                "Restaurante Sushi King",
                "sushiking@example.com",
                "password123",
                "1234567899",
                35.6762,
                139.6503,
                LocalDateTime.now(),
                true,
                emptyList(),
                4.6,
                "Sushi fresco y delicioso",
                R.drawable.profile_icon,
                35.6762,
                139.6503,
                4
                 ),
            Local(
                8,
                "Burguer House",
                "burguerhouse@example.com",
                "password123",
                "1234567899",
                48.8566,
                2.3522,
                LocalDateTime.now(),
                true,
                emptyList(),
                4.4,
                "Hamburguesas gourmet",
                R.drawable.profile_icon,
                48.8566,
                2.3522,
                4
                 ),
            Local(
                9,
                "Café de París",
                "cafedeparis@example.com",
                "password123",
                "1234567899",
                48.8566,
                2.3522,
                LocalDateTime.now(),
                true,
                emptyList(),
                5.0,
                "Café y pasteles franceses",
                R.drawable.profile_icon,
                48.8566,
                2.3522,
                5
                 ),
            Local(
                10,
                "Panadería El Horno",
                "panaderiaelhorn@example.com",
                "password123",
                "1234567899",
                40.7306,
                -73.9352,
                LocalDateTime.now(),
                true,
                emptyList(),
                4.3,
                "Pan recién horneado",
                R.drawable.profile_icon,
                40.7306,
                -73.9352,
                4
                 )
                              )


        val eventList = listOf(
            Evento(
                id = 1,
                name = "Cena Italiana",
                date = "2025-02-25", // Fecha del evento
                description = "Una noche de pasta y vino con música en vivo",
                local = localList[0], // Relacionado con el primer local de la lista de locales
                music = null,
                estado = true,  // El evento está disponible
                duration = 180 // Duración en minutos (3 horas)
                  ), Evento(
                id = 2,
                name = "Noche Mexicana",
                date = "2025-03-01",
                description = "Fiesta mexicana con mariachi y comida típica",
                local = localList[1],  // Relacionado con el segundo local de la lista de locales
                music = null,
                estado = true,
                duration = 240 // Duración en minutos (4 horas)
                           ), Evento(
                id = 3,
                name = "Café y Música",
                date = "2025-02-28",
                description = "Café y postres con música en vivo para disfrutar",
                local = localList[2],  // Relacionado con el tercer local
                music = null,
                estado = true,
                duration = 120 // Duración en minutos (2 horas)
                                    ), Evento(
                id = 4,
                name = "Noche Napolitana",
                date = "2025-04-10",
                description = "Pizza auténtica napolitana con espectáculo de comedia",
                local = localList[3],  // Relacionado con el cuarto local
                music = null,
                estado = true,
                duration = 150 // Duración en minutos (2.5 horas)
                                             ), Evento(
                id = 5,
                name = "Taco Fest",
                date = "2025-03-15",
                description = "Festa de tacos con los mejores sabores de México",
                local = localList[4],  // Relacionado con el quinto local
                music = null,
                estado = true,
                duration = 180 // Duración en minutos (3 horas)
                                                      ),
            // Eventos adicionales
            Evento(
                id = 6,
                name = "Jazz Nocturno",
                date = "2025-05-01",
                description = "Una noche de jazz en vivo en un ambiente relajado",
                local = localList[5], // Relacionado con el sexto local de la lista
                music = null,
                estado = true,
                duration = 150 // Duración en minutos (2.5 horas)
                  ), Evento(
                id = 7,
                name = "Festival de Sushi",
                date = "2025-06-15",
                description = "Un festín de sushi con una variedad increíble de platos",
                local = localList[6],  // Relacionado con el séptimo local de la lista
                music = null,
                estado = true,
                duration = 180 // Duración en minutos (3 horas)
                           ), Evento(
                id = 8,
                name = "Cena Mediterránea",
                date = "2025-07-10",
                description = "Disfruta de una cena mediterránea con música suave",
                local = localList[7],  // Relacionado con el octavo local de la lista
                music = null,
                estado = true,
                duration = 180 // Duración en minutos (3 horas)
                                    ), Evento(
                id = 9,
                name = "Música Clásica",
                date = "2025-08-20",
                description = "Concierto de música clásica en vivo",
                local = localList[8],  // Relacionado con el noveno local de la lista
                music = null,
                estado = true,
                duration = 120 // Duración en minutos (2 horas)
                                             ), Evento(
                id = 10,
                name = "Fiesta del Mar",
                date = "2025-09-05",
                description = "Celebración con música de mar y comida de mariscos",
                local = localList[9],  // Relacionado con el décimo local de la lista
                music = null,
                estado = true,
                duration = 240 // Duración en minutos (4 horas)
                                                      )
                              )


        // Configurar el RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewEventos)
        recyclerView.layoutManager =
            LinearLayoutManager(context) // Usamos el contexto del fragmento
        recyclerView.adapter =
            EventoAdapter(eventList) // Pasamos la lista de objetos Local al adaptador

*/
        return view
    }
}
