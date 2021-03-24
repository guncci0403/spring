package kr.or.ddit.batch.ranger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class RangerProcessor implements ItemProcessor<String, String>{

	private static final Logger logger = LoggerFactory.getLogger(RangerProcessor.class);

	//brown => sally => cony => moon => james
	@Override
	public String process(String item) throws Exception {
		
		logger.debug("processor {} ==> {}", item, item + "modified");
		
		return item + "modified"; 
	}
	

}
