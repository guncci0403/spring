package kr.or.ddit.test.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration      //Spring환경을 web기반의 application Context로 생성
@ContextConfiguration(locations = {"classpath:/kr/or/ddit/config/spring/application-context.xml",
					   			   "classpath:/kr/or/ddit/config/spring/root-context.xml",
					   				"classpath:/kr/or/ddit/config/spring/datasource-context.xml"})
public class WebTestConfig {

	@Autowired
	private WebApplicationContext context;
	
	protected MockMvc mockMvc; 
	
	@Before
	public void setup() {
	
		//초기화 작업
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Ignore
	@Test
	public void dummy() {
		
	}
}
