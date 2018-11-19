package com.template.excel;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ListToExcel {

	public static void exportExcel(List result, OutputStream out, String sheetName, List tableHead) throws Exception {
		WritableWorkbook workBook = null;
		try {
			workBook = Workbook.createWorkbook(out);
			workBook.setProtected(true);
			WritableSheet sheet = workBook.createSheet(sheetName, 0);
			SheetSettings ss = sheet.getSettings();
			WritableFont wf = null;
			WritableCellFormat wcf = null;

			wf = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);

			wcf = new WritableCellFormat(wf);
			wcf.setAlignment(Alignment.CENTRE);

			Label label = null;

			for (int i = 0; i < tableHead.size(); i++) {
				sheet.setColumnView(i, 20);
				label = new Label(i, 0, (String) tableHead.get(i), wcf);

				sheet.addCell(label);
			}
			List temp;
			for (int i = 0; i < result.size(); i++) {
				temp = (ArrayList) result.get(i);
				for (int j = 0; j < temp.size(); j++) {
					label = new Label(j, i + 1, (String) temp.get(j));
					sheet.addCell(label);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				workBook.write();
				if (workBook != null) {
					workBook.close();
				}
				out.flush();
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 返回文件生成地址
	 * @param sheets
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static void exportExcelForSheet(List<ExcelSheet> sheets, File file) throws Exception {
		WritableWorkbook workBook = null;
		try {
			workBook = Workbook.createWorkbook(file);
			workBook.setProtected(true);
			int index=0;
			for (int i = 0; i < sheets.size(); i++) {
				if(sheets.get(i).empty()){
					continue;
				}
				exportSheet(sheets.get(i), workBook, index);
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				workBook.write();
				if (workBook != null) {
					workBook.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void exportSheet(ExcelSheet sheet, WritableWorkbook workBook, int index) throws Exception {
		WritableSheet writeSheet = workBook.createSheet(sheet.getSheetName(), index);
		List<String> tableHead = sheet.getHeaderList();
		List<List> dataList = sheet.getDataList();
		SheetSettings ss = writeSheet.getSettings();
		WritableFont wf = null;
		WritableCellFormat wcf = null;

		wf = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
				Colour.BLACK);

		wcf = new WritableCellFormat(wf);
		wcf.setAlignment(Alignment.CENTRE);

		Label label = null;

		for (int i = 0; i < tableHead.size(); i++) {
			writeSheet.setColumnView(i, 20);
			label = new Label(i, 0, tableHead.get(i), wcf);
			writeSheet.addCell(label);
		}
		List temp;
		for (int i = 0; i < dataList.size(); i++) {
			temp = dataList.get(i);
			for (int j = 0; j < temp.size(); j++) {
				label = new Label(j, i + 1,  temp.get(j).toString());
				writeSheet.addCell(label);
			}
		}
	}
}
