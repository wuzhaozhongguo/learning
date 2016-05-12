package aa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUitl {
	
	public static String getDateFormat(Date date){
		Date now = new Date();
		
		Calendar inputDate = Calendar.getInstance();
		inputDate.setTime(date);
		
		Calendar nowDate = Calendar.getInstance();
		nowDate.setTime(now);
		
		int yearOfInput = inputDate.get(Calendar.YEAR);
		int monthOfInput = inputDate.get(Calendar.MONTH);
		int weekOfInput = inputDate.get(Calendar.WEEK_OF_YEAR);
		int dayOfInput = inputDate.get(Calendar.DAY_OF_WEEK);
		
		int yearOfNow = nowDate.get(Calendar.YEAR);
		int monthOfNow = nowDate.get(Calendar.MONTH);
		int weekOfNow = nowDate.get(Calendar.WEEK_OF_YEAR);
		int dayOfNow = nowDate.get(Calendar.DAY_OF_WEEK);
		
		if (yearOfInput < yearOfNow) {//今年以前的
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.format(inputDate.getTime());
		}else if(yearOfInput == yearOfNow){//今年的
			if(weekOfInput < weekOfNow){//上周的
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
				return sdf.format(inputDate.getTime());
			}else if(weekOfInput == weekOfNow){//本周的
				if (dayOfNow - dayOfInput == 1) {//昨天的
					return "昨天";
				}else if (dayOfNow == dayOfInput) {//今天的
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					return sdf.format(inputDate.getTime());
				}else{//本周其他的
					String [] dayStr = {"日","一","二","三","四","五"};
					return dayStr[dayOfInput - 1];
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(DateUitl.getDateFormat(new Date("2016/05/8")));
	}
}
