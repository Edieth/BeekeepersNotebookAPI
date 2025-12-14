package Entity

import android.graphics.Bitmap
import com.google.firebase.firestore.Exclude
import java.util.Date

class Person {

    private var uid: String = ""
    private var name: String = ""
    private var fLastName: String = ""
    private var sLastName: String = ""
    private var email: String = ""
    private var password: String = ""
    private var phonePerson: String = ""

    // Bitmap solo en memoria
    @get:Exclude
    @set:Exclude
    var Photo: Bitmap? = null

    var photoBase64: String = ""

    constructor()

    constructor(
        id: String,
        name: String,
        fLastName: String,
        sLastName: String,
        email: String,
        password: String,
        phoneperson: String,
        photo: Bitmap?,
        photoBase64: String = ""
    ) {
        this.uid = id
        this.name = name
        this.fLastName = fLastName
        this.sLastName = sLastName
        this.email = email
        this.password = password
        this.phonePerson = phoneperson
        this.Photo = photo
        this.photoBase64 = photoBase64
    }

    var ID: String
        get() = this.uid
        set(value) { this.uid = value }

    var Name: String
        get() = this.name
        set(value) { this.name = value }

    var FLastName: String
        get() = this.fLastName
        set(value) { this.fLastName = value }

    var SLastName: String
        get() = this.sLastName
        set(value) { this.sLastName = value }

    var Email: String
        get() = this.email
        set(value) { this.email = value }

    var Password: String
        get() = this.password
        set(value) { this.password = value }

    var PhonePerson: String
        get() = this.phonePerson
        set(value) { this.phonePerson = value }

    fun FullName(): String =
        "$Name $FLastName $SLastName"
}
