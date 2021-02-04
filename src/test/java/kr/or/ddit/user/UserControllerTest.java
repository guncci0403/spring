package kr.or.ddit.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import kr.or.ddit.test.config.WebTestConfig;
import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.service.UserService;

public class UserControllerTest extends WebTestConfig{
	
	@Resource(name="userService")
	private UserService userService;
	
	@Before
	public void setup() {
		super.setup();
		// 테스트에서 사용할 신규 사용자 추가
		UserVo userVo = new UserVo("testUser", "테스트사용자", "testUserPass", new Date(), "대덕", "대전 중구 중앙로 76", "4층",
				"34940", "brown.png", "uuid-generated-filename.png");

		userService.registUser(userVo);

		// 신규 입력 테스트를 위해 테스트 과정에서 입력된 데이터를 삭제
		userService.deleteUser("ddit_n");
	}

	@After
	public void tearDodwn() {
		userService.deleteUser("testUser");
	}
	
	
	
	@Test
	public void allUserTest() throws Exception{
		mockMvc.perform(get("/user/allUser"))
						.andExpect(view().name("user/allUser"))
						.andExpect(status().isOk())
						.andExpect(model().attributeExists("userList"))
						.andDo(print());
	}
	
	@Test
	public void pagingUserTest() throws Exception {
		mockMvc.perform(get("/user/pagingUser"))
					.andExpect(view().name("user/pagingUser"))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("userList"))
					.andExpect(model().attributeExists("pageVo"))
					.andExpect(model().attributeExists("pagination"))
					.andDo(print());
	}
	
	@Test
	public void pagingUser2pageTest() throws Exception {
		mockMvc.perform(get("/user/pagingUser").param("page", "2"))
					.andExpect(view().name("user/pagingUser"))
					.andExpect(status().isOk())
					.andExpect(model().attributeExists("userList"))
					.andExpect(model().attributeExists("pageVo"))
					.andExpect(model().attributeExists("pagination"))
					.andDo(print());
	}
	@Test
	public void userTest() throws Exception {
		mockMvc.perform(get("/user/userSelect").param("userid", "sally"))
//			.andExpect(view().name("user/userSelect"))
			.andExpect(view().name("tiles.user.userSelect"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("user"))
			.andDo(print());
	}
	
	@Test
	public void userModifyGetTest() throws Exception {
		mockMvc.perform(get("/user/userModify").param("userid", "sally"))
			.andExpect(view().name("user/userModify"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("user"))
			.andDo(print());
	}
	
	@Test
	public void userModifyPostTest() throws Exception {
		
		ClassPathResource resource = new ClassPathResource("kr/or/ddit/upload/sally.png");
		
		MockMultipartFile file = 
				new MockMultipartFile("profile", "sally.png", "image/png",
									   resource.getInputStream());
		
		mockMvc.perform(fileUpload("/user/userModify")
						.file(file)
						.param("userid", "testUser")
						.param("usernm", "대덕수정")
						.param("alias", "영민빌딩")
						.param("pass", "ddit_newpass")
						.param("alais", "영민빌딩")
//						.param("reg_dt", "2021-01-13")
						.param("addr1", "대전 중구 중앙로 76")
						.param("addr2", "4층")
						.param("zipcode", "34940") )
			.andExpect(view().name("redirect:/user/userSelect"))
			.andExpect(status().is3xxRedirection())
			.andExpect(model().attributeExists("userid"))
			.andDo(print());
	}
	
	@Test
	public void userRegistGetTest() throws Exception {
		mockMvc.perform(get("/user/userRegist"))
			.andExpect(view().name("tiles.user.userRegist"))
			.andExpect(status().isOk())
			.andDo(print());
	}
	
	@Test
	public void userRegistPostTest() throws Exception {
		
		ClassPathResource resource = new ClassPathResource("kr/or/ddit/upload/sally.png");
		
		MockMultipartFile file = 
				new MockMultipartFile("profile", "sally.png", "image/png",
									   resource.getInputStream());
		
		mockMvc.perform(fileUpload("/user/userRegist")
						.file(file)
						.param("userid", "ddit_n")
						.param("usernm", "대덕수정")
						.param("alias", "영민빌딩")
						.param("pass", "ddit_newpass")
						.param("alais", "영민빌딩")
						.param("reg_dt", "2021-01-13")
						.param("addr1", "대전 중구 중앙로 76")
						.param("addr2", "4층")
						.param("zipcode", "34940") )
			.andExpect(view().name("redirect:/user/pagingUser"))
			.andExpect(status().is3xxRedirection())
			.andDo(print());
	}
	
	@Test
	public void userDeleteTest() throws Exception {
		mockMvc.perform(post("/user/deleteUser")
						.param("userid", "testUser"))
				.andExpect(view().name("redirect:/user/pagingUser"))
				.andExpect(status().is3xxRedirection())
				.andDo(print());
	}
}