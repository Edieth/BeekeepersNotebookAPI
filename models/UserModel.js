const db = require("../firebase");
const COLLECTION = "person";

module.exports = {

  get_all: async () => {
    const snapshot = await db.collection(COLLECTION).get();
    const users = snapshot.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));

    return { data: users, responseCode: 200 };
  },

  get_by_id: async (id) => {
    const doc = await db.collection(COLLECTION).doc(id).get();

    if (!doc.exists) {
      return { data: [], responseCode: 404, message: "User not found" };
    }

    return {
      data: [{ id: doc.id, ...doc.data() }],
      responseCode: 200
    };
  },

  create: async (body) => {
    const ref = db.collection(COLLECTION).doc();

    const user = {
      id: ref.id,                   
      name: body.name || "",
      fLastName: body.fLastName || "",
      sLastName: body.sLastName || "",
      email: body.email || "",
      password: body.password || "",
      phonePerson: body.phonePerson || "",
      photoBase64: body.photoBase64 || ""
    };

    await ref.set(user);

    return { data: [user], responseCode: 200 };
  },

  update: async (id, body) => {
    const ref = db.collection(COLLECTION).doc(id);
    const doc = await ref.get();

    if (!doc.exists) {
      return { data: [], responseCode: 404 };
    }

    const updated = {
      ...doc.data(),
      ...body,
      id: id
    };

    await ref.update(updated);

    return { data: [updated], responseCode: 200 };
  },

  remove: async (id) => {
    await db.collection(COLLECTION).doc(id).delete();
    return { data: [{ id }], responseCode: 200 };
  }
};
