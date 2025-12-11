package Entity
import Entity.Person

class Zone {
    private var id: String = ""
    private var name: String = ""
    //private var beehiveIds: MutableList<String> = mutableListOf()
    private var personID: String=""
    private var state: String = ""
    private var district: String = ""
    private var address: String = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0



    constructor()

    constructor(id: String, name: String, personID: String, state: String, district: String,
                address: String, latitude: Double, longitude: Double)
    {
        this.id = id
        this.name = name
        this.personID = personID
        this.state = state
        this.district = district
        this.address = address
        this.latitude = latitude
        this.longitude = longitude

    }

    var ID: String
        get() = this.id
        set(value) { this.id = value }
    var PersonID: String
        get() = this.personID
        set(value) { this.personID = value }
    var Name: String
        get() = this.name
        set(value) { this.name = value }

    //var BeehiveIds: MutableList<String>
       // get() = this.beehiveIds
      //  set(value) { this.beehiveIds = value }

    var State: String
        get() = this.state
        set(value) {
            this.state = value
        }

    var District: String
        get() = this.district
        set(value) {
            this.district = value
        }

    var Address: String
        get() = this.address
        set(value) {
            this.address = value
        }

    var Latitude: Double
        get() = this.latitude
        set(value) {
            this.latitude = value
        }

    var Longitude: Double
        get() = this.longitude
        set(value) {
            this.longitude = value
        }

    override fun toString(): String = "$name)"
}