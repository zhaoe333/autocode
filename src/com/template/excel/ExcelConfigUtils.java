package com.template.excel;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * POI实现excel读取工具类
 * 
 * @author 段思远
 * @date Apr 27, 2011 1:48:26 PM
 * @email duan.sy@ems.com.cn
 */
public class ExcelConfigUtils {
	

	private FileInputStream fileOS;
	private HSSFWorkbook hssfwb;
	private String filePath;
	private int rowNum;

	public ExcelConfigUtils(String filePath) {
		this.filePath = filePath;
	}

	/** 返回工作表名称 */
	public List getSheetNamesOfExcel() { // 返回工作表名称
		List sheetNamesAl = null; // excel的sheet个数
		if (filePath != null) {
			openFileStream();
			sheetNamesAl = getSheetList();
			closeFileStream();
		}
		return sheetNamesAl;
	}

	private List getSheetList() {
		int sheetCount = hssfwb.getNumberOfSheets();
		List sheetNamesAl = new ArrayList();
		if (sheetCount > 0)
			for (int i = 0; i < sheetCount; i++)
				sheetNamesAl.add(hssfwb.getSheetName(i));
		return sheetNamesAl;
	}

	/** 返回工作表列数 */
	private int getColsCount(String SheetName, int rowNo) { // 返回列数
		int colNum = 0;
		short rowNoTemp = 0;
		HSSFSheet hssfSh = hssfwb.getSheet(SheetName);
		rowNoTemp = (short) (rowNo - 1);
		if (hssfSh != null)
			if (hssfSh.getRow(rowNoTemp) != null)
				colNum = hssfSh.getRow(rowNoTemp).getPhysicalNumberOfCells(); // 列数
		return colNum;
	}

	/** 返回工作表列名称 */
	public List getColsNames(String SheetName, int rowNo) { // 返回列名称
		openFileStream();
		List colsNamesAl = new ArrayList();
		HSSFSheet hssfSh = hssfwb.getSheet(SheetName);
		if (hssfSh != null)
			getSheetColumnName(SheetName, rowNo, hssfSh, colsNamesAl);
		closeFileStream();
		return colsNamesAl;
	}

	private List getSheetColumnName(String SheetName, int rowNo, HSSFSheet hssfSh, List colsNamesAl) {
		int colCount = getColsCount(SheetName, rowNo);
		if (colCount > 0) {
			HSSFRow hssfrow = hssfSh.getRow(rowNo - 1);
			for (int i = 0; i < colCount; i++) {
				HSSFCell hssfcell = hssfrow.getCell((short) i);
				if (hssfcell != null) {
					int cellType = hssfcell.getCellType(); // 单元格类型
					if (cellType == HSSFCell.CELL_TYPE_STRING) { // 字符串
						String str = hssfcell.getStringCellValue();
						if (str != null) {
							colsNamesAl.add(str);
						}
					} else if (cellType == HSSFCell.CELL_TYPE_NUMERIC) { // 整型
						if (HSSFDateUtil.isCellDateFormatted(hssfcell)) {
							Date date = (Date) hssfcell.getDateCellValue();
							SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
							String dateStr = format.format(date);
							colsNamesAl.add(dateStr);
						} else {
							double doubleStr = hssfcell.getNumericCellValue();
							String formatStr = doubleTransfer(doubleStr);
							colsNamesAl.add(formatStr);
						}
					} else if (cellType == HSSFCell.CELL_TYPE_BLANK) { // 空白
						colsNamesAl.add(" ");
					}
				}
			}
		}
		return colsNamesAl;
	}

	public int getRowsCount(String SheetName) {
		int rowNum = -1;
		openFileStream();
		HSSFSheet hssfSh = hssfwb.getSheet(SheetName);
		if (hssfSh != null)
			rowNum = hssfSh.getLastRowNum(); // 行数
		return rowNum;
	}

