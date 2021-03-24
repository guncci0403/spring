package kr.or.ddit.batch.ranger;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration({"classpath:/kr/or/ddit/config/spring/batch-context.xml",
					   "classpath:/kr/or/ddit/config/spring/scheduler-context.xml",
					   "classpath:/kr/or/ddit/config/spring/batch-test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class RangerBatchTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	
	
	@Test
	public void rangerTaskTest() throws Exception{
		//batch 테스트시  job 타입으로 등록된 스프링 빈은 하나여야 된다
		//job 이름을 명시하지 않아도  container에 하나의 배치 job이 등록되어 있다는
		//가정이 있기 때문에 별도로 job 이름을 명시하지 않는다
		JobExecution execution = jobLauncherTestUtils.launchJob();
		execution.getExitStatus();
		
		assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
	}

}
