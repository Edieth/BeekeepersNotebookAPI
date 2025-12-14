package cr.ac.utn.beekeepersnotebook

import Entity.Person
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.widget.ImageButton

class ProfileActivity : AppCompatActivity() {
    private lateinit var imgPhoto: ImageView
    private lateinit var etName: EditText
    private lateinit var etFLast: EditText
    private lateinit var etSLast: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var btnResetPassword: Button
    private lateinit var btnChangeEmail: Button
    private lateinit var btnChangePhoto: ImageButton

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var currentPerson: Person? = null
    private var selectedBitmap: Bitmap? = null

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                contentResolver.openInputStream(uri)?.use { stream ->
                    val bmp = BitmapFactory.decodeStream(stream)
                    selectedBitmap = bmp
                    imgPhoto.setImageBitmap(bmp)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        imgPhoto = findViewById(R.id.imgProfilePhoto)
        etName = findViewById(R.id.etProfileName)
        etFLast = findViewById(R.id.etProfileFLastName)
        etSLast = findViewById(R.id.etProfileSLastName)
        etEmail = findViewById(R.id.etProfileEmail)
        etPhone = findViewById(R.id.etProfilePhone)
        btnSave = findViewById(R.id.btnSaveProfile)
        btnResetPassword = findViewById(R.id.btnResetPassword)
        btnChangeEmail = findViewById(R.id.btnChangeEmail)
        btnChangePhoto = findViewById(R.id.btnChangePhoto)

        btnChangePhoto.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        btnSave.setOnClickListener { saveChanges() }
        btnResetPassword.setOnClickListener { goToForgotPassword() }
        btnChangeEmail.setOnClickListener { changeEmailWithVerification() }

        loadProfile()
    }
    override fun onStart() {
        super.onStart()
        val u = FirebaseAuth.getInstance().currentUser

    }

    private fun loadProfile() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        db.collection("person")
            .document(user.uid)
            .get()
            .addOnSuccessListener { snap ->
                val person = snap.toObject(Person::class.java)
                if (person != null) {
                    currentPerson = person

                    etName.setText(person.Name)
                    etFLast.setText(person.FLastName)
                    etSLast.setText(person.SLastName)
                    etEmail.setText(person.Email)
                    etPhone.setText(person.PhonePerson)

                    if (!person.photoBase64.isNullOrEmpty()) {
                        val bytes = Base64.decode(person.photoBase64, Base64.DEFAULT)
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        selectedBitmap = bmp
                        imgPhoto.setImageBitmap(bmp)
                    }
                } else {
                    Toast.makeText(this, "No se encontraron datos de perfil", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar perfil: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveChanges() {
        val user = auth.currentUser ?: return
        val person = currentPerson ?: Person()

        val newName = etName.text.toString().trim()
        val newFLast = etFLast.text.toString().trim()
        val newSLast = etSLast.text.toString().trim()
        val newEmail = etEmail.text.toString().trim()
        val newPhone = etPhone.text.toString().trim()

        if (newName.isEmpty() || newFLast.isEmpty() || newSLast.isEmpty()
            || newEmail.isEmpty() || newPhone.isEmpty()
        ) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val base64Photo = selectedBitmap?.let { bitmapToBase64(it) } ?: person.photoBase64

        person.ID = user.uid
        person.Name = newName
        person.FLastName = newFLast
        person.SLastName = newSLast
        person.Email = newEmail
        person.PhonePerson = newPhone
        person.photoBase64 = base64Photo
        person.Photo = selectedBitmap   // solo en memoria

        db.collection("person")
            .document(user.uid)
            .set(person)
            .addOnSuccessListener {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun goToForgotPassword() {
        // Reutilizas tu Activity existente
        val i = Intent(this, ForgotPasswordActivity::class.java)
        i.putExtra("EMAIL", etEmail.text.toString().trim())
        startActivity(i)
    }

    private fun changeEmailWithVerification() {
        val user = auth.currentUser ?: return
        val newEmail = etEmail.text.toString().trim()

        if (newEmail.isEmpty()) {
            Toast.makeText(this, "Ingrese el nuevo correo", Toast.LENGTH_SHORT).show()
            return
        }

        // IMPORTANTE: updateEmail a veces requiere re-autenticación reciente
        user.updateEmail(newEmail)
            .addOnSuccessListener {
                user.sendEmailVerification()
                Toast.makeText(
                    this,
                    "Correo actualizado. Revise su email para verificar.",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Error al cambiar correo: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val bytes = stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}