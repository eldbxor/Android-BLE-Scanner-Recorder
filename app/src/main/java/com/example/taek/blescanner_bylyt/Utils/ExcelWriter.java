package com.example.taek.blescanner_bylyt.Utils;

import android.content.Context;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eldbx on 2017-04-10.
 */

public class ExcelWriter {
    private  String TAG = "ExcelWriter";
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private HSSFRow row;
    private HSSFCell cell;
    private File file;
    private FileInputStream fin;
    private FileOutputStream fos;
    private int lastRowNum = 0;
    private Context context;
    public boolean isNewFile;

    public ExcelWriter(Context context) {
        this.context = context;
        isNewFile = false;
    }

    public boolean readFile(String fileName) {
        // set up all variable to null or 0
        workbook = null;
        sheet = null;
        row = null;
        cell = null;
        file = null;
        fin = null;
        fos = null;
        lastRowNum = 0;

        file = new File(context.getExternalFilesDir(null), fileName);
        try {
            fin = new FileInputStream(file);
            workbook = new HSSFWorkbook(fin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ( workbook != null) {
            Log.d(TAG, "readFile(): read " + fileName);
            sheet = workbook.getSheetAt(0);

            if (sheet != null) {
                lastRowNum = sheet.getLastRowNum() + 1;
                row = sheet.createRow(lastRowNum);
            } else {
                Log.d(TAG, "readFile(): sheet is null");
                sheet = workbook.createSheet();
                lastRowNum = 0;
                row = sheet.createRow(lastRowNum);
                isNewFile = true;
            }
        } else {
            Log.d(TAG, "readFile(): workbook is null");
            workbook = new HSSFWorkbook();
            sheet = workbook.createSheet();
            lastRowNum = 0;
            row = sheet.createRow(lastRowNum);
            isNewFile = true;
            return false; // the workbook have the file's name doesn't exist..
        }

        return true; // the workbook have the file's name exist.
    }

    public void writeFile(ArrayList<BLEData> list_BLEData) {
        if (isNewFile()) {
            // Cell style for header row
            CellStyle cs = workbook.createCellStyle();
            cs.setFillBackgroundColor(HSSFColor.LIME.index);
            cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Generate column headings
            cell = row.createCell(0);
            cell.setCellValue("DateTime");
            cell.setCellStyle(cs);

            cell = row.createCell(1);
            cell.setCellValue("DeviceName");
            cell.setCellStyle(cs);

            cell = row.createCell(2);
            cell.setCellValue("DeviceAddress");
            cell.setCellStyle(cs);

            cell = row.createCell(3);
            cell.setCellValue("UUID");
            cell.setCellStyle(cs);

            cell = row.createCell(4);
            cell.setCellValue("Major");
            cell.setCellStyle(cs);

            cell = row.createCell(5);
            cell.setCellValue("Minor");
            cell.setCellStyle(cs);

            cell = row.createCell(6);
            cell.setCellValue("ScanRecord");
            cell.setCellStyle(cs);

            cell = row.createCell(7);
            cell.setCellValue("RSSI");
            cell.setCellStyle(cs);

            lastRowNum++;
            isNewFile = false;
        }

        for (BLEData bleData : list_BLEData) {
            row = sheet.createRow(lastRowNum);

            cell = row.createCell(0);
            cell.setCellValue(bleData.currentTime);

            cell = row.createCell(1);
            cell.setCellValue(bleData.deviceName);

            cell = row.createCell(2);
            cell.setCellValue(bleData.deviceAddress);

            cell = row.createCell(3);
            cell.setCellValue(bleData.uuid);

            cell = row.createCell(4);
            cell.setCellValue(bleData.major);

            cell = row.createCell(5);
            cell.setCellValue(bleData.minor);

            cell = row.createCell(6);
            cell.setCellValue(bleData.allData);

            cell = row.createCell(7);
            cell.setCellValue(bleData.rssi);

            lastRowNum++;
        }

        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
            Log.d(TAG, "writeFile(): writing file - " + file);

        } catch (IOException e) {
            Log.d(TAG, "writeFile(): Error writing " + file, e);
        } catch (Exception e) {
            Log.d(TAG, "writeFile(): Failed to save file", e);
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isNewFile() {
        return isNewFile;
    }
}
