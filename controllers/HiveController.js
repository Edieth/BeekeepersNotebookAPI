const HiveModel = require("../models/HiveModel");

module.exports = {
  get_all: async (req, res) => {
    const result = await HiveModel.get_all();
    res.status(result.responseCode).json(result);
  },

  get_by_id: async (req, res) => {
    const { id } = req.params;
    const result = await HiveModel.get_by_id(id);
    res.status(result.responseCode).json(result);
  },

  get_by_person: async (req, res) => {
    const { personId } = req.params;
    const result = await HiveModel.get_by_person(personId);
    res.status(result.responseCode).json(result);
  },

  get_by_zone: async (req, res) => {
    const { zoneId } = req.params;
    const result = await HiveModel.get_by_zone(zoneId);
    res.status(result.responseCode).json(result);
  },

  create: async (req, res) => {
    const result = await HiveModel.create(req.body);
    res.status(result.responseCode).json(result);
  },

  update: async (req, res) => {
    const { id } = req.params;
    const result = await HiveModel.update(id, req.body);
    res.status(result.responseCode).json(result);
  },

  remove: async (req, res) => {
    const { id } = req.params;
    const result = await HiveModel.remove(id);
    res.status(result.responseCode).json(result);
  }
};
