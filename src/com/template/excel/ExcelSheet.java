package com.template.excel;

import java.util.List;

/**
 * 方便导出为excel
 * @author yunlu
 *
 */
public class ExcelSheet {
	
	private String sheetName;
	private List<String> headerList;
	private List<List> dataList;
	
	public ExcelSheet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ExcelSheet(String sheetName, List<String> headerList, List<List> dataList) {
		super();
		this.sheetName = sheetName;
		this.headerList = headerList;
		this.dataList = dataList;
	}
	
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public List<String> getHeaderList() {
		return headerList;
	}
	public void setHeaderList(List<String> headerList) {
		this.headerList = headerList;
	}
	public List<List> getDataList() {
		return dataList;
	}
	public void setDataList(List<List> dataList) {
		this.dataList = dataList;
	}
	
	public void addData(List data){
		if(dataList==null){
			return;
		}
		dataList.add(data);
	}
	
	public boolean empty(){
		return dataList.size()==0;
	}
}
