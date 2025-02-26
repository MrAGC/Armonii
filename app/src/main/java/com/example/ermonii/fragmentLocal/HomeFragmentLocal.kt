package com.example.ermonii.fragmentLocal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ermonii.R
import com.example.ermonii.clases.Musico
import com.example.ermonii.fragmentMusico.EventoAdapter
import java.util.Date

class HomeFragmentLocal : Fragment() {
    private lateinit var adapter: MusicoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        val view = inflater.inflate(R.layout.fragment_home_local, container, false)

        // Datos de ejemplo
        val musicosList = listOf(
            Musico(
                1,
                "Carlos",
                "carlos@musica.com",
                "claveSegura1",
                600111222L,
                40.4168f,
                -3.7038f,
                Date(1643673600000),
                true,
                emptyList(),
                4.7f,
                "Santana",
                "",  // Nuevo campo apodo
                28,
                "Guitarrista profesional con 10 años de experiencia",
                "Masculino",
                listOf("Rock", "Blues"),
                emptyList(),
                "El Guitarra",
                R.drawable.profile_icon
                  ),
            Musico(
                2,
                "Laura",
                "laura@musica.com",
                "securePass2",
                644333444L,
                41.3851f,
                2.1734f,
                Date(1646092800000),
                false,
                emptyList(),
                4.9f,
                "Villalba",
                "Lauris",  // Apodo
                24,
                "Baterista versátil en múltiples géneros",
                "Femenino",
                listOf("Jazz", "Funk"),
                emptyList(),
                "La Batería",
                R.drawable.profile_icon
                  ),
            Musico(
                3,
                "José",
                "jose@musica.com",
                "claveSegura3",
                665544332L,
                19.4326f,
                -99.1332f,
                Date(1648771200000),
                true,
                emptyList(),
                4.5f,
                "Pérez",
                "",  // Apodo
                30,
                "Tecladista con 8 años de trayectoria",
                "Masculino",
                listOf("Pop", "Rock"),
                emptyList(),
                "El Tecladista",
                R.drawable.profile_icon
                  ),
            Musico(
                4,
                "Ana",
                "ana@musica.com",
                "claveSegura4",
                555666777L,
                34.0522f,
                -118.2437f,
                Date(1651363200000),
                false,
                emptyList(),
                4.8f,
                "Sánchez",
                "Anita",  // Apodo
                27,
                "Vocalista con experiencia en teatro musical",
                "Femenino",
                listOf("Balada", "Soul"),
                emptyList(),
                "La Voz",
                R.drawable.profile_icon
                  ),
            Musico(
                5,
                "Diego",
                "diego@musica.com",
                "claveSegura5",
                777888999L,
                51.5074f,
                -0.1278f,
                Date(1654041600000),
                true,
                emptyList(),
                4.6f,
                "Ramírez",
                "Dieguito",  // Apodo
                35,
                "Bajista con enfoque en música alternativa",
                "Masculino",
                listOf("Alternativa", "Indie"),
                emptyList(),
                "El Bajo",
                R.drawable.profile_icon
                  ),
            Musico(
                6,
                "Sofía",
                "sofia@musica.com",
                "claveSegura6",
                999000111L,
                48.8566f,
                2.3522f,
                Date(1656633600000),
                false,
                emptyList(),
                4.9f,
                "López",
                "Sofi",  // Apodo
                22,
                "Violinista con pasión por la música clásica",
                "Femenino",
                listOf("Clásica", "Celta"),
                emptyList(),
                "La Violinista",
                R.drawable.profile_icon
                  ),
            Musico(
                7,
                "Ricardo",
                "ricardo@musica.com",
                "claveSegura7",
                222333444L,
                40.7306f,
                -73.9352f,
                Date(1659312000000),
                true,
                emptyList(),
                4.4f,
                "González",
                "Richie",  // Apodo
                29,
                "Percusionista especializado en música latina",
                "Masculino",
                listOf("Salsa", "Merengue"),
                emptyList(),
                "El Percusionista",
                R.drawable.profile_icon
                  ),
            Musico(
                8,
                "Valentina",
                "valentina@musica.com",
                "claveSegura8",
                888777666L,
                34.0522f,
                -118.2437f,
                Date(1661990400000),
                false,
                emptyList(),
                4.9f,
                "Martínez",
                "Vale",  // Apodo
                26,
                "Guitarrista con enfoque en rock alternativo",
                "Femenino",
                listOf("Rock Alternativo", "Folk"),
                emptyList(),
                "La Guitarrista",
                R.drawable.profile_icon
                  ),
            Musico(
                9,
                "Fernando",
                "fernando@musica.com",
                "claveSegura9",
                444555666L,
                37.7749f,
                -122.4194f,
                Date(1664582400000),
                true,
                emptyList(),
                4.6f,
                "Torres",
                "Fer",  // Apodo
                32,
                "Cantante con influencia en rock y pop",
                "Masculino",
                listOf("Rock", "Pop"),
                emptyList(),
                "El Cantante",
                R.drawable.profile_icon
                  ),
            Musico(
                10,
                "María",
                "maria@musica.com",
                "claveSegura10",
                333222111L,
                39.9042f,
                116.4074f,
                Date(1667260800000),
                false,
                emptyList(),
                4.8f,
                "Cruz",
                "Mari",  // Apodo
                25,
                "Flautista con amor por la música contemporánea",
                "Femenino",
                listOf("Contemporánea", "Clásica"),
                emptyList(),
                "La Flautista",
                R.drawable.profile_icon
                  ),
            Musico(
                11,
                "Cristian",
                "cristian@musica.com",
                "claveSegura11",
                555888999,
                34.0522f,
                -118.2437f,
                Date(1669843200000),
                true,
                emptyList(),
                4.6f,
                "Hernández",
                "Cris",  // Apodo
                31,
                "Saxofonista con experiencia en jazz moderno",
                "Masculino",
                listOf("Jazz", "Funk"),
                emptyList(),
                "El Saxo",
                R.drawable.profile_icon
                  ),
            Musico(
                12,
                "Isabella",
                "isabella@musica.com",
                "claveSegura12",
                444111222,
                48.8566f,
                2.3522f,
                Date(1672521600000),
                false,
                emptyList(),
                4.9f,
                "González",
                "Bella",  // Apodo
                28,
                "Cellista con enfoque en música barroca",
                "Femenino",
                listOf("Clásica", "Barroca"),
                emptyList(),
                "La Chelista",
                R.drawable.profile_icon
                  ),
            Musico(
                13,
                "Álvaro",
                "alvaro@musica.com",
                "claveSegura13",
                222555333,
                19.4326f,
                -99.1332f,
                Date(1675200000000),
                true,
                emptyList(),
                4.7f,
                "Méndez",
                "Alvi",  // Apodo
                33,
                "Guitarrista de música folclórica mexicana",
                "Masculino",
                listOf("Folclor", "Tradicional"),
                emptyList(),
                "El Folklorista",
                R.drawable.profile_icon
                  ),
            Musico(
                14,
                "Camila",
                "camila@musica.com",
                "claveSegura14",
                777333444,
                40.4168f,
                -3.7038f,
                Date(1677782400000),
                false,
                emptyList(),
                4.8f,
                "Torres",
                "Cami",  // Apodo
                26,
                "Violinista en orquesta sinfónica",
                "Femenino",
                listOf("Clásica", "Sinfónica"),
                emptyList(),
                "La Sinfonista",
                R.drawable.profile_icon
                  ),
            Musico(
                15,
                "Emilio",
                "emilio@musica.com",
                "claveSegura15",
                888777666,
                51.5074f,
                -0.1278f,
                Date(1680364800000),
                true,
                emptyList(),
                4.5f,
                "Cáceres",
                "Emi",  // Apodo
                34,
                "Cantante de música latina con ritmos urbanos",
                "Masculino",
                listOf("Urbano", "Latino"),
                emptyList(),
                "El Urbano",
                R.drawable.profile_icon
                  ),
            Musico(
                16,
                "Lucía",
                "lucia@musica.com",
                "claveSegura16",
                666999444,
                39.9042f,
                116.4074f,
                Date(1683043200000),
                false,
                emptyList(),
                4.9f,
                "Salazar",
                "Luce",  // Apodo
                24,
                "Cantautora con letras poéticas",
                "Femenino",
                listOf("Folk", "Indie"),
                emptyList(),
                "La Cantautora",
                R.drawable.profile_icon
                  ),
            Musico(
                17,
                "Santiago",
                "santiago@musica.com",
                "claveSegura17",
                999000111,
                34.0522f,
                -118.2437f,
                Date(1685721600000),
                true,
                emptyList(),
                4.4f,
                "Jiménez",
                "Santi",  // Apodo
                29,
                "Bajista en banda de rock progresivo",
                "Masculino",
                listOf("Rock", "Progresivo"),
                emptyList(),
                "El Progresivo",
                R.drawable.profile_icon
                  ),
            Musico(
                18,
                "Marta",
                "marta@musica.com",
                "claveSegura18",
                555444333,
                40.4168f,
                -3.7038f,
                Date(1688400000000),
                false,
                emptyList(),
                4.8f,
                "Vega",
                "Marti",  // Apodo
                30,
                "Baterista de rock con experiencia en giras",
                "Femenino",
                listOf("Rock", "Pop"),
                emptyList(),
                "La Batería",
                R.drawable.profile_icon
                  ),
            Musico(
                19,
                "Gabriel",
                "gabriel@musica.com",
                "claveSegura19",
                333222111,
                34.0522f,
                -118.2437f,
                Date(1691088000000),
                true,
                emptyList(),
                4.6f,
                "Pérez",
                "Gabo",  // Apodo
                35,
                "Guitarrista de heavy metal",
                "Masculino",
                listOf("Metal", "Rock"),
                emptyList(),
                "El Metálico",
                R.drawable.profile_icon
                  ),
            Musico(
                20,
                "Valeria",
                "valeria@musica.com",
                "claveSegura20",
                222111000,
                51.5074f,
                -0.1278f,
                Date(1693766400000),
                false,
                emptyList(),
                4.9f,
                "Montalvo",
                "Vale",  // Apodo
                23,
                "Violinista en grupo de música contemporánea",
                "Femenino",
                listOf("Contemporánea", "Experimental"),
                emptyList(),
                "La Experimental",
                R.drawable.profile_icon
                  )

                                )

        // Configurar el RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewMusicos)
        adapter = MusicoAdapter(musicosList) // Usa tu lista existente

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        return view
    }
}