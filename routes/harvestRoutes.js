const express = require("express");
const router = express.Router();
const HarvestController = require("../controllers/HarvestController");

router.get("/", HarvestController.get_all);

router.get("/by-hive/:beehiveId", HarvestController.get_by_hive);
router.get("/by-zone/:zoneID", HarvestController.get_by_zone);

router.get("/:id", HarvestController.get_by_id);

router.post("/", HarvestController.create);

router.put("/:id", HarvestController.update);

router.delete("/:id", HarvestController.remove);

module.exports = router;
