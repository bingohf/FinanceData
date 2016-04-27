package com.togb.finance.trade_history;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.Today;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.functions.Func1;
import rx.Subscriber;

public class DownloadHistoryCSV {
	
	private final static String CSV_URL = "http://quotes.money.163.com/service/chddata.html?code=%s&start=19900101&end=%s&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP";
	public final static String TEMP_FOLDER = "temp_history/";
	private static String today;
	private ArrayList<String> stocks = new ArrayList<String>();

	static{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		today = simpleDateFormat.format(new Date());
	}
	public Observable<File> doAction(ArrayList<String> stockNo) {
		createTempFolder();
		return Observable.from(stockNo).flatMap(new Func1<String, Observable<File>>() {

			public Observable<File> call(final String stockNo) {
				return Observable.create(new OnSubscribe<File>(){

					public void call(Subscriber<? super File> subscriber) {
						// TODO Auto-generated method stub
						try {
							System.out.println("start download\t" + stockNo);
							String code = stockNo;
							if (code.startsWith("60")){
								code = "0" + code;
							}else if (code.startsWith("00")){
								code ="1" + code;
							}
						
							URL website = new URL(String.format(CSV_URL, code,today));
							File file = new File(TEMP_FOLDER + stockNo +".csv");
							FileUtils.copyURLToFile(website, file);
							subscriber.onNext(file);
							subscriber.onCompleted();
							System.out.println("done download\t" + stockNo);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							subscriber.onError(e);
						}
					}
					
				}).retry(4);
				
			}
		});
	}
	
	private void createTempFolder() {
		// TODO Auto-generated method stub
		File file = new File(TEMP_FOLDER);
		if (!file.exists()){
			file.mkdirs();
		}
	}
}