	/**
	 * 根据sheet名称获当前数据
	 * 
	 * @author 段思远
	 * @date Apr 27, 2011 3:43:08 PM
	 * @param SheetName
	 * @param rowNo
	 *            行数
	 * @return Vector
	 * @throws
	 */
	public List getDataBySNList(String SheetName, int rowNo, boolean sqlModel) {
		openFileStream();
		List temp = new ArrayList(); // 返回vector
		HSSFSheet hssfSh = hssfwb.getSheet(SheetName);
		if (hssfSh != null) {
			int colCount = getColsCount(SheetName, rowNo); // 列数
			int thisFromRow = rowNo;
			getDataByRow(temp, hssfSh, rowNo, colCount, sqlModel);
		}
		closeFileStream();
		return temp;
	}

	/**
	 * 根据sheet名称获当前数据
	 * 
	 * @author 段思远
	 * @date Apr 27, 2011 3:43:08 PM
	 * @param SheetName
	 * @param rowNo
	 *            行数
	 * @return Vector
	 * @throws
	 */
	public Vector getDataBySN(String SheetName, int rowNo, boolean sqlModel) {
		openFileStream();
		Vector retVect = new Vector(); // 返回vector
		HSSFSheet hssfSh = hssfwb.getSheet(SheetName);
		if (hssfSh != null) {
			int colCount = getColsCount(SheetName, rowNo); // 列数
			int thisFromRow = rowNo;
			getDataByRow(retVect, hssfSh, rowNo, colCount, sqlModel);
		}
		closeFileStream();
		return retVect;
	}

	/**
	 * 
	 * 方法描述：TODO 获取Excel行数据
	 * 
	 * @author 段思远
	 * @date May 5, 2011 9:38:10 AM
	 * @param columnList
	 *            列集合
	 * @param hssfSh
	 *            工作表
	 * @param thisFromRow
	 *            起始行
	 * @param colCount
	 *            列数
	 * @param sqlModel
	 *            void 是否是sql模式
	 * @throws
	 */
	private void getDataByRow(List columnList, HSSFSheet hssfSh, int thisFromRow, int colCount, boolean sqlModel) {
		String prefix = "";
		String suffix = "";
		if (sqlModel) {
			prefix = "'";
			suffix = "'";
		}
		DecimalFormat df = new DecimalFormat("##0.00##");
		int curRowNum = hssfSh.getPhysicalNumberOfRows(); // 行数
		List rowVect = null; // 存放excel一行数据
		for (int i = thisFromRow; i <= curRowNum; i++) { // 行循环
			rowVect = new Vector();
			HSSFRow hssfRow = hssfSh.getRow(i);
			if (hssfRow != null) { // 行数据不为空
				for (int j = 0; j < colCount; j++) { // 列循环
					HSSFCell hssfCell = hssfRow.getCell((short) j); // 单元数据
					if (hssfCell != null) {
						int cellType = hssfCell.getCellType(); // 单元格类型
						if (cellType == HSSFCell.CELL_TYPE_STRING) { // 字符串
							String str = hssfCell.getStringCellValue();
							str = (str == null) ? "" : str;
							// if (!str.equals(""))
							rowVect.add(prefix + str + suffix);

						} else if (cellType == HSSFCell.CELL_TYPE_FORMULA) {// 公式
							double doubleStr = hssfCell.getNumericCellValue();// 一般公式计算的结果都为数值型
							String douStr = new Double(doubleStr).toString();
							String str = df.format(doubleStr);
							if (!str.equals(""))
								rowVect.add(prefix + str + suffix);
						} else if (cellType == HSSFCell.CELL_TYPE_NUMERIC) // 数值型
							transCellDate(rowVect, hssfCell);
						else if (cellType == HSSFCell.CELL_TYPE_BLANK)
							rowVect.add(""); // 空白
					} else {
						rowVect.add("");
					}
					// else columnList.add(" ");
				}
			}
			columnList.add(rowVect.toArray());
		}
	}

