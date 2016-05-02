package com.togb.finance;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.togb.finance.margin_trading.DownloadHTML;
import com.togb.finance.margin_trading.HTMLTableToExcel;
import com.togb.finance.trade_history.CSVToExcel;
import com.togb.finance.trade_history.DownloadHistoryCSV;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Hello world!
 *
 */
public class App {
	private DownloadHistoryCSV downloadCSV = new DownloadHistoryCSV();
	private CSVToExcel csvToExcel = new CSVToExcel();
	private DownloadHTML downloadHTML = new DownloadHTML();
	private HTMLTableToExcel htmlTableToExcel = new HTMLTableToExcel();
	private ArrayList<String> mStockList = new ArrayList<String>();
	
	public static void main(String[] args) {
		new App().doTask();
	}
	
	private void doTask(){
		setStockList();
		
		htmlTableToExcel.doAction(mStockList)
		.subscribe();
/*		downloadHTML.doDownLoad(mStockList).subscribe();
		downloadCSV.doAction(mStockList).subscribe();
		csvToExcel.getCSV().subscribe(new Action1<File>() {

			public void call(File t) {
				System.out.println(t.getName());
				
			}
		});*/
	}
	
	private void setStockList(){
		File xls = new File("stock.xlsx");
		FileInputStream fis;
		try {
			fis = new FileInputStream(xls);
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
			XSSFSheet mySheet = myWorkBook.getSheetAt(0);
			int rows; // No of rows
			rows = mySheet.getPhysicalNumberOfRows();

			for (int r = 0; r < rows; r++) {
				XSSFRow row = mySheet.getRow(r);
				if (row != null) {
					XSSFCell cell = row.getCell(0);
					//subscriber.onNext(cell.getStringCellValue());
					mStockList.add(cell.getStringCellValue());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
