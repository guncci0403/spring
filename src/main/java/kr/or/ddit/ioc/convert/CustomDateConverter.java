package kr.or.ddit.ioc.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class CustomDateConverter implements Converter<String, Date>{

	private String dateFormat;
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}



	//source : 2021-01-11, yyyy-mm-dd
	@Override
	public Date convert(String source) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = null;
		try {
			date = sdf.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	
}
