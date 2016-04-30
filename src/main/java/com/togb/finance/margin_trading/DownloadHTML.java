package com.togb.finance.margin_trading;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class DownloadHTML {
	public static final String TEMP_FLODER = "temp_margin_trading/";
	public static final String URL = "http://data.10jqka.com.cn/market/rzrqgg/code/%s/order/desc/page/%d/ajax/1/";
	
	public Observable<File> doDownLoad(ArrayList<String> stockList) {
		return Observable.from(stockList)
				.flatMap(new Func1<String, Observable<File>>() {

					public Observable<File> call(final String stockNo) {
						// TODO Auto-generated method stub
						return Observable.create(new OnSubscribe<File>(){

							public void call(Subscriber<? super File> subscriber) {
								// TODO Auto-generated method stub
								File file = new File(TEMP_FLODER + stockNo +"/");
								if (!file.exists()){
									file.mkdirs();
								}
								downloadDetail(stockNo, 1).subscribe(new Subscriber<File>() {

									public void onCompleted() {
										// TODO Auto-generated method stub
										
									}

									public void onError(Throwable e) {
										// TODO Auto-generated method stub
										
									}

									public void onNext(File file) {
										
										
									}
								});
							}
							
						});
					}
				});
	}
	
	private Observable<File> downloadDetail(final String stockNo,final int pageIndex) {
		return Observable.create(new OnSubscribe<File>(){

			public void call(Subscriber<? super File> subscriber) {
				try {
					File pageFile = new File(TEMP_FLODER + stockNo +"/" + pageIndex +".html");
					URL website = new URL(String.format(URL, stockNo,pageIndex));
					FileUtils.copyURLToFile(website, pageFile);
					subscriber.onNext(pageFile);
					subscriber.onCompleted();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					subscriber.onError(e);
				}
				
			}
			
		}).retry(3);
	}

}
