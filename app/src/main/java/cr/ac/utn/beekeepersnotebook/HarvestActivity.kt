package cr.ac.utn.beekeepersnotebook

import Adapter.HarvestRecordAdpter
import Controller.BeehiveController
import Controller.HarvestController
import Entity.Beehive
import Entity.HarvestRecord
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HarvestActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var btnHarvest: Button
    private lateinit var adapter: HarvestRecordAdpter
    private lateinit var controller: HarvestController
    private lateinit var beehiveController: BeehiveController

    private val harvests = mutableListOf<HarvestRecord>()
    private val beehives = mutableListOf<Beehive>()

    private var beehiveId: String = ""
    private var zoneId: String = ""
    private var zoneName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_harvest)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Datos recibidos
        zoneId = intent.getStringExtra("ZONE_ID") ?: ""
        zoneName = intent.getStringExtra("ZONE_NAME") ?: ""
        beehiveId = intent.getStringExtra("HIVE_ID") ?: ""

        title = "Cosechas - $zoneName"

        controller = HarvestController(this)
        beehiveController = BeehiveController(this)

        recycler = findViewById(R.id.recyclerHarvests)
        btnHarvest = findViewById(R.id.btnAddHarvest)

        adapter = HarvestRecordAdpter(
            harvests,
            onItemClick = { h -> editHarvestDialog(h) },
            onItemLongClick = { h -> deleteHarvest(h) }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnHarvest.setOnClickListener { addHarvestDialog() }

        loadHarvests()
    }

    // =========================
    // Cargar cosechas de la zona
    // =========================
    private fun loadHarvests() {
        controller.getAll { list ->
            harvests.clear()

            val filtered = if (beehiveId.isNotEmpty()) {
                // Si venís desde Detalle de colmena, solo esa colmena
                list.filter { it.BeehiveID == beehiveId }
            } else {
                // Si venís desde el menú de zona, todas las colmenas de esa zona
                list.filter { it.ZoneID == zoneId }
            }

            harvests.addAll(filtered)
            adapter.notifyDataSetChanged()

            Toast.makeText(
                this,
                "Cosechas cargadas: ${harvests.size} items",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    private fun loadBeehivesForZone(onReady: () -> Unit) {
        beehiveController.getAll { list ->
            beehives.clear()

            beehives.addAll(
                list.filter { it.ZoneID == zoneId }
            )

            runOnUiThread {
                onReady()
            }
        }
    }
    // =========================
    // Agregar cosecha
    // =========================
    private fun addHarvestDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_harvest, null)
        val spBeehive = view.findViewById<Spinner>(R.id.spBeehive)
        val txtDate = view.findViewById<EditText>(R.id.txtHarvestDate)
        val txtFrames = view.findViewById<EditText>(R.id.txtHarvestFrames)
        val txtKg = view.findViewById<EditText>(R.id.txtHarvestKg)
        val txtKgNeta = view.findViewById<EditText>(R.id.txtHarvestKgNeta)

        // Primero cargamos las colmenas y luego llenamos el spinner
        loadBeehivesForZone {
            val labels = mutableListOf<String>()
            labels.add("Seleccione una colmena")
            labels.addAll(beehives.map { it.Name })

            val spinAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                labels
            )
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spBeehive.adapter = spinAdapter

            // Si venimos desde Detalle de colmena, preseleccionar
            if (beehiveId.isNotEmpty()) {
                val idx = beehives.indexOfFirst { it.ID == beehiveId }
                if (idx >= 0) {
                    spBeehive.setSelection(idx + 1) // +1 por "Seleccione una colmena"
                }
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Nueva cosecha")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val selectedPos = spBeehive.selectedItemPosition

                if (selectedPos <= 0 || beehives.isEmpty()) {
                    Toast.makeText(this, "Seleccione una colmena", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val selectedHive = beehives[selectedPos - 1]

                val date = txtDate.text.toString().trim()
                val framesStr = txtFrames.text.toString().trim()
                val kgStr = txtKg.text.toString().trim()
                val kgNetaStr = txtKgNeta.text.toString().trim()

                if (date.isEmpty() || framesStr.isEmpty() || kgStr.isEmpty() || kgNetaStr.isEmpty()) {
                    Toast.makeText(this, "Complete todos los datos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val frames = framesStr.toIntOrNull() ?: 0
                val kg = kgStr.toDoubleOrNull() ?: 0.0
                val kgNeta = kgNetaStr.toDoubleOrNull() ?: 0.0

                val harvest = HarvestRecord().apply {
                    ID = ""
                    BeehiveID = selectedHive.ID
                    ZoneID = zoneId
                    DateHarvestHoney = date
                    HoneyFramesHarvest = frames
                    HoneyAmountKgHarvest = kg
                    HoneyAmountKgNetaHarvest = kgNeta
                }

                controller.addHarvest(harvest) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Cosecha guardada", Toast.LENGTH_SHORT).show()
                        loadHarvests()
                    } else {
                        Toast.makeText(
                            this,
                            msg ?: "Error al guardar cosecha",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // =========================
    // Editar cosecha
    // =========================
    private fun editHarvestDialog(h: HarvestRecord) {
        val view = layoutInflater.inflate(R.layout.dialog_harvest, null)
        val spBeehive = view.findViewById<Spinner>(R.id.spBeehive)
        val txtDate = view.findViewById<EditText>(R.id.txtHarvestDate)
        val txtFrames = view.findViewById<EditText>(R.id.txtHarvestFrames)
        val txtKg = view.findViewById<EditText>(R.id.txtHarvestKg)
        val txtKgNeta = view.findViewById<EditText>(R.id.txtHarvestKgNeta)

        txtDate.setText(h.DateHarvestHoney)
        txtFrames.setText(h.HoneyFramesHarvest.toString())
        txtKg.setText(h.HoneyAmountKgHarvest.toString())
        txtKgNeta.setText(h.HoneyAmountKgNetaHarvest.toString())

        // Cargamos colmenas para el spinner y preseleccionamos la del registro
        loadBeehivesForZone {
            val labels = mutableListOf<String>()
            labels.add("Seleccione una colmena")
            labels.addAll(beehives.map { it.Name })

            val spinAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                labels
            )
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spBeehive.adapter = spinAdapter

            val idx = beehives.indexOfFirst { it.ID == h.BeehiveID }
            if (idx >= 0) {
                spBeehive.setSelection(idx + 1)
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Editar cosecha")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val selectedPos = spBeehive.selectedItemPosition
                if (selectedPos <= 0 || beehives.isEmpty()) {
                    Toast.makeText(this, "Seleccione una colmena", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val selectedHive = beehives[selectedPos - 1]

                val date = txtDate.text.toString().trim()
                val framesStr = txtFrames.text.toString().trim()
                val kgStr = txtKg.text.toString().trim()
                val kgNetaStr = txtKgNeta.text.toString().trim()

                if (date.isEmpty() || framesStr.isEmpty() || kgStr.isEmpty() || kgNetaStr.isEmpty()) {
                    Toast.makeText(this, "Complete todos los datos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                h.BeehiveID = selectedHive.ID
                h.DateHarvestHoney = date
                h.HoneyFramesHarvest = framesStr.toIntOrNull() ?: 0
                h.HoneyAmountKgHarvest = kgStr.toDoubleOrNull() ?: 0.0
                h.HoneyAmountKgNetaHarvest = kgNetaStr.toDoubleOrNull() ?: 0.0

                controller.updateHarvest(h) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Cosecha actualizada", Toast.LENGTH_SHORT).show()
                        loadHarvests()
                    } else {
                        Toast.makeText(
                            this,
                            msg ?: "Error al actualizar cosecha",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // =========================
    // Eliminar cosecha
    // =========================
    private fun deleteHarvest(h: HarvestRecord) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar cosecha")
            .setMessage("¿Desea eliminar esta cosecha del ${h.DateHarvestHoney}?")
            .setPositiveButton("Eliminar") { _, _ ->
                controller.deleteHarvest(h.ID) { ok, msg ->
                    if (ok) {
                        Toast.makeText(this, "Cosecha eliminada", Toast.LENGTH_SHORT).show()
                        loadHarvests()
                    } else {
                        Toast.makeText(
                            this,
                            msg ?: "Error al eliminar cosecha",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    }

