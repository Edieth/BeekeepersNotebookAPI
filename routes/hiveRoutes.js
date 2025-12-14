const express = require("express");
const router = express.Router();
const HiveController = require("../controllers/HiveController");

// GET /hives
router.get("/", HiveController.get_all);

// ✅ filtros que tu APP ya usa
router.get("/by-person/:personId", HiveController.get_by_person);
router.get("/by-zone/:zoneId", HiveController.get_by_zone);

// GET /hives/:id
router.get("/:id", HiveController.get_by_id);

// POST /hives
router.post("/", HiveController.create);

// PUT /hives/:id
router.put("/:id", HiveController.update);

// DELETE /hives/:id
router.delete("/:id", HiveController.remove);

module.exports = router;
