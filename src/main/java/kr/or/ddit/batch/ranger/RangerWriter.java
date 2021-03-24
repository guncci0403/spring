package kr.or.ddit.batch.ranger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

public class RangerWriter implements ItemWriter<String>{
	private static final Logger logger = LoggerFactory.getLogger(RangerWriter.class);

	@Override
	public void write(List<? extends String> items) throws Exception {
		for(String item : items) {
			logger.debug("writer : {}", item);
		}
		
	}
	
}
