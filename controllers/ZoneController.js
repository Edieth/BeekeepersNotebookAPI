// controllers/ZoneController.js
const ZoneModel = require("../models/ZoneModel");

module.exports = {

  get_all: async (req, res) => {
    const result = await ZoneModel.get_all();
    res.status(result.responseCode).json(result);
  },

  get_by_id: async (req, res) => {
    const { id } = req.params;
    const result = await ZoneModel.get_by_id(id);
    res.status(result.responseCode).json(result);
  },

  get_by_person: async (req, res) => {
    const { personID } = req.params;
    const result = await ZoneModel.get_by_person(personID);
    res.status(result.responseCode).json(result);
  },

  create: async (req, res) => {
    const result = await ZoneModel.create(req.body);
    res.status(result.responseCode).json(result);
  },

  update: async (req, res) => {
    const { id } = req.params;
    const result = await ZoneModel.update(id, req.body);
    res.status(result.responseCode).json(result);
  },

  remove: async (req, res) => {
    const { id } = req.params;
    const result = await ZoneModel.remove(id);
    res.status(result.responseCode).json(result);
  }
};
