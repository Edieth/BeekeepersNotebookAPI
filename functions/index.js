// functions/index.js

// Triggers v2
const { setGlobalOptions } = require("firebase-functions/v2/options");
const { onRequest } = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");

// Limitar instancias (sin espacios para que no se enoje ESLint)
setGlobalOptions({maxInstances: 10});

// Función de ejemplo para usar onRequest y logger
exports.helloWorld = onRequest((req, res) => {
  logger.info("Hello logs!", {structuredData: true});
  res.send("Hello from Firebase!");
});
