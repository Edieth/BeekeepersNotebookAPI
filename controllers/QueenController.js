// controllers/QueenController.js
const QueenModel = require("../models/QueenModel");

module.exports = {
  get_all: async (req, res) => {
    const result = await QueenModel.get_all();
    res.status(result.responseCode).json(result);
  },

  get_by_id: async (req, res) => {
    const { id } = req.params;
    const result = await QueenModel.get_by_id(id);
    res.status(result.responseCode).json(result);
  },

  get_by_person: async (req, res) => {
    const { personId } = req.params;
    const result = await QueenModel.get_by_person(personId);
    res.status(result.responseCode).json(result);
  },

  get_by_hive: async (req, res) => {
    const { hiveId } = req.params;
    const result = await QueenModel.get_by_hive(hiveId);
    res.status(result.responseCode).json(result);
  },

  create: async (req, res) => {
    const result = await QueenModel.create(req.body);
    res.status(result.responseCode).json(result);
  },

  update: async (req, res) => {
    const { id } = req.params;
    const result = await QueenModel.update(id, req.body);
    res.status(result.responseCode).json(result);
  },

  remove: async (req, res) => {
    const { id } = req.params;
    const result = await QueenModel.remove(id);
    res.status(result.responseCode).json(result);
  },
};
