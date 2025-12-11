package Entity
import android.graphics.Bitmap
import java.time.LocalDate
import Entity.Zone
import com.google.firebase.firestore.Exclude


class Beehive {
    private var id: String = ""
    private var zoneId: String = ""
    private var name: String = ""
    private var boxType: String = ""
    private var queenId: String = ""
    private var photo: Bitmap? = null

    constructor()

    constructor(
        id: String,
        name: String,
        boxType: String,
        queenId: String,
        zoneId: String,
        photo: Bitmap?
    ) {
        this.id = id
        this.name = name
        this.boxType = boxType
        this.queenId = queenId
        this.zoneId = zoneId
        this.photo = photo
    }

    var ID: String
        get() = this.id
        set(value) { this.id = value }

    var Name: String
        get() = this.name
        set(value) { this.name = value }

    var BoxType: String
        get() = this.boxType
        set(value) { this.boxType = value }

    var QueenID: String
        get() = this.queenId
        set(value) { this.queenId = value }

    var ZoneID: String
        get() = this.zoneId
        set(value) { this.zoneId = value }

    @get:Exclude
    @set:Exclude
    var Photo: Bitmap?
        get() = this.photo
        set(value) { this.photo = value }

    fun FullInfo(): String =
        "$name $boxType $queenId $zoneId"
}

