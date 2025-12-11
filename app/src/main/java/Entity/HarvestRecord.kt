package Entity

import com.google.firebase.firestore.Exclude
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HarvestRecord() {

    private var id: String = ""
    private var beehiveId: String = ""
    private var zoneID: String = ""
    private var honeyFrames: Int = 0
    private var honeyAmountKg: Double = 0.0
    private var honeyAmountKgNetaHarvest: Double = 0.0
    private var DateHarvest: String = ""


    constructor(
        id: String,
        beehiveId: String,
        zoneID: String,
        dateHarvest: String,
        honeyFramesHarvest: Int,
        honeyAmountKgHarvest: Double,
        honeyAmountKgNetaHarvest: Double
    ) : this() {
        this.id = id
        this.beehiveId = beehiveId
        this.zoneID = zoneID
        this.honeyFrames = honeyFramesHarvest
        this.honeyAmountKg = honeyAmountKgHarvest
        this.honeyAmountKgNetaHarvest = honeyAmountKgNetaHarvest
        this.DateHarvest = dateHarvest
    }

    var ID: String
        get() = id
        set(value) {
            id = value
        }
    var BeehiveID: String
        get() = beehiveId
        set(value) {
            beehiveId = value
        }
    var ZoneID: String
        get() = zoneID
        set(value) {
            zoneID = value
        }
    var HoneyFramesHarvest: Int
        get() = honeyFrames
        set(value) {
            honeyFrames = value
        }

    var HoneyAmountKgHarvest: Double
        get() = honeyAmountKg
        set(value) {
            honeyAmountKg = value
        }

    var HoneyAmountKgNetaHarvest: Double
        get() = honeyAmountKgNetaHarvest
        set(value) {
            honeyAmountKgNetaHarvest = value
        }

    var DateHarvestHoney: String
        get() = DateHarvest
        set(value) {
            DateHarvest = value
        }

    // ------------------ Fecha ------------------
    fun FullInfoHarvest(): String =
        "Cosecha $DateHarvestHoney - Colmena $BeehiveID - " +
                "Marcos $HoneyFramesHarvest - Kg bruto $HoneyAmountKgHarvest - Kg neto $HoneyAmountKgNetaHarvest"
}