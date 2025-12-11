package Entity

class InventoryItem() {
    var id: String = ""
    var personId: String = ""

    // Nombre del material
    var nameInventoryItem: String = ""

    // Cantidad total
    var inventoryItemTotalQuantity: Int = 0
    private var inventoryDataType: String = ""

    constructor(
        id: String,
        personId: String,
        nameInventory: String,
        inventoryTotalQuantity: Int,
        inventoryDataType: String
    ) : this() {
        this.id = id
        this.personId = personId
        this.nameInventoryItem= nameInventory
        this.inventoryItemTotalQuantity = inventoryTotalQuantity
        this.inventoryDataType = inventoryDataType
    }

    // ---- PROPIEDADES PÚBLICAS TIPO "C# style" ----

    var ID: String
        get() = id
        set(value) { id = value }

    var PersonID: String
        get() = personId
        set(value) { personId = value }

    var Name: String
        get() = nameInventoryItem
        set(value) { nameInventoryItem = value }

    var InventoryTotalQuantity: Int
        get() = inventoryItemTotalQuantity
        set(value) { inventoryItemTotalQuantity = value }

    var InventoryDataType: String
        get() = inventoryDataType
        set(value) {inventoryDataType = value }

    fun FullInfoInventory(): String =
        "${this.Name} ${this.InventoryTotalQuantity}  ${this.InventoryDataType}"
}