	/**
	 * 
	 * 方法描述：TODO 获取Excel行数据
	 * 
	 * @author 段思远
	 * @date May 5, 2011 9:38:10 AM
	 * @param retVect
	 *            列集合
	 * @param hssfSh
	 *            工作表
	 * @param thisFromRow
	 *            起始行
	 * @param colCount
	 *            列数
	 * @param sqlModel
	 *            void 是否是sql模式
	 * @throws
	 */
	private void getDataByRow(Vector retVect, HSSFSheet hssfSh, int thisFromRow, int colCount, boolean sqlModel) {
		String prefix = "";
		String suffix = "";
		if (sqlModel) {
			prefix = "'";
			suffix = "'";
		}
		DecimalFormat df = new DecimalFormat("##0.00##");
		int curRowNum = hssfSh.getPhysicalNumberOfRows(); // 行数
		Vector rowVect = null; // 存放excel一行数据
		for (int i = thisFromRow; i <= curRowNum; i++) { // 行循环
			rowVect = new Vector();
			HSSFRow hssfRow = hssfSh.getRow(i);
			if (hssfRow != null) { // 行数据不为空
				for (int j = 0; j < colCount; j++) { // 列循环
					HSSFCell hssfCell = hssfRow.getCell((short) j); // 单元数据
					if (hssfCell != null) {
						int cellType = hssfCell.getCellType(); // 单元格类型
						if (cellType == HSSFCell.CELL_TYPE_STRING) { // 字符串
							String str = hssfCell.getStringCellValue();
							str = (str == null) ? "" : str;
							if (!str.equals(""))
								rowVect.add(prefix + str + suffix);
						} else if (cellType == HSSFCell.CELL_TYPE_FORMULA) {// 公式
							double doubleStr = hssfCell.getNumericCellValue();// 一般公式计算的结果都为数值型
							String douStr = new Double(doubleStr).toString();
							String str = df.format(doubleStr);
							if (!str.equals(""))
								rowVect.add(prefix + str + suffix);
						} else if (cellType == HSSFCell.CELL_TYPE_NUMERIC) // 数值型
							transCellDate(rowVect, hssfCell);

						else if (cellType == HSSFCell.CELL_TYPE_BLANK)
							rowVect.add(" "); // 空白
					} else
						rowVect.add(" ");
				}
			}
			retVect.add(rowVect);
		}
	}

	private void transCellDate(Vector rowVect, HSSFCell hssfCell) {
		if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
			Date date = (Date) hssfCell.getDateCellValue();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = format.format(date);
			rowVect.add(dateStr);
		} else {
			double doubleStr = hssfCell.getNumericCellValue();
//			String formatStr = doubleTransfer(doubleStr);
			rowVect.add(doubleStr);
		}
	}

	private void transCellDate(List rowVect, HSSFCell hssfCell) {
		if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
			Date date = (Date) hssfCell.getDateCellValue();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = format.format(date);
			rowVect.add(dateStr);
		} else {
			double doubleStr = hssfCell.getNumericCellValue();
			DecimalFormat format = (DecimalFormat) NumberFormat.getPercentInstance();
			format.applyPattern("0");
			String formatStr = format.format(doubleStr);
			rowVect.add(formatStr);
		}
	}

	public int getRowNum() {
		return rowNum;
	}

	private void closeFileStream() {
		try {
			if (fileOS != null) {
				fileOS.close();
				fileOS = null;
			}
			if (hssfwb != null) {
				hssfwb = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void openFileStream() {
		if (filePath != null) {
			try {
				fileOS = new FileInputStream(filePath);
				hssfwb = new HSSFWorkbook(fileOS);
			} catch (Exception e) {
				System.out.println("FounExcelConfig初始化失败！");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private String doubleTransfer(double dou) { // excel的double和int问题
		String retStr = null;
		if (new Double(dou) != null) {
			String str = new Double(dou).toString();
			String[] strs = str.split("\\.");

			if (strs != null && strs.length > 0) {
				if (strs[1].equals("0")) {
					retStr = strs[0];
				} else {
					retStr = str;
				}
			}
		}
		return retStr;
	}

	private String stringTransfer(String str) { // excel的double和int问题
		String retStr = null;
		if (str != null) {
			String[] strs = str.split("\\.");

			if (strs != null && strs.length > 0) {
				if (strs[1].equals("0") || strs[1].equals("00") || strs[1].equals("000") || strs[1].equals("0000")) {
					retStr = strs[0];
				} else {
					retStr = str;
				}
			}
		}
		return retStr;
	}
}
