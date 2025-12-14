// controllers/UserController.js
const UserModel = require("../models/UserModel");

exports.get_all = async (req, res) => {
  try {
    const data = await UserModel.getAll();
    res.json(data);
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
};

exports.get_by_id = async (req, res) => {
  try {
    const data = await UserModel.getById(req.params.id);
    if (!data) return res.status(404).json({ error: "No encontrado" });
    res.json(data);
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
};

exports.create = async (req, res) => {
  try {
    const created = await UserModel.create(req.body);
    res.status(201).json(created);
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
};

exports.update = async (req, res) => {
  try {
    const updated = await UserModel.update(req.params.id, req.body);
    res.json(updated);
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
};

exports.remove = async (req, res) => {
  try {
    await UserModel.remove(req.params.id);
    res.json({ ok: true });
  } catch (e) {
    res.status(500).json({ error: e.message });
  }
};
