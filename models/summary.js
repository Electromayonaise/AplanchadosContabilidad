const mongoose = require('mongoose');

const summarySchema = new mongoose.Schema({
  date: { type: Date, default: Date.now },
  initialBalance: Number,
  finalBalance: Number,
  totalIncome: Number,
  totalExpenses: Number,
  managementAmount: Number,
  netBalance: Number
});

module.exports = mongoose.model('Summary', summarySchema);
