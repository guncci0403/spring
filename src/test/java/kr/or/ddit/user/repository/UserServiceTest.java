package kr.or.ddit.user.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.test.config.ModelTestConfig;
import kr.or.ddit.test.config.WebTestConfig;
import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.service.UserService;

//eclipse / maven 자체에 내장 -> junit이 메인 메서드가 없는데 실행될 수 있는 이유
//스프링 환경에서 junit 코드를 실행 ==> junit 코드도 스프링 빈으로 등록
public class UserServiceTest extends ModelTestConfig{
	
	@Resource(name="userService")
	private UserService userService;
	
	@Test
	public void selectUserTest() {
		/***Given***/
		String userid = "brown";

		/***When***/
		UserVo userVo = userService.selectUser(userid);

		/***Then***/
		assertEquals("브라운", userVo.getUsernm());
	}
	
	
	//전체 사용자 조회 테스트
	@Test
	public void selectAllUserTest() {
		/***Given***/
		
		/***When***/
		List<UserVo> userList = userService.selectAllUser();

		/***Then***/
		assertEquals(20, userList.size());
	}
	
	//사용자 아이디가 존재하지 않을 때, 특정 사용자 조회
	@Test
	public void selectUserNotExsistTest() {
		/***Given***/
		String userid = "brownNotexists";

		/***When***/
		UserVo user	= userService.selectUser(userid);

		/***Then***/
		assertNull(user);
	}
	
	
	//사용자 페이징 조회 테스트
	@Test
	public void selectPagingUserTest() {
		/***Given***/
		PageVo pageVo = new PageVo(2, 5);
		
		/***When***/
		Map<String, Object> map = userService.selectPagingUser(pageVo);
		List<UserVo> userList = (List<UserVo>)map.get("userList");
		//int userCnt = (int)map.get("userCnt");

		/***Then***/
		assertEquals(5, userList.size());
		//assertEquals(19, userCnt);
	}
	
	@Test
	public void modifyUserTest() {
		/***Given***/
		UserVo userVo = new UserVo("ddit", "대덕인재", "dditpass", new Date(),
								   "개발원_m", "대전시 중구 중앙로 76", "4층 대덕인재개발원", "34940", "brown.png", "uuid-generated-filename.png");
		
		/***When***/
		int updateCnt = userService.modifyUser(userVo);

		/***Then***/
		assertEquals(1, updateCnt);
	}
	
	@Test
	public void registUserTest() {
		/***Given***/
		UserVo userVo = new UserVo("ddit_n4", "대덕인재", "dditpass", new Date(),
								   "개발원_m", "대전시 중구 중앙로 76", "4층 대덕인재개발원", "34940", "brown.png", "uuid-generated-filename.png");
		
		/***When***/
		int insertCnt = userService.registUser(userVo);

		/***Then***/
		assertEquals(1, insertCnt);
	}
	
	@Test
	public void deleteUserTest() {
		/***Given***/
		String userid = "ddit_n4";

		/***When***/
		int deleteCnt = userService.deleteUser(userid);
		/***Then***/
		assertEquals(1, deleteCnt);
	}
}
