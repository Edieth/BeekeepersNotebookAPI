
const db = require("../firebase");
const COLLECTION = "harvests";

module.exports = {

  get_all: async () => {
    try {
      const snapshot = await db.collection(COLLECTION).get();
      const harvests = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));

      return { data: harvests, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching harvests" };
    }
  },

  get_by_id: async (id) => {
    try {
      const doc = await db.collection(COLLECTION).doc(id).get();

      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Harvest not found" };
      }

      return { data: [{ id: doc.id, ...doc.data() }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching harvest" };
    }
  },

  get_by_hive: async (beehiveId) => {
    try {
      const snapshot = await db.collection(COLLECTION)
        .where("beehiveId", "==", beehiveId)
        .get();

      const harvests = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));

      return { data: harvests, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching harvests by hive" };
    }
  },


  get_by_zone: async (zoneID) => {
    try {
      const snapshot = await db.collection(COLLECTION)
        .where("zoneID", "==", zoneID)
        .get();

      const harvests = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      }));

      return { data: harvests, responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error fetching harvests by zone" };
    }
  },


  create: async (body) => {
    try {
      const ref = db.collection(COLLECTION).doc();

      const harvest = {
        id: ref.id,
        beehiveId: body.beehiveId || "",
        zoneID: body.zoneID || "",
        personId: body.personId || "",
        honeyFrames: body.honeyFrames ?? 0,
        honeyAmountKg: body.honeyAmountKg ?? 0,
        honeyAmountKgNetaHarvest: body.honeyAmountKgNetaHarvest ?? 0,
        DateHarvest: body.DateHarvest || ""   
      };

      await ref.set(harvest);

      return { data: [harvest], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error creating harvest" };
    }
  },

  update: async (id, body) => {
    try {
      const ref = db.collection(COLLECTION).doc(id);
      const doc = await ref.get();

      if (!doc.exists) {
        return { data: [], responseCode: 404, message: "Harvest not found" };
      }

      const updated = {
        ...doc.data(),
        ...body,
        id: id
      };

      const clean = {
        id: updated.id,
        beehiveId: updated.beehiveId || "",
        zoneID: updated.zoneID || "",
        honeyFrames: updated.honeyFrames ?? 0,
        honeyAmountKg: updated.honeyAmountKg ?? 0,
        honeyAmountKgNetaHarvest: updated.honeyAmountKgNetaHarvest ?? 0,
        DateHarvest: updated.DateHarvest || ""
      };

      await ref.update(clean);

      return { data: [clean], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error updating harvest" };
    }
  },

  remove: async (id) => {
    try {
      await db.collection(COLLECTION).doc(id).delete();
      return { data: [{ id }], responseCode: 200 };
    } catch (e) {
      console.error(e);
      return { data: [], responseCode: 500, message: "Error deleting harvest" };
    }
  }
};
