package com.utils;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author answer
 *         2017/11/20
 */
public class ExcelUtil {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 动态解析Excel文件，并返回指定的对象列表
     *
     * @param excelFile
     * @return
     */
    public static List<Map<String, String>> resolveExcel(File excelFile) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        //载入文件
        Workbook workbook = WorkbookFactory.create(excelFile);

        //遍历所有Sheet页
        Sheet sheet = workbook.getSheetAt(0);

        //遍历全部非空行
        Iterator<Row> rowItr = sheet.rowIterator();
        while (rowItr.hasNext()) {
            Map<String, String> infoMap = new LinkedHashMap<>();
            //获取行
            Row row = rowItr.next();

            //遍历本行全部列（包括空列）
            int colNum = row.getLastCellNum();
            for (int index = 0; index < colNum; index++) {
                //获取单元格
                Cell cell = row.getCell(index);
                //获取单元格内容
                Object value = getCellValue(cell);
                if (index == 0) {
                    infoMap.put("name", value.toString());
                } else {
                    infoMap.put("phone", value.toString());
                }
                System.out.println(value);
            }
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 依据Excel中Cell类型读取不同的值
     *
     * @param cell
     * @return String/Double/Date/boolean/null
     */
    public static Object getCellValue(Cell cell) {
        Object result = null;
        //获取类型
        int type = cell.getCellType();
        //根据不同类型处理
        switch (type) {
            //字符串
            case Cell.CELL_TYPE_STRING: {
                result = cell.getRichStringCellValue().toString().trim();
                break;
            }
            //数字(Double)
            case Cell.CELL_TYPE_NUMERIC: {
                //公式(Double)
            }
            case Cell.CELL_TYPE_FORMULA: {
                //日期类型判断
                //日期类型
                if (DateUtil.isCellDateFormatted(cell)) {
                    result = cell.getDateCellValue();
                }
                //普通数字
                else {
                    DecimalFormat df = new DecimalFormat("0");
                    result = df.format(cell.getNumericCellValue());
                }
                break;
            }
            //布尔
            case Cell.CELL_TYPE_BOOLEAN: {
                result = cell.getBooleanCellValue();
                break;
            }
            //其他
            default:
        }

        return result;
    }

    //写
    public static void writeExcel(Map dataMap, List<String> keys, int cloumnCount, String finalXlsxPath) {
        OutputStream out = null;
        try {
            // 获取总列数
            int columnNumCount = cloumnCount;
            // 读取Excel文档
            File finalXlsxFile = new File(finalXlsxPath);
            Workbook workBook = getWorkbok(finalXlsxFile);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            /**
             * 追加数据，除了属性列
             */
            int rowNumber = sheet.getLastRowNum();  // 第一行从0开始算
            System.out.println("原始数据总行数，除属性列：" + rowNumber);
//            for (int i = 1; i <= rowNumber; i++) {
//                Row row = sheet.getRow(i);
//              sheet.removeRow(row);  删除行
//            }
            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            /**
             * 往Excel中写新数据
             */
                // 创建一行：从第二行开始，跳过属性列
            Row row = sheet.createRow(rowNumber + 1);
                for (int k = 0; k <= columnNumCount - 1; k++) {
                    // 在一行内循环
                    Cell first = row.createCell(k);
                    if (dataMap.get(keys.get(k)) != null) {
                        first.setCellValue(dataMap.get(keys.get(k)).toString());
                    } else {
                        first.setCellValue("");
                    }

                }
            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(finalXlsxPath);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if (file.getName().endsWith(EXCEL_XLS)) {  //Excel 2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }


    public static void main(String[] args) {
//        String filepath = "C:\\Users\\zoe\\Desktop\\ada.xlsx";
        String filepath = "C:\\Users\\zoe\\Desktop\\11.xlsx";
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", "aaa");
        map.put("age", "22");
        map.put("address", "深圳");
        try {
            ExcelUtil.writeExcel(map, Lists.newArrayList("name", "age", "address"), 3, filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
