// firebase.js
const admin = require("firebase-admin");
const serviceAccount = require("./config/beekeepersnotebook-firebase-adminsdk-fbsvc-57b643d692.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

module.exports = db;
