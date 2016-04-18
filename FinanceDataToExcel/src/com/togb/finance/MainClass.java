package com.togb.finance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainClass {
	public final static String TEMP_FOLDER = "temp/";
	private ArrayList<String> stocks = new ArrayList<>();
	private final static String CSV_URL = "http://quotes.money.163.com/service/chddata.html?code=%s&start=19900101&end=20160418&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new MainClass().applicationStart();
		
	}

	private void applicationStart(){
		createTempFolder();
		loadInputList();
		downloadToTemp();
		
		
		
	}
	private void downloadToTemp() {
		// TODO Auto-generated method stub
		
		
		Observable.from(stocks).flatMap(new Func1<String, Observable<String>>(){

			@Override
			public Observable<String> call(String stockNo) {
				// TODO Auto-generated method stub
				return Observable.create(new OnSubscribe<String>(){

					@Override
					public void call(Subscriber<? super String> subscriber) {
						// TODO Auto-generated method stub
						try {
						
							URL website = new URL(String.format(CSV_URL, stockNo));
							FileUtils.copyURLToFile(website, new File(TEMP_FOLDER + stockNo +".csv"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							subscriber.onError(e);
						}
						subscriber.onNext(stockNo);
						subscriber.onCompleted();
					}
					
				}).retry(4);
			}
			
		}).subscribe(new Action1<String>(){

			@Override
			public void call(String arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0);
			}
			
		});
	}

	private void loadInputList() {
		// TODO Auto-generated method stub
		stocks.add("0600030");
	}

	private void createTempFolder() {
		// TODO Auto-generated method stub
		File file = new File(TEMP_FOLDER);
		if (!file.exists()){
			file.mkdirs();
		}
	}

}
