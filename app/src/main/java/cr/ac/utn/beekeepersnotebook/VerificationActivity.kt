package cr.ac.utn.beekeepersnotebook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class VerificationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        auth = FirebaseAuth.getInstance()

        val btnAlreadyVerified: Button = findViewById(R.id.btnAlreadyVerified)
        val btnResendEmail: Button = findViewById(R.id.btnResendEmail)

        // Botón: "Ya verifiqué mi correo"
        btnAlreadyVerified.setOnClickListener {
            val user = auth.currentUser
            if (user == null) {
                Toast.makeText(this, "No hay usuario logueado.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Muy importante: recargar datos del servidor
            user.reload().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (user.isEmailVerified) {
                        Toast.makeText(this, "Correo verificado correctamente.", Toast.LENGTH_SHORT).show()
                        // Ir a la pantalla principal de tu app
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Tu correo aún no está verificado. Revisa el correo y haz clic en el enlace.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "No se pudo comprobar el estado del correo.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // Botón: "Reenviar correo"
        btnResendEmail.setOnClickListener {
            val user = auth.currentUser
            if (user == null) {
                Toast.makeText(this, "No hay usuario logueado.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            user.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Correo de verificación reenviado.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "No se pudo reenviar el correo.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
    }
