package com.example.ermonii.fragmentLocal

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ermonii.R
import com.example.ermonii.clases.Evento
import com.example.ermonii.clases.Local
import com.example.ermonii.clases.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CrearEventoFragmentLocal : Fragment() {

    private var usuarioId: Int = -1
    private var localUsuario: Local? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioId = arguments?.getInt("usuarioId") ?: -1
    }

    companion object {
        fun newInstance(usuarioId: Int): CrearEventoFragmentLocal {
            val fragment = CrearEventoFragmentLocal()
            val args = Bundle()
            args.putInt("usuarioId", usuarioId)
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("MissingInflatedId", "DefaultLocale")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {

        val view = inflater.inflate(R.layout.fragment_crear_local, container, false)

        val edtNombre = view.findViewById<EditText>(R.id.edtNombre)
        val edtFecha = view.findViewById<EditText>(R.id.edtFecha)
        val edtHora = view.findViewById<EditText>(R.id.edtHora)
        val edtDescripcion = view.findViewById<EditText>(R.id.edtDescripcion)
        val edtDuracion = view.findViewById<EditText>(R.id.edtDuracion)
        val btnCrearEvento = view.findViewById<Button>(R.id.btnCrearEvento)


        // Fecha
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        edtFecha.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = String.format(
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                                                    )
                    edtFecha.setText(selectedDate)
                }, year, month, day)
            datePickerDialog.show()
        }

        // Hora
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        edtHora.setOnClickListener {
            val timePickerDialog =
                TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                    // Formato de 24 horas
                    val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    edtHora.setText(selectedTime)
                }, hour, minute, true)  // 'true' para formato de 24 horas
            timePickerDialog.show()
        }

        // Progragmatica para crear evento
        btnCrearEvento.setOnClickListener {
            if (edtNombre.text.toString() == "") {
                edtNombre.setBackgroundResource(R.drawable.redondear_edittext_error)
                edtFecha.setBackgroundResource(R.drawable.redondear_edittext)
                edtHora.setBackgroundResource(R.drawable.redondear_edittext)
                edtDescripcion.setBackgroundResource(R.drawable.redondear_edittext)
                edtDuracion.setBackgroundResource(R.drawable.redondear_edittext)

                Toast.makeText(requireContext(), "Inserta un nombre válido", Toast.LENGTH_SHORT)
                    .show()
            } else if (edtFecha.text.toString() == "") {
                edtNombre.setBackgroundResource(R.drawable.redondear_edittext)
                edtFecha.setBackgroundResource(R.drawable.redondear_edittext_error)
                edtHora.setBackgroundResource(R.drawable.redondear_edittext)
                edtDescripcion.setBackgroundResource(R.drawable.redondear_edittext)
                edtDuracion.setBackgroundResource(R.drawable.redondear_edittext)

                Toast.makeText(requireContext(), "Inserta una fecha válida", Toast.LENGTH_SHORT)
                    .show()
            } else if (edtHora.text.toString() == "") {
                edtNombre.setBackgroundResource(R.drawable.redondear_edittext)
                edtFecha.setBackgroundResource(R.drawable.redondear_edittext)
                edtHora.setBackgroundResource(R.drawable.redondear_edittext_error)
                edtDescripcion.setBackgroundResource(R.drawable.redondear_edittext)
                edtDuracion.setBackgroundResource(R.drawable.redondear_edittext)

                Toast.makeText(requireContext(), "Inserta una hora válida", Toast.LENGTH_SHORT)
                    .show()
            } else if (edtDescripcion.text.toString() == "") {
                edtNombre.setBackgroundResource(R.drawable.redondear_edittext)
                edtFecha.setBackgroundResource(R.drawable.redondear_edittext)
                edtHora.setBackgroundResource(R.drawable.redondear_edittext)
                edtDescripcion.setBackgroundResource(R.drawable.redondear_edittext_error)
                edtDuracion.setBackgroundResource(R.drawable.redondear_edittext)

                Toast.makeText(requireContext(), "Inserta una fecha válida", Toast.LENGTH_SHORT)
                    .show()
            } else if (edtDuracion.text.toString() == "") {
                edtNombre.setBackgroundResource(R.drawable.redondear_edittext)
                edtFecha.setBackgroundResource(R.drawable.redondear_edittext)
                edtHora.setBackgroundResource(R.drawable.redondear_edittext)
                edtDescripcion.setBackgroundResource(R.drawable.redondear_edittext)
                edtDuracion.setBackgroundResource(R.drawable.redondear_edittext_error)

                Toast.makeText(requireContext(), "Inserta una fecha válida", Toast.LENGTH_SHORT)
                    .show()
            } else {
                edtNombre.setBackgroundResource(R.drawable.redondear_edittext)
                edtFecha.setBackgroundResource(R.drawable.redondear_edittext)
                edtHora.setBackgroundResource(R.drawable.redondear_edittext)
                edtDescripcion.setBackgroundResource(R.drawable.redondear_edittext)
                edtDuracion.setBackgroundResource(R.drawable.redondear_edittext)


                val fechaString = edtFecha.text.toString()
                val horaString = edtHora.text.toString()

                // Concatenamos la fecha y hora en el formato "yyyy-MM-ddTHH:mm:ss"
                val fechaHoraFinal = fechaString + "T" + horaString + ":00"


                // Llamamos la API dentro de una coroutine
                GlobalScope.launch(Dispatchers.Main) {
                    val locales = withContext(Dispatchers.IO) {
                        llamarAPILocales()
                    }

                    Log.d("PerfilLocal", "Locales obtenidos: ${locales}")

                    val localEncontrado = locales.find { it.idUsuario == usuarioId }

                    if (locales.isNotEmpty()) {
                        localUsuario = localEncontrado

                    } else {
                        Log.e("PerfilLocal", "No se encontró local para usuarioId: $usuarioId")
                        Toast.makeText(
                            requireContext(),
                            "No se encontró el local",
                            Toast.LENGTH_SHORT
                                      ).show()
                    }
                }

                // HAY QUE CORREGIR EL ID DE LOCAL
                val eventoNuevo = localUsuario?.let { it1 ->
                    Evento(
                        0,
                        edtNombre.text.toString().trim(),
                        fechaHoraFinal,
                        edtDescripcion.text.toString().trim(),
                        it1.id,
                        null,
                        true,
                        edtDuracion.text.toString().toInt()
                          )
                }

                if (eventoNuevo != null) {
                    Log.d(
                        "API_EventoNuevo", """
                                    ===== EVENTO =====
                                    ID: ${eventoNuevo.id}
                                    Nombre: ${eventoNuevo.nombre}
                                    Local: ${eventoNuevo.idLocal}
                                    Musico: ${eventoNuevo.idMusico}
                                    Descripcion: ${eventoNuevo.descripcion}
                                    Fecha: ${eventoNuevo.fecha}
                                    Duracion: ${eventoNuevo.duracion}                
                                    """.trimIndent()
                         )
                }


                if (eventoNuevo != null) {
                    RetrofitClient.instance.postEvento(eventoNuevo).enqueue(object : Callback<Boolean> {
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    requireContext(), "Creado correctamente!", Toast.LENGTH_SHORT
                                              ).show()

                                // Reseteamos los campos
                                edtNombre.setText("")
                                edtFecha.setText("")
                                edtHora.setText("")
                                edtDescripcion.setText("")
                                edtDuracion.setText("")

                                Log.d(
                                    "API_RESPONSE", "Evento registrado correctamente: ${response.body()}"
                                     )
                            } else {
                                Log.e(
                                    "API_ERROR", "Error al registrar el evento: ${response.errorBody()?.string()}"
                                     )
                            }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            Log.e("API_FAILURE", "Fallo en la conexión: ${t.message}")
                        }
                    })
                }
            }
        }





        return view
    }

    private suspend fun llamarAPILocales(): List<Local> {
        return try {
            RetrofitClient.instance.getLocales()  // ✅ Directamente devuelve la lista
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error al obtener locales", e)
            emptyList()
        }
    }
}
