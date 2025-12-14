
const db = require("../firebase");
const COLLECTION = "inventory";

module.exports = {

  get_all: async () => {
    try {
      const snapshot = await db.collection(COLLECTION).get();
      const items = snapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
      }));

      return { data: items, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching inventory" };
    }
  },

  get_by_id: async (id) => {
    try {
      const doc = await db.collection(COLLECTION).doc(id).get();

      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Inventory item not found" };
      }

      return { data: [{ id: doc.id, ...doc.data() }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching inventory item" };
    }
  },


  get_by_person: async (personId) => {
    try {
      const snapshot = await db.collection(COLLECTION)
        .where("personID", "==", personId)
        .get();

      const items = snapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
      }));

      return { data: items, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching inventory by person" };
    }
  },

  create: async (body) => {
    try {
      const ref = db.collection(COLLECTION).doc();

      const item = {
        id: ref.id, 
        personId: body.personId || "",
        nameInventoryItem: body.nameInventoryItem || "",
        inventoryItemTotalQuantity: body.inventoryItemTotalQuantity ?? 0,
        inventoryDataType: body.inventoryDataType || ""
      };

      await ref.set(item);

      return { data: [item], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error creating inventory item" };
    }
  },

  update: async (id, body) => {
    try {
      const ref = db.collection(COLLECTION).doc(id);
      const doc = await ref.get();

      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Inventory item not found" };
      }

      const updated = {
        ...doc.data(),
        ...body,
        id: id
      };

      const clean = {
        id: updated.id,
        personId: updated.personId || "",
        nameInventoryItem: updated.nameInventoryItem || "",
        inventoryItemTotalQuantity: updated.inventoryItemTotalQuantity ?? 0,
        inventoryDataType: updated.inventoryDataType || ""
      };

      await ref.update(clean);

      return { data: [clean], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error updating inventory item" };
    }
  },

  remove: async (id) => {
    try {
      await db.collection(COLLECTION).doc(id).delete();
      return { data: [{ id }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error deleting inventory item" };
    }
  },
};
