const express = require('express');
const router = express.Router();
const summaryController = require('../controllers/summaryController');

router.post('/', summaryController.createSummary);
router.get('/', summaryController.getSummaries);
router.get('/download', summaryController.downloadExcel);

module.exports = router;
