package cr.ac.utn.beekeepersnotebook

import Entity.Person
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SingUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button
    private lateinit var btnSelectPhoto: Button
    private lateinit var etFirstLastName: EditText
    private lateinit var etSecondLastName: EditText
    private var selectedPhotoBitmap: Bitmap? = null

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val bmp = MediaStore.Images
                    .Media
                    .getBitmap(this.contentResolver, uri)
                selectedPhotoBitmap = bmp
                imgPhoto.setImageBitmap(bmp)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sing_up)

        // 🔹 ESTA lambda solo ajusta padding y devuelve insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 TODA la inicialización va FUERA de la lambda
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etEmail = findViewById(R.id.etEmail)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnCancel = findViewById(R.id.btnCancel)
        etFirstLastName = findViewById(R.id.etFirstLastName)
        etSecondLastName = findViewById(R.id.etSecondLastName)
        imgPhoto = findViewById(R.id.imgPhoto)
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto)
        btnSubmit.setOnClickListener { doSignUp() }
        btnCancel.setOnClickListener { finish() }
        btnSelectPhoto.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        btnSubmit.setOnClickListener {
            doSignUp()
        }
    }

    private fun doSignUp() {
        val username = etUsername.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirm = etConfirmPassword.text.toString().trim()
        val firstLastName = etFirstLastName.text.toString().trim()
        val secondLastName = etSecondLastName.text.toString().trim()


        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirm) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 6 caracteres",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user == null) {
                    Toast.makeText(this, "No se pudo crear el usuario", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                // 🔹 Creamos Person usando TU Entity.Person
                val person = Person(
                    id = user.uid,
                    name = username,
                    fLastName = "",
                    sLastName = "",
                    email = email,
                    password = password,
                    phoneperson = "",
                    photo = null
                )

                // Guardamos el Person en Firestore
                db.collection("persons")
                    .document(user.uid)
                    .set(person)
                    .addOnSuccessListener {

                        // Enviamos verificación de correo
                        user.sendEmailVerification()
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Cuenta creada. Revise su correo para verificar.",
                                    Toast.LENGTH_LONG
                                ).show()

                                val i = Intent(this, VerificationActivity::class.java)
                                i.putExtra("EMAIL", email)
                                startActivity(i)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Error al enviar verificación: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Error al guardar datos: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error al crear usuario: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
