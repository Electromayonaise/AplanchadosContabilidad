const express = require('express');
const multer = require('multer');
const path = require('path');
const excelService = require('../services/excelService');
const router = express.Router();

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'uploads/');
  },
  filename: (req, file, cb) => {
    cb(null, `${file.fieldname}-${Date.now()}${path.extname(file.originalname)}`);
  },
});

const upload = multer({ storage });

router.post('/', upload.single('file'), (req, res) => {
  const filePath = req.file.path;
  const data = excelService.parseExcelFile(filePath);
  // Process the data and save to the database
  res.status(200).json({ message: 'File uploaded and processed', data });
});

module.exports = router;
