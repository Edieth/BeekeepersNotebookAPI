package cr.ac.utn.beekeepersnotebook

import Controller.PersonController
import Entity.Person
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate
import java.util.Calendar
import Util.Util


class PersonActivity : AppCompatActivity() {

    // Campos de la pantalla
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtFLastName: EditText
    private lateinit var txtSLastName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPhone: EditText
    private lateinit var imgPhoto: ImageView

    // Controladores y estado
    private lateinit var personController: PersonController
    private lateinit var menuitemDelete: MenuItem
    private var isEditMode: Boolean = false
    private var currentPerson: Person? = null

    // =========================
    // ActivityResult para cámara y galería
    // =========================

    private val cameraPreviewLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                imgPhoto.setImageBitmap(bitmap)
            }
        }

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    imgPhoto.setImageURI(uri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_person)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        personController = PersonController(this)

        // Vincular controles del layout
        txtId = findViewById(R.id.txtId_person)
        txtName = findViewById(R.id.txtName_person)
        txtFLastName = findViewById(R.id.txtFLastName_person)
        txtSLastName = findViewById(R.id.txtSLastName_person)
        txtEmail = findViewById(R.id.txtEmail_person)
        txtPhone = findViewById(R.id.txtPhone_person)
        imgPhoto = findViewById(R.id.imgPhoto_person)

        // Botón de foto
        val btnPhoto = findViewById<ImageButton>(R.id.btnPhoto_person)
        btnPhoto.setOnClickListener {
            takePhoto()
        }

        // Botón de buscar por ID
        val btnSearch = findViewById<ImageButton>(R.id.btnSearchId_person)
        btnSearch.setOnClickListener {
            val id = txtId.text.toString().trim()
            if (id.isEmpty()) {
                Toast.makeText(this, "Ingrese un ID para buscar", Toast.LENGTH_SHORT).show()
            } else {
                searchPerson(id)
            }
        }

        // Si se abrió con un ID (por ejemplo desde otra pantalla), lo cargamos
        val personIdFromIntent = intent.getStringExtra("PERSON_ID")
        if (!personIdFromIntent.isNullOrEmpty()) {
            txtId.setText(personIdFromIntent)
            searchPerson(personIdFromIntent)
        }
    }

    // =========================
    // Menú CRUD
    // =========================

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_crud, menu)
        menuitemDelete = menu.findItem(R.id.mnu_delete)
        menuitemDelete.isVisible = isEditMode
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // asegurar que el botón de eliminar se actualice cuando cambiamos de modo
        if (::menuitemDelete.isInitialized) {
            menuitemDelete.isVisible = isEditMode
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                if (isEditMode) {
                    Util.showDialogCondition(
                        this,
                        getString(R.string.TextSaveActionQuestion)
                    ) { savePerson() }
                } else {
                    savePerson()
                }
                true
            }

            R.id.mnu_delete -> {
                Util.showDialogCondition(
                    this,
                    getString(R.string.TextDeleteActionQuestion)
                ) { deleteContact() }
                true
            }

            R.id.mnu_cancel -> {
                cleanScreen()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // =========================
    // Búsqueda
    // =========================

    private fun searchPerson(id: String) {
        personController.getByIdPerson(id) { person ->
            if (person != null) {
                isEditMode = true
                currentPerson = person

                txtId.setText(person.ID)
                txtId.isEnabled = false
                txtName.setText(person.Name)
                txtFLastName.setText(person.FLastName)
                txtSLastName.setText(person.SLastName)
                txtEmail.setText(person.Email)
                txtPhone.setText(person.PhonePerson)

                val bitmap = person.Photo
                if (bitmap != null) {
                    imgPhoto.setImageBitmap(bitmap)
                } else {
                    imgPhoto.setImageBitmap(null)
                }

                menuitemDelete.isVisible = true
            } else {
                Toast.makeText(this, R.string.MsgDataNotFound, Toast.LENGTH_LONG).show()
            }
        }
    }

    // =========================
    // Limpieza de pantalla
    // =========================

    private fun cleanScreen() {
        isEditMode = false
        currentPerson = null

        txtId.isEnabled = true
        txtId.setText("")
        txtName.setText("")
        txtFLastName.setText("")
        txtSLastName.setText("")
        txtEmail.setText("")
        txtPhone.setText("")

        imgPhoto.setImageBitmap(null)

        invalidateOptionsMenu()
    }

    // =========================
    // Validación
    // =========================

    private fun isValidatedData(): Boolean {
        val id = txtId.text.toString().trim()
        val name = txtName.text.toString().trim()
        val fLast = txtFLastName.text.toString().trim()
        val sLast = txtSLastName.text.toString().trim()
        val email = txtEmail.text.toString().trim()
        val phone = txtPhone.text.toString().trim()

        if (id.isEmpty() || name.isEmpty() || fLast.isEmpty() || sLast.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            return false
        }

        // Validación simple de teléfono numérico y largo
        if (phone.length < 8 || phone.toLongOrNull() == null || phone == "0") {
            return false
        }

        return true
    }

    // =========================
    // Guardar (add / update)
    // =========================

    private fun savePerson() {
        try {
            if (!isValidatedData()) {
                Toast.makeText(this, R.string.MsgMissingData, Toast.LENGTH_LONG).show()
                return
            }

            val id = txtId.text.toString().trim()

            if (!isEditMode) {
                // Alta nueva: revisamos duplicado por ID
                personController.getByIdPerson(id) { existing ->
                    if (existing != null) {
                        Toast.makeText(this, R.string.MsgDuplicateData, Toast.LENGTH_LONG).show()
                    } else {
                        createNewPerson()
                    }
                }
            } else {
                // Edición
                updateExistingPerson()
            }

        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun createNewPerson() {
        val person = Person().apply {
            ID = txtId.text.toString().trim()
            Name = txtName.text.toString().trim()
            FLastName = txtFLastName.text.toString().trim()
            SLastName = txtSLastName.text.toString().trim()
            Email = txtEmail.text.toString().trim()
            PhonePerson = txtPhone.text.toString().trim()
            // Password no se edita aquí, se maneja en SignUp / Auth
            Password = ""
            Photo = (imgPhoto.drawable as? BitmapDrawable)?.bitmap
        }

        personController.addPerson(person) { ok, error ->
            if (ok) {
                Toast.makeText(this, R.string.MsgSaveSuccess, Toast.LENGTH_LONG).show()
                cleanScreen()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.ErrorMsgAdd) + " ${error ?: ""}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateExistingPerson() {
        val originalPassword = currentPerson?.Password ?: ""
        val originalPhoto = currentPerson?.Photo

        val newPhoto = (imgPhoto.drawable as? BitmapDrawable)?.bitmap

        val person = Person().apply {
            ID = txtId.text.toString().trim()
            Name = txtName.text.toString().trim()
            FLastName = txtFLastName.text.toString().trim()
            SLastName = txtSLastName.text.toString().trim()
            Email = txtEmail.text.toString().trim()
            PhonePerson = txtPhone.text.toString().trim()
            Password = originalPassword
            Photo = newPhoto ?: originalPhoto
        }

        personController.updatePerson(person) { ok, error ->
            if (ok) {
                Toast.makeText(this, R.string.MsgSaveSuccess, Toast.LENGTH_LONG).show()
                cleanScreen()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.ErrorMsgUpdate) + " ${error ?: ""}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================
    // Eliminar
    // =========================

    private fun deleteContact() {
        val id = txtId.text.toString().trim()
        if (id.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID para eliminar", Toast.LENGTH_SHORT).show()
            return
        }

        personController.deletePerson(id) { ok, error ->
            if (ok) {
                Toast.makeText(this, R.string.MsgDeleteSucess, Toast.LENGTH_LONG).show()
                cleanScreen()
            } else {
                Toast.makeText(
                    this,
                    (error ?: getString(R.string.ErrorMsgRemove)),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================
    // Cámara y galería
    // =========================

    private fun takePhoto() {
        cameraPreviewLauncher.launch(null)
    }

    @Suppress("unused")
    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        selectImageLauncher.launch(intent)
    }
}