package cr.ac.utn.beekeepersnotebook

import Controller.BeehiveController
import Controller.HarvestController
import Controller.QueenController
import Entity.Beehive
import Entity.Queen
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BeehiveDetailActivity : AppCompatActivity() {
    private lateinit var tvName: TextView
    private lateinit var tvBoxType: TextView
    private lateinit var tvQueenInfo: TextView
    private lateinit var btnQueens: Button
    private lateinit var btnHarvests: Button
    private lateinit var tvTotalHarvest: TextView
    private lateinit var spQueen: Spinner
    private lateinit var btnSaveChanges: Button
    private lateinit var beehiveController: BeehiveController
    private lateinit var queenController: QueenController
    private lateinit var harvestController: HarvestController
    private var hiveId: String = ""
    private var currentHive: Beehive? = null
    private var oldQueenId: String? = null
    private var availableQueens: List<Queen> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_beehive_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        beehiveController = BeehiveController(this)
        queenController = QueenController(this)
        harvestController = HarvestController(this)

        tvName = findViewById(R.id.tvHiveName)
        tvBoxType = findViewById(R.id.tvHiveBoxType)
        tvTotalHarvest = findViewById(R.id.tvTotalHarvest)
        spQueen = findViewById(R.id.spQueenDetail)
        btnSaveChanges = findViewById(R.id.btnSaveQueenChanges)
        hiveId = intent.getStringExtra("HIVE_ID") ?: ""

        if (hiveId.isEmpty()) {
            Toast.makeText(this, "ID de colmena no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        loadHiveData()

        btnSaveChanges.setOnClickListener {
            saveQueenChanges()
        }
    }
    private fun loadHiveData() {
        beehiveController.getById(hiveId) { hive ->
            if (hive == null) {
                Toast.makeText(this, "No se encontró la colmena", Toast.LENGTH_LONG).show()
                finish()
            } else {
                currentHive = hive
                oldQueenId = hive.QueenID

                tvName.text = hive.Name
                tvBoxType.text = "Caja: ${hive.BoxType}"

                // Después de tener la colmena, cargamos las reinas para el spinner
                loadQueensForSpinner()
            }


        }
    }


    private fun loadQueensForSpinner() {
        queenController.getAll { list ->

            availableQueens = list.filter { q ->
                q.HiveID.isEmpty() || q.HiveID == hiveId
            }

            val labels = mutableListOf<String>()
            labels.add("Sin reina asignada")
            labels.addAll(
                availableQueens.map { q -> "${q.Type} - ${q.getEntryDateAsString()}" }
            )

            val spinAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                labels
            )
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spQueen.adapter = spinAdapter

            val currentId = oldQueenId
            if (!currentId.isNullOrEmpty()) {
                val idx = availableQueens.indexOfFirst { it.ID == currentId }
                if (idx >= 0) {
                    spQueen.setSelection(idx + 1) // +1 por "Sin reina asignada"
                }
            }
        }
    }
    private fun loadTotalHarvest() {
        if (hiveId.isEmpty()) return

        harvestController.getAll { list ->
            // puedes sumar bruto o neto, yo uso neto:
            val totalNeto = list
                .filter { it.BeehiveID == hiveId }
                .sumOf { it.HoneyAmountKgNetaHarvest }

            runOnUiThread {
                tvTotalHarvest.text = String.format("%.2f Kg", totalNeto)
            }
        }
    }
    private fun saveQueenChanges() {
        val hive = currentHive
        if (hive == null) {
            Toast.makeText(this, "Colmena no cargada aún", Toast.LENGTH_SHORT).show()
            return
        }

        val previousQueenId = oldQueenId

        val selectedPos = spQueen.selectedItemPosition
        val selectedQueen: Queen? =
            if (selectedPos > 0 && availableQueens.isNotEmpty())
                availableQueens[selectedPos - 1]
            else
                null

        // Actualizamos la colmena con la nueva reina
        hive.QueenID = selectedQueen?.ID ?: ""

        beehiveController.updateBeehive(hive) { ok, msg ->
            if (!ok) {
                Toast.makeText(
                    this,
                    msg ?: "Error al actualizar la colmena",
                    Toast.LENGTH_LONG
                ).show()
                return@updateBeehive
            }

            // 1) Liberar la reina anterior si cambió
            if (!previousQueenId.isNullOrEmpty() &&
                previousQueenId != selectedQueen?.ID
            ) {
                queenController.getById(previousQueenId) { oldQueen ->
                    if (oldQueen != null) {
                        oldQueen.HiveID = ""
                        queenController.updateQueen(oldQueen) { _, _ -> }
                    }
                }
            }

            // 2) Asignar la nueva reina (ponerle HiveID = id de esta colmena)
            selectedQueen?.let { q ->
                q.HiveID = hive.ID
                queenController.updateQueen(q) { _, _ -> }
            }

            oldQueenId = hive.QueenID

            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
        }
    }
}