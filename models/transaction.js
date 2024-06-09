const mongoose = require('mongoose');

const transactionSchema = new mongoose.Schema({
  document: String,
  clientName: String,
  paymentType: String,
  paymentMethod: String,
  amount: Number,
  date: { type: Date, default: Date.now },
  detail: String,
  type: String // entrada or salida
});

module.exports = mongoose.model('Transaction', transactionSchema);
