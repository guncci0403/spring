package kr.or.ddit.batch.yogurt;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class YogurtBatchMain {
	public static void main(String[] args) {
		ApplicationContext context = 
				new ClassPathXmlApplicationContext(
					"classpath:/kr/or/ddit/config/spring/yogurt-batch-context.xml");
		
		JobLauncher jobLauncher = context.getBean("jobLauncher", JobLauncher.class);
		Job yogurtJob= context.getBean("yogurtJob", Job.class);
		
		try {
			jobLauncher.run(yogurtJob, new JobParametersBuilder()
										.addDate("dt", new Date())
										.toJobParameters());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}
}







