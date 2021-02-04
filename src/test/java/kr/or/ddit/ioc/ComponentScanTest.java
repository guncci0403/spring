package kr.or.ddit.ioc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.or.ddit.test.config.ModelTestConfig;
import kr.or.ddit.test.config.WebTestConfig;
import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.repository.UserDao;
import kr.or.ddit.user.service.UserService;

@ContextConfiguration(locations = {"classpath:/kr/or/ddit/ioc/component-scan.xml",
"classpath:/kr/or/ddit/config/spring/datasource-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ComponentScanTest{
	
	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="userService")
	private UserService userService;
	
	//@Repository 어노테이션을 적용한 userDaoImpl 스프링 빈이 정상적으로 컨테이너에 등록 되었는지 확인
	@Test
	public void userDaoImplSpringBeanTest() {
		assertNotNull(userDao);
		
		UserVo userVo = userDao.selectUser("brown");
		assertEquals("브라운", userVo.getUsernm());
	}

	//userServiceImpl 스프링 빈이 정상적으로 컨테이너에 등록 되었는지 확인
	@Test
	public void userServiceImplSpringBeanTest() {
		assertNotNull(userService);
		
		UserVo userVo = userService.selectUser("brown");
		assertEquals("브라운", userVo.getUsernm());
	}
}
