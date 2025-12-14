// server.js
const express = require('express');
const cors = require('cors');

const userRoutes = require('./routes/userRoutes');
const hiveRoutes = require('./routes/hiveRoutes'); 
const inventoryRoutes = require("./routes/inventoryRoutes");
const queenRoutes = require("./routes/queenRoutes");
const harvestRoutes = require("./routes/harvestRoutes");
const zoneRoutes = require("./routes/zoneRoutes");

const app = express();
app.use(cors());
app.use(express.json());

app.get('/', (req, res) => {
  res.json({ message: "Beekeepers API running with Firestore!" });
});

app.use('/users', userRoutes);
app.use('/hives', hiveRoutes); 
app.use("/inventory", inventoryRoutes);  
app.use("/queens", queenRoutes);
app.use("/harvests", harvestRoutes);
app.use("/zones", zoneRoutes);

const port = process.env.PORT || 3001;
app.listen(port, () => {
  console.log(`API running on port ${port}`);
});
