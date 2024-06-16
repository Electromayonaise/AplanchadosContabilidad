const express = require('express');
const mongoose = require('mongoose');
const dotenv = require('dotenv');
const path = require('path');


// Load environment variables
dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'public'))); // Sirve archivos estáticos desde 'public'

// Routes
const transactionRoutes = require('./routes/transactions');
const summaryRoutes = require('./routes/summaries');
app.use('/api/transactions', transactionRoutes);
app.use('/api/summaries', summaryRoutes);

// Connect to MongoDB
const connectDB = require('./utils/db');
connectDB();

// Serve HTML files
app.get('/', (req, res) => res.sendFile(path.join(__dirname, 'public', 'index.html')));
app.get('/transactions.html', (req, res) => res.sendFile(path.join(__dirname, 'public', 'transactions.html')));
app.get('/summaries.html', (req, res) => res.sendFile(path.join(__dirname, 'public', 'summaries.html')));
app.get('/upload.html', (req, res) => res.sendFile(path.join(__dirname, 'public', 'upload.html')));

app.listen(PORT, () => console.log(`Server is running on port ${PORT}`));
