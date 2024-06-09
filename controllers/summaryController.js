const excelService = require('../services/excelService');
const Summary = require('../models/summary');
const path = require('path');

exports.downloadExcel = async (req, res) => {
    try {
        const summaries = await Summary.find();
        const filePath = path.join(__dirname, '..', 'uploads', 'resumen.xlsx');
        await excelService.exportToExcel(summaries, filePath);
        res.download(filePath);
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
};
