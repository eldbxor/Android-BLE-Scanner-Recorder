package com.example.taek.blescanner_bylyt.Utils;

import android.content.Context;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

    public ExcelWriter(Context context) {
        this.context = context;
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

        file = new File(fileName);
        try {
            fin = new FileInputStream(file);
            workbook = new HSSFWorkbook(fin);
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

            return false; // the workbook have the file's name doesn't exist..
        }
        
        return true; // the workbook have the file's name exist.
    }

    public void writeFile(List<BLEData> list_BLEData) {
        if (workbook != null) {

        } else {
            workbook = new HSSFWorkbook();
            sheet = workbook.createSheet();
        }
    }
}
