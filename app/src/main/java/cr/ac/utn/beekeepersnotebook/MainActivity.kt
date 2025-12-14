package cr.ac.utn.beekeepersnotebook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var btnZones: Button
    private lateinit var btnLogout: Button
    private lateinit var btnInventory: Button
    private lateinit var btnProfile: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            insets
        }
        btnInventory = findViewById(R.id.btnInventory)
        btnZones = findViewById(R.id.btnZones2)
        btnLogout = findViewById(R.id.btnLogout)
        btnProfile = findViewById(R.id.btnProfile)

        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)

        btnProfile.setOnClickListener {
            val u = FirebaseAuth.getInstance().currentUser
            

            startActivity(Intent(this, ProfileActivity::class.java))
        }
        btnInventory.setOnClickListener {
            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)
        }
        btnZones.setOnClickListener {
            startActivity(Intent(this, ZoneActivity::class.java))
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        // Si quieres obligar verificación, recarga primero:
        user.reload().addOnCompleteListener {
            if (!user.isEmailVerified) {
                startActivity(Intent(this, VerificationActivity::class.java))
                finish()
            }
        }
    }

}