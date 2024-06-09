const ExcelJS = require('exceljs');

exports.exportToExcel = async (data, filePath) => {
    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet('Resumen');

    worksheet.columns = [
        { header: 'Documento', key: 'document', width: 30 },
        { header: 'Detalle', key: 'detail', width: 30 },
        { header: 'Valor', key: 'value', width: 15 },
        { header: 'Fecha', key: 'date', width: 20 },
        // Añade más columnas según sea necesario
    ];

    data.forEach(item => {
        worksheet.addRow(item);
    });

    await workbook.xlsx.writeFile(filePath);
};
