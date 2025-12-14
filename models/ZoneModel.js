const db = require("../firebase");
const COLLECTION = "zones";

module.exports = {

  get_all: async () => {
    try {
      const snapshot = await db.collection(COLLECTION).get();
      const zones = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));
      return { data: zones, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching zones" };
    }
  },

  get_by_id: async (id) => {
    try {
      const doc = await db.collection(COLLECTION).doc(id).get();
      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Zone not found" };
      }
      return { data: [{ id: doc.id, ...doc.data() }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching zone" };
    }
  },


  get_by_person: async (personID) => {
    try {
      const snapshot = await db.collection(COLLECTION)
        .where("personID", "==", personID)
        .get();

      const zones = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));

      return { data: zones, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching zones by person" };
    }
  },

  create: async (body) => {
    try {
      const ref = db.collection(COLLECTION).doc();


      const zone = {
        id: ref.id,
        name: body.name || "",
        beehiveIds: Array.isArray(body.beehiveIds) ? body.beehiveIds : [],
        personID: body.personID || "",
        state: body.state || "",
        district: body.district || "",
        address: body.address || "",
        latitude: body.latitude ?? 0,
        longitude: body.longitude ?? 0
      };

      await ref.set(zone);
      return { data: [zone], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error creating zone" };
    }
  },


  update: async (id, body) => {
    try {
      const ref = db.collection(COLLECTION).doc(id);
      const doc = await ref.get();

      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Zone not found" };
      }

      const updated = {
        ...doc.data(),
        ...body,
        id: id
      };

      const clean = {
        id: updated.id,
        name: updated.name || "",
        beehiveIds: Array.isArray(updated.beehiveIds) ? updated.beehiveIds : [],
        personID: updated.personID || "",
        state: updated.state || "",
        district: updated.district || "",
        address: updated.address || "",
        latitude: updated.latitude ?? 0,
        longitude: updated.longitude ?? 0
      };

      await ref.update(clean);
      return { data: [clean], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error updating zone" };
    }
  },

  remove: async (id) => {
    try {
      await db.collection(COLLECTION).doc(id).delete();
      return { data: [{ id }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error deleting zone" };
    }
  }
};
