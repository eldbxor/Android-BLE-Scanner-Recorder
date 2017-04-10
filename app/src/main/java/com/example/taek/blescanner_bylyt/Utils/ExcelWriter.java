package com.example.taek.blescanner_bylyt.Utils;

import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private int lastRowNum = 0;

    public ExcelWriter() { }

    public boolean readFile(String fileName) {
        workbook = null;
        sheet = null;
        row = null;
        cell = null;
        lastRowNum = 0;

        try {
            workbook = new HSSFWorkbook(new FileInputStream(new File(fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ( workbook != null) {
            sheet = workbook.getSheetAt(0);

            if (sheet != null) {
                lastRowNum = sheet.getLastRowNum();
            } else {
                Log.d(TAG, "readFile(): sheet is null");
            }
        } else {
            Log.d(TAG, "readFile(): workbook is null");
            return false;
        }

        return true;
    }

    public void writeFile(List<BLEData> list_BLEData) {
        
    }
}
