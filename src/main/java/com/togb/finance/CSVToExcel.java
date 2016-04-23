package com.togb.finance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rx.Observable;
import rx.functions.Func1;

public class CSVToExcel {
	public static final String OUT_PUT_FOLDER = "历史交易数据/";
	
	
	public Observable<File> getCSV(){
		File f = new File(DownloadCSV.TEMP_FOLDER);
		File[] matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith("csv");
		    }
		});
		prepareOutPutFolder();
		return Observable.from(matchingFiles)
				.map(new Func1<File, File>() {

					public File call(File file) {
						// TODO Auto-generated method stub
						 CSVParser parser;
						 File outFile = new File(OUT_PUT_FOLDER + file.getName().replace("csv", "xlsx"));
						try {
							XSSFWorkbook wb = new XSSFWorkbook();
							XSSFSheet sheet = wb.createSheet("Data") ;
							
							parser = CSVParser.parse(file,Charset.forName("GB2312"), CSVFormat.RFC4180);
							Iterator<CSVRecord> rowit = parser.iterator();
							//title
							int rowIndex = 0;
							if (rowit.hasNext()){
								CSVRecord record = rowit.next();
								XSSFRow row = sheet.createRow(rowIndex ++);
								int index  =0;
								for(String col: record){
									XSSFCell cell = row.createCell(index ++);
									cell.setCellValue(col);
								}
							}
							//data
							while (rowit.hasNext()){
								CSVRecord record = rowit.next();
								XSSFRow row = sheet.createRow(rowIndex ++);
								int index  =0;
								for(String col: record){
									XSSFCell cell = row.createCell(index ++);
									Object val = MUtils.tranValue(col);
									if (val instanceof Double){
										cell.setCellValue(((Double)val).doubleValue());
									}else if (val instanceof Date){
										cell.setCellValue((Date)val);
									}else{
							
										cell.setCellValue((String)val);
									}
								}
							}
							
							FileOutputStream fileOut = new FileOutputStream(outFile);
							wb.write(fileOut);
							fileOut.flush();
							fileOut.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 return outFile;

						
					}
				});
	}
	private void prepareOutPutFolder(){
		File file = new File(OUT_PUT_FOLDER);
		if (!file.exists()){
			file.mkdirs();
		}
	}
}
