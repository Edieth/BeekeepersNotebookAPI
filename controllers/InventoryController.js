// controllers/InventoryController.js
const InventoryModel = require("../models/InventoryModel");

module.exports = {
  get_all: async (req, res) => {
    const result = await InventoryModel.get_all();
    res.status(result.responseCode).json(result);
  },

  get_by_id: async (req, res) => {
    const { id } = req.params;
    const result = await InventoryModel.get_by_id(id);
    res.status(result.responseCode).json(result);
  },

  get_by_person: async (req, res) => {
    const { personId } = req.params;
    const result = await InventoryModel.get_by_person(personId);
    res.status(result.responseCode).json(result);
  },

  create: async (req, res) => {
    const result = await InventoryModel.create(req.body);
    res.status(result.responseCode).json(result);
  },

  update: async (req, res) => {
    const { id } = req.params;
    const result = await InventoryModel.update(id, req.body);
    res.status(result.responseCode).json(result);
  },

  remove: async (req, res) => {
    const { id } = req.params;
    const result = await InventoryModel.remove(id);
    res.status(result.responseCode).json(result);
  },
};
