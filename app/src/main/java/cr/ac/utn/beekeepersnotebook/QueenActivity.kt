package cr.ac.utn.beekeepersnotebook

import Adapter.QueenAdapter
import Controller.QueenController
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import Entity.Queen
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class QueenActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView

    private lateinit var btnAddQueen: Button
    private lateinit var adapter: QueenAdapter
    private lateinit var controller: QueenController
    private var zoneId: String = ""
    private var zoneName: String = ""

    private val queens = mutableListOf<Queen>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_queen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // ID y nombre de la zona que viene desde ZoneActivity
        zoneId = intent.getStringExtra("ZONE_ID") ?: ""
        zoneName = intent.getStringExtra("ZONE_NAME") ?: ""

        title = "Colmenas - $zoneName"

        // Si algún día se necesita saber desde qué colmena entré, podrías leer HIVE_ID aquí,
        // pero para registrar reinas "globales" no lo necesitamos.
        // val beehiveId = intent.getStringExtra("HIVE_ID") ?: ""

        controller = QueenController(this)

        recycler = findViewById(R.id.recyclerQueens)
        btnAddQueen = findViewById(R.id.btnAddQueen)

        adapter = QueenAdapter(
            queens,
            onItemClick = { queen -> editQueenDialog(queen) },
            onItemLongClick = { queen -> deleteQueen(queen) }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnAddQueen.setOnClickListener { addQueenDialog() }

        loadQueens()
    }

    // =========================
    // Cargar TODAS las reinas (o filtrar por usuario si querés)
    // =========================
    private fun loadQueens() {
        controller.getAll { list ->
            queens.clear()

            // Aquí podrías filtrar por usuario si tus reinas tienen PersonID o ZoneID.
            // De momento las cargamos todas:
            queens.addAll(list.filter { it.ZoneID == zoneId })
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Reina cargado: ${queens.size} items", Toast.LENGTH_SHORT).show()

        }
    }


    // =========================
    // Nueva reina (queda LIBRE: HiveID = "")
    // =========================
    private fun addQueenDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_queen, null)
        val txtType = view.findViewById<EditText>(R.id.txtQueenType)
        val txtDate = view.findViewById<EditText>(R.id.txtQueenDate)

        AlertDialog.Builder(this)
            .setTitle("Nueva reina")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val type = txtType.text.toString().trim()
                val date = txtDate.text.toString().trim()

                if (type.isEmpty() || date.isEmpty()) {
                    Toast.makeText(this, "Complete los datos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val q = Queen().apply {
                    Type = type
                    HiveID = ""
                    ZoneID = zoneId
                    setEntryDateFromString(date)
                }

                controller.addQueen(q) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Reina guardada", Toast.LENGTH_SHORT).show()
                        loadQueens()
                    } else {
                        Toast.makeText(this, msg ?: "Error al guardar reina", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // =========================
    // Editar reina (solo tipo y fecha)
    // =========================
    private fun editQueenDialog(queen: Queen) {
        val view = layoutInflater.inflate(R.layout.dialog_queen, null)
        val txtType = view.findViewById<EditText>(R.id.txtQueenType)
        val txtDate = view.findViewById<EditText>(R.id.txtQueenDate)

        txtType.setText(queen.Type)
        txtDate.setText(queen.getEntryDateAsString())

        AlertDialog.Builder(this)
            .setTitle("Editar reina")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val type = txtType.text.toString().trim()
                val date = txtDate.text.toString().trim()

                if (type.isEmpty() || date.isEmpty()) {
                    Toast.makeText(this, "Complete los datos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                queen.Type = type
                queen.setEntryDateFromString(date)

                controller.updateQueen(queen) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Reina actualizada", Toast.LENGTH_SHORT).show()
                        loadQueens()
                    } else {
                        Toast.makeText(this, msg ?: "Error al actualizar reina", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // =========================
    // Eliminar reina
    // =========================
    private fun deleteQueen(queen: Queen) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar reina")
            .setMessage("¿Desea eliminar la reina ${queen.Type}?")
            .setPositiveButton("Eliminar") { _, _ ->
                controller.deleteQueen(queen.ID) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Reina eliminada", Toast.LENGTH_SHORT).show()
                        loadQueens()
                    } else {
                        Toast.makeText(this, msg ?: "Error al eliminar reina", Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}