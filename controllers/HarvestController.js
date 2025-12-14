// controllers/HarvestController.js
const HarvestModel = require("../models/HarvestModel");

module.exports = {

  get_all: async (req, res) => {
    const result = await HarvestModel.get_all();
    res.status(result.responseCode).json(result);
  },

  get_by_id: async (req, res) => {
    const { id } = req.params;
    const result = await HarvestModel.get_by_id(id);
    res.status(result.responseCode).json(result);
  },

  get_by_hive: async (req, res) => {
    const { beehiveId } = req.params;
    const result = await HarvestModel.get_by_hive(beehiveId);
    res.status(result.responseCode).json(result);
  },

  get_by_zone: async (req, res) => {
    const { zoneID } = req.params;
    const result = await HarvestModel.get_by_zone(zoneID);
    res.status(result.responseCode).json(result);
  },

  create: async (req, res) => {
    const result = await HarvestModel.create(req.body);
    res.status(result.responseCode).json(result);
  },

  update: async (req, res) => {
    const { id } = req.params;
    const result = await HarvestModel.update(id, req.body);
    res.status(result.responseCode).json(result);
  },

  remove: async (req, res) => {
    const { id } = req.params;
    const result = await HarvestModel.remove(id);
    res.status(result.responseCode).json(result);
  }
};
