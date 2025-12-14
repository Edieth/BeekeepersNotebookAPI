const db = require("../firebase");
const COLLECTION = "queens";

module.exports = {
  get_all: async () => {
    try {
      const snapshot = await db.collection(COLLECTION).get();
      const queens = snapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
      }));
      return { data: queens, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching queens" };
    }
  },

  get_by_id: async (id) => {
    try {
      const doc = await db.collection(COLLECTION).doc(id).get();
      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Queen not found" };
      }
      return { data: [{ id: doc.id, ...doc.data() }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching queen" };
    }
  },

  get_by_person: async (personId) => {
    try {
      const snapshot = await db.collection(COLLECTION)
        .where("personId", "==", personId)
        .get();

      const queens = snapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
      }));

      return { data: queens, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching queens by person" };
    }
  },

  get_by_hive: async (hiveId) => {
    try {
      const snapshot = await db.collection(COLLECTION)
        .where("hiveId", "==", hiveId)
        .get();

      const queens = snapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
      }));

      return { data: queens, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching queens by hive" };
    }
  },

  create: async (body) => {
    try {
      const ref = db.collection(COLLECTION).doc();

      const queen = {
        id: ref.id,
        hiveId: body.hiveId || "",
        personId: body.personId || "",
        zoneID: body.zoneID || "",
        typeInternal: body.typeInternal || "",
        entryDateInternal: body.entryDateInternal || ""
      };

      await ref.set(queen);
      return { data: [queen], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error creating queen" };
    }
  },

  update: async (id, body) => {
    try {
      const ref = db.collection(COLLECTION).doc(id);
      const doc = await ref.get();

      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Queen not found" };
      }

      const updated = {
        ...doc.data(),
        ...body,
        id: id
      };

      const clean = {
        id: updated.id,
        hiveId: updated.hiveId || "",
        personId: updated.personId || "",
        zoneID: updated.zoneID || "",
        typeInternal: updated.typeInternal || "",
        entryDateInternal: updated.entryDateInternal || ""
      };

      await ref.update(clean);
      return { data: [clean], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error updating queen" };
    }
  },


  remove: async (id) => {
    try {
      await db.collection(COLLECTION).doc(id).delete();
      return { data: [{ id }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error deleting queen" };
    }
  },
};
