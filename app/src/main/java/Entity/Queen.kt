package Entity
import com.google.firebase.firestore.Exclude
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class Queen {

    private var id: String = ""
    private var hiveId: String = ""
    private var personId: String = ""
    private var zoneID: String = ""
    private var typeInternal: String = ""
    var entryDateStr: String = ""



   //*private var entryDateInternal: LocalDate = LocalDate.now()*//



    constructor()
    constructor(id: String, entryDateStr: String, type: String, hiveId: String, zoneID: String) {
            this.id = id
            this.typeInternal = type
            this.hiveId = hiveId
            this.entryDateStr = entryDateStr
            this.zoneID = zoneID
        }

    @get:Exclude
    var EntryDate: LocalDate
        get() {
            return try {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                LocalDate.parse(entryDateStr, formatter)
            } catch (e: Exception) {
                LocalDate.now()
            }
        }
        set(value) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            this.entryDateStr = value.format(formatter)
        }

    var ID: String
        get() = id
        set(value) { id = value }

    var Type: String
        get() = typeInternal
        set(value) { typeInternal = value }

    var HiveID: String
        get() = hiveId
        set(value) { hiveId = value }

    var ZoneID: String
        get() = this.zoneID
        set(value) { this.zoneID = value }
    // ------------------ FECHA DESDE STRING ------------------
    fun setEntryDateFromString(dateStr: String) {
        try {
            // Acepta 7/12/2024, 07/12/2024, 9/11/2025, etc.
            val parser = DateTimeFormatter.ofPattern("d/M/yyyy")
            val date = LocalDate.parse(dateStr, parser)

            // La guardamos normalizada como dd/MM/yyyy
            val formatterOut = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            this.entryDateStr = date.format(formatterOut)
        } catch (e: Exception) {
            // Si hay error, guardamos hoy
            val today = LocalDate.now()
            val formatterOut = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            this.entryDateStr = today.format(formatterOut)
        }
    }

    fun getEntryDateAsString(): String = entryDateStr

    // === NUEVA FUNCIÓN PRINCIPAL DE EDAD EN TEXTO ===
    fun AgeText(): String {
        val today = LocalDate.now()
        val start = EntryDate

        // Si la fecha está en el futuro, devolvemos 0 días
        if (start.isAfter(today)) {
            return "0 días"
        }

        val p: Period = Period.between(start, today)
        val years  = p.years
        val months = p.months
        val days   = p.days

        // 1) Menos de 1 mes -> solo días
        if (years == 0 && months == 0) {
            return when (days) {
                0  -> "0 días"
                1  -> "1 día"
                else -> "$days días"
            }
        }

        // 2) 1 mes o más -> combinamos años, meses y días
        val partes = mutableListOf<String>()

        if (years > 0) {
            partes += if (years == 1) "1 año" else "$years años"
        }
        if (months > 0) {
            partes += if (months == 1) "1 mes" else "$months meses"
        }
        if (days > 0) {
            partes += if (days == 1) "1 día" else "$days días"
        }

        return when (partes.size) {
            1 -> partes[0]
            2 -> partes[0] + " y " + partes[1]
            else -> partes[0] + ", " + partes[1] + " y " + partes[2]
        }
    }

    // (Opcional) numérico en meses, si lo sigues usando en algún lado
    fun AgeInMonths(): Int {
        val today = LocalDate.now()
        val start = EntryDate
        if (start.isAfter(today)) return 0

        val p = Period.between(start, today)
        return p.years * 12 + p.months
    }

    fun FullInfoQueen(): String =
        "${this.Type} ${this.ZoneID} ${this.getEntryDateAsString()} ${this.HiveID} ${this.ID} ${this.AgeInMonths()}"
    override fun toString(): String = FullInfoQueen()
}