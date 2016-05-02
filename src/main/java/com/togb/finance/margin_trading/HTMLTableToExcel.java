package com.togb.finance.margin_trading;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.formula.functions.Rows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;import org.apache.xmlbeans.impl.xb.xsdschema.FractionDigitsDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.functions.Func1;
import rx.Subscriber;

public class HTMLTableToExcel {
	private static final String OUT_PATH = "融资融券/";
	public Observable<File> doAction(ArrayList<String> stockList){
		prepareFolder();
		return Observable.from(stockList)
				.flatMap(new Func1<String, Observable<? extends File>>() {

					public Observable<? extends File> call(final String stockNo) {
						
						return Observable.create(new OnSubscribe<File>() {

							public void call(Subscriber<? super File> subscriber) {
								File firstHTML = new File(DownloadHTML.TEMP_FLODER + stockNo+"/1.html");
								if (!firstHTML.exists()){
									subscriber.onCompleted();
									return;
								}
								try {
									Document document = Jsoup.parse(firstHTML, "GB2312");
									if (document.select("tbody tr").size()<1){
										subscriber.onCompleted();
										return;
									}
									XSSFWorkbook wb = new XSSFWorkbook();
									XSSFSheet sheet = wb.createSheet("Data") ;
									XSSFCellStyle headerStyle = wb.createCellStyle();
									headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
									int rowIndex = 0;
									XSSFRow row = sheet.createRow(rowIndex);
									XSSFCell cell = row.createCell(0);
									cell.setCellValue("序号");
									sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, 0, 0));
									cell = row.createCell(1);
									cell.setCellValue("交易时间");
									sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, 1, 1));
									
									cell = row.createCell(2);
									cell.setCellValue("融资(元)");
									sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 2+3));
									cell.setCellStyle(headerStyle);
									cell = row.createCell(6);
									cell.setCellValue("融券(万股)");
									sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 6+3));
									cell.setCellStyle(headerStyle);
									
									cell = row.createCell(10);
									cell.setCellValue("融资融券余额\n(元)");
									sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex+1, 10, 10));
									
									++rowIndex;
									row = sheet.createRow(rowIndex);
									cell = row.createCell(2);
									cell.setCellValue("余额");
									cell = row.createCell(3);
									cell.setCellValue("买入额");
									cell = row.createCell(4);
									cell.setCellValue("偿还额");
									cell = row.createCell(5);
									cell.setCellValue("融资净买入");
									cell = row.createCell(6);
									cell.setCellValue("余量");
	
									cell = row.createCell(7);
									cell.setCellValue("卖出量");
									
									cell = row.createCell(8);
									cell.setCellValue("偿还量");
									
									cell = row.createCell(9);
									cell.setCellValue("融券净卖出");
									
									int pageIndex = 1;
									File file = new File(DownloadHTML.TEMP_FLODER + stockNo+"/"+ pageIndex + ".html");
									while (file.exists()){
										document = Jsoup.parse(file, "GB2312");
										Elements datarows = document.select("tbody tr");
										for(Element rowE :datarows){
											row = sheet.createRow(++rowIndex);
											Elements dataCells = rowE.select("td");
											int colIndex = 0;
											for(Element element :dataCells){
												cell = row.createCell(colIndex ++);
												cell.setCellValue(element.text());
											}
										}
										
										
										pageIndex++;
										file = new File(DownloadHTML.TEMP_FLODER + stockNo +"/"+pageIndex + ".html");
									}
									
									
									
									File outFile = new File(OUT_PATH +stockNo+".xlsx");
									FileOutputStream fileOut = new FileOutputStream(outFile);
									wb.write(fileOut);
									fileOut.flush();
									fileOut.close();
									
									
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									subscriber.onError(e);
								}
								
							}
						});

					
						
					}
				});
		
		
		
	}
	private void prepareFolder() {
		File file = new File(OUT_PATH);
		if (!file.exists()){
			file.mkdirs();
		}
	}
}
