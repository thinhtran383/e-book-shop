package org.example.bookshop.utils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class Exporter<T> {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<T> dataList;
    private final String sheetName;

    public Exporter(List<T> dataList, String sheetName) {
        this.dataList = dataList;
        this.sheetName = sheetName;

        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet(sheetName);

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        // Lấy tiêu đề từ các thuộc tính của class
        if (!dataList.isEmpty()) {
            T firstObject = dataList.get(0);
            Field[] fields = firstObject.getClass().getDeclaredFields();

            int columnCount = 0;
            for (Field field : fields) {
                createCell(row, columnCount++, field.getName(), style);
            }
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        for (T data : dataList) {
            Row row = sheet.createRow(rowCount++);
            Field[] fields = data.getClass().getDeclaredFields();
            int columnCount = 0;

            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(data);
                    createCell(row, columnCount++, value, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
