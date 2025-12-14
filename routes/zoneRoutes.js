// routes/zoneRoutes.js
const express = require("express");
const router = express.Router();
const ZoneController = require("../controllers/ZoneController");

// GET /zones
router.get("/", ZoneController.get_all);

// filtro antes de /:id
router.get("/by-person/:personID", ZoneController.get_by_person);

// GET /zones/:id
router.get("/:id", ZoneController.get_by_id);

// POST /zones
router.post("/", ZoneController.create);

// PUT /zones/:id
router.put("/:id", ZoneController.update);

// DELETE /zones/:id
router.delete("/:id", ZoneController.remove);

module.exports = router;
