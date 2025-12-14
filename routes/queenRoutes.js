// routes/queenRoutes.js
const express = require("express");
const router = express.Router();
const QueenController = require("../controllers/QueenController");

// GET /queens
router.get("/", QueenController.get_all);

// filtros (antes de /:id)
router.get("/by-person/:personId", QueenController.get_by_person);
router.get("/by-hive/:hiveId", QueenController.get_by_hive);

// GET /queens/:id
router.get("/:id", QueenController.get_by_id);

// POST /queens
router.post("/", QueenController.create);

// PUT /queens/:id
router.put("/:id", QueenController.update);

// DELETE /queens/:id
router.delete("/:id", QueenController.remove);

module.exports = router;
