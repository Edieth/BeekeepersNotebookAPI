package cr.ac.utn.beekeepersnotebook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ZoneMenuActivity : AppCompatActivity() {
    private lateinit var btnBeehives: Button
    private lateinit var btnQueens: Button
    private lateinit var btnHarvests: Button
    private lateinit var tvZoneName: TextView

    private var zoneId: String = ""
    private var zoneName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_zone_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        zoneId = intent.getStringExtra("ZONE_ID") ?: ""
        zoneName = intent.getStringExtra("ZONE_NAME") ?: ""

        tvZoneName = findViewById(R.id.tvZoneName)
        btnBeehives = findViewById(R.id.btnBeehives)
        btnQueens = findViewById(R.id.btnQueens)
        btnHarvests = findViewById(R.id.btnHarvests)

        tvZoneName.text = zoneName

        // Botón COLMENAS → lista de colmenas de esa zona
        btnBeehives.setOnClickListener {
            val intent = Intent(this, BeehiveActivity::class.java)
            intent.putExtra("ZONE_ID", zoneId)
            intent.putExtra("ZONE_NAME", zoneName)
            startActivity(intent)
        }

        // Botón REINAS → pantalla de reinas, filtrando por zona (o luego por colmena)
        btnQueens.setOnClickListener {
            val intent = Intent(this, QueenActivity::class.java)
            intent.putExtra("ZONE_ID", zoneId)
            startActivity(intent)
        }

        // Botón COSECHAS
        btnHarvests.setOnClickListener {
            val intent = Intent(this, HarvestActivity::class.java)
            intent.putExtra("ZONE_ID", zoneId)
            startActivity(intent)
        }
    }
}