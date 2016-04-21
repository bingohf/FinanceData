package com.togb.finance;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import rx.Observable;
import rx.functions.Func1;

public class CSVToExcel {
	
	public Observable<File> getCSV(){
		File f = new File(DownloadCSV.TEMP_FOLDER);
		File[] matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith("csv");
		    }
		});
		return Observable.from(matchingFiles)
				.map(new Func1<File, File>() {

					public File call(File file) {
						// TODO Auto-generated method stub
						 CSVParser parser;
						try {
							System.out.println(new String(Files.readAllBytes(Paths.get(file.getPath())),"GB2312"));
							parser = CSVParser.parse(file,StandardCharsets.ISO_8859_1, CSVFormat.RFC4180);
							 for (CSVRecord csvRecord : parser) {
								 System.out.println(csvRecord.get(1));
							 }
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						 return file;
					}
				});
	}
}
