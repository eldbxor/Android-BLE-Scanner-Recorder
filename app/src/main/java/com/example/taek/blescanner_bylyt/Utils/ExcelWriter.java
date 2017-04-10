package com.example.taek.blescanner_bylyt.Utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.util.List;

/**
 * Created by eldbx on 2017-04-10.
 */

public class ExcelWriter {
    public void xlsWriter(List list) {
        // create a workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        // create a worksheet
        HSSFSheet sheet = workbook.createSheet();
        // create a row
        HSSFRow row = sheet.createRow(0);
        // create a cell;
        HSSFCell cell;

        // 헤더 정보 구성
        cell = row.createCell(0);
        cell.setCellValue("비콘 주소");

        // 리스트의 size 만큼 row를 생성

    }
}
