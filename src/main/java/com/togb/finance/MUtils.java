package com.togb.finance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class MUtils {
	public static final Pattern  REGEX_NUMERIC = Pattern.compile("^\\-?\\d+(\\.\\d+)?$");
	public static final Pattern  REGEX_DATE = Pattern.compile("^\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d$");
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Object tranValue(String strValue) {
		if (strValue.startsWith("'")){
			return strValue.substring(1);
		}
		if(REGEX_NUMERIC.matcher(strValue).matches()){
			return Double.parseDouble(strValue);
		}
		
/*		if (REGEX_DATE.matcher(strValue).matches()){
			try {
				Date date = DATE_FORMAT.parse(strValue);
				return date;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		return strValue;
	}
}
