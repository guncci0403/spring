package kr.or.ddit.batch.yogurt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import kr.or.ddit.yogurt.model.CycleVo;
import kr.or.ddit.yogurt.model.DailyVo;

public class YogurtProcessor implements ItemProcessor<CycleVo, List<DailyVo>>{

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	@Value("#{jobParameters[dt]}")
	private Date dt; 
	//private String dt;		// 202102
	
	//dt : 202102, item : cid-1, pid-100, day-2, cnt-1
	//==>
	//					  cid-1, pid-100, dt-20210201, cnt-1
	//		  			  cid-1, pid-100, dt-20210208, cnt-1
	//		              cid-1, pid-100, dt-20210215, cnt-1
	//		              cid-1, pid-100, dt-20210222, cnt-1
	
	// 1일~28일 loop
	// if(요일 == item.요일과 같은지 체크)
	//  	해당 일자로 일실적 데이터를 생성
	//
	
	// 해당년월의 마지막 날짜 (date)
	// 해당년월의 첫번째 날짜- 1일 (date)
	
	@Override
	public List<DailyVo> process(CycleVo item) throws Exception {
		
		//현재 날짜 시간
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(dt);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDt = calendar.getTime(); 
		// 20210228 02:00:00
		
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		
		//20210201 00:00:00.5
		//Date startDt = calendar.getTime();
		
		List<DailyVo> dailyVoList = new ArrayList<DailyVo>();
		while(endDt.compareTo(calendar.getTime()) > 0) {
			
			//20210201 == > 주간요일
			if(item.getDay() == calendar.get(Calendar.DAY_OF_WEEK)) {
				//cid, pid, dt(yyyyMMdd), cnt
				dailyVoList.add(new DailyVo(item.getCid(), 
											item.getPid(), 
											sdf.format(calendar.getTime()), 
											item.getCnt()));
			}
			
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		}
		
		return dailyVoList;
	}

}
