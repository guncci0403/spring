package kr.or.ddit.user.repository;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.test.config.ModelTestConfig;
import kr.or.ddit.user.model.UserVo;

//eclipse / maven 자체에 내장 -> junit이 메인 메서드가 없는데 실행될 수 있는 이유
//스프링 환경에서 junit 코드를 실행 ==> junit 코드도 스프링 빈으로 등록
public class UserDaoTest extends ModelTestConfig{
	
	@Resource(name="userDao")
	private UserDao userDao;
	
	@Resource(name="dataSource")
	private DataSource dataSource;
	
	@Before
	public void setup() {
		//initData.sql을 실행 : 스프링에서 제공하는 ResourceDatabasePopulator
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		
		//populator를 통해 실행시킬 sql 파일을 지정
		populator.addScript(new ClassPathResource("/kr/or/ddit/config/db/initData.sql"));
		
		//script 파일을 실행하다 에러가 발생할 경우 더이상
		//진행하지 않고 멈추게끔 설정
		populator.setContinueOnError(false);
		
		//populator를 실행
		DatabasePopulatorUtils.execute(populator, dataSource);
		
	}
	
	
	
	@Test
	public void selectUserTest() {
		/***Given***/
		String userid = "brown";

		/***When***/
		UserVo userVo = userDao.selectUser(userid);

		/***Then***/
		assertEquals("브라운", userVo.getUsernm());
	}

	//전체 사용자 조회 테스트
	@Test
	public void selectAllUserTest() {
		/***Given***/
		
		/***When***/
		List<UserVo> userList = userDao.selectAllUser();

		/***Then***/
		assertEquals(20, userList.size());
	}

	
	//사용자 페이징 조회 테스트
	@Test
	public void selectPagingUserTest() {
		/***Given***/
		PageVo pageVo = new PageVo(2, 5);
		
		/***When***/
		List<UserVo> userList = userDao.selectPagingUser(pageVo);

		/***Then***/
		assertEquals(5, userList.size());
	}
	
	// 전체 사용자 수 조회
	@Test
	public void selectAllUserCntTest() {
		/***Given***/
		
		/***When***/
		int userCnt = userDao.selectAllUserCnt();

		/***Then***/
		assertEquals(19, userCnt);
	}
	
	@Test
	public void modifyUserTest() {
		/***Given***/
		UserVo userVo = new UserVo("ddit", "대덕인재", "dditpass", new Date(),
								   "개발원_m", "대전시 중구 중앙로 76", "4층 대덕인재개발원", "34940", "brown.png", "uuid-generated-filename.png");
		
		/***When***/
		int updateCnt = userDao.modifyUser(userVo);

		/***Then***/
		assertEquals(1, updateCnt);
	}
	
	@Test
	public void registUserTest() {
		/***Given***/
		UserVo userVo = new UserVo("ddit_n0", "대덕인재", "dditpass", new Date(),
								   "개발원_m", "대전시 중구 중앙로 76", "4층 대덕인재개발원", "34940", "brown.png", "uuid-generated-filename.png");
		
		/***When***/
		int insertCnt = userDao.registUser(userVo);

		/***Then***/
		assertEquals(1, insertCnt);
	}

	//삭제 테스트
	@Test
	public void deleteUserTest() {
		/***Given***/
		String userid = "ddit_n0";

		/***When***/
		int deleteCnt = userDao.deleteUser(userid);
		/***Then***/
		assertEquals(1, deleteCnt);
	}
}
