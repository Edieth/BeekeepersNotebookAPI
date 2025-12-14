// routes/inventoryRoutes.js
const express = require("express");
const router = express.Router();
const InventoryController = require("../controllers/InventoryController");

// GET /inventory
router.get("/", InventoryController.get_all);

// GET /inventory/:id
router.get("/:id", InventoryController.get_by_id);

// GET /inventory/by-person/:personId
router.get("/by-person/:personId", InventoryController.get_by_person);

// POST /inventory
router.post("/", InventoryController.create);

// PUT /inventory/:id
router.put("/:id", InventoryController.update);

// DELETE /inventory/:id
router.delete("/:id", InventoryController.remove);

module.exports = router;
