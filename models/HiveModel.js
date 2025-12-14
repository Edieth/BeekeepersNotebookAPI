const db = require("../firebase");
const COLLECTION = "beehives";

module.exports = {
  // GET /hives
  get_all: async () => {
    try {
      const snapshot = await db.collection(COLLECTION).get();
      const hives = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));
      return { data: hives, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching hives" };
    }
  },

  // GET /hives/:id
  get_by_id: async (id) => {
    try {
      const doc = await db.collection(COLLECTION).doc(id).get();
      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Hive not found" };
      }
      return { data: [{ id: doc.id, ...doc.data() }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching hive" };
    }
  },

  // GET /hives/by-person/:personId
  get_by_person: async (personId) => {
    try {
      const snapshot = await db.collection(COLLECTION)
        .where("personId", "==", personId)
        .get();

      const hives = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));

      return { data: hives, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching hives by person" };
    }
  },

  // GET /hives/by-zone/:zoneId
  get_by_zone: async (zoneId) => {
    try {
      const snapshot = await db.collection(COLLECTION)
        .where("zoneId", "==", zoneId)
        .get();

      const hives = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));

      return { data: hives, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching hives by zone" };
    }
  },

  // POST /hives
  create: async (body) => {
    try {
      const ref = db.collection(COLLECTION).doc();

      // ✅ CAMPOS EXACTOS: DTOBeehive (Android) + personId
      const hive = {
        id: ref.id,
        name: body.name || "",
        zoneId: body.zoneId || "",
        boxType: body.boxType || "",
        queenId: body.queenId || "",
        personId: body.personId || ""
      };

      await ref.set(hive);
      return { data: [hive], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error creating hive" };
    }
  },

  // PUT /hives/:id
  update: async (id, body) => {
    try {
      const ref = db.collection(COLLECTION).doc(id);
      const doc = await ref.get();

      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Hive not found" };
      }

      const updated = {
        ...doc.data(),
        ...body,
        id: id
      };

      // ✅ “clean” para NO guardar basura
      const clean = {
        id: updated.id,
        name: updated.name || "",
        zoneId: updated.zoneId || "",
        boxType: updated.boxType || "",
        queenId: updated.queenId || "",
        personId: updated.personId || ""
      };

      await ref.update(clean);
      return { data: [clean], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error updating hive" };
    }
  },

  // DELETE /hives/:id
  remove: async (id) => {
    try {
      await db.collection(COLLECTION).doc(id).delete();
      return { data: [{ id }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error deleting hive" };
    }
  }
};
