package kr.or.ddit.hello;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.annotation.Resource;

import org.junit.Before;

/*
 *  java - gui swing, awt, java fx 
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import kr.or.ddit.test.config.WebTestConfig;
import kr.or.ddit.user.model.UserVo;


public class HelloControllerTest extends WebTestConfig{
   
   //helloController 으로 리소스를 받았는데 그곳으로 가보면 @Resource(name="userService")와 userDao 두개가 필요해서
   // @ContextConfiguration(locations = 를 두개 적어야한다 기능을 xml에 적어논곳을 불러와서 기능이 사용될 수 있게한다.
//   @Resource(name="helloController")
//   private HelloController helloController;
      
   //스프링 빈 중에 대입 가능한 타입의 스프링 빈을 주입한다
   //만약 동일한 타입의 스프링 빈이 여러개 있을 경우 @Qulifier 어노테이션을 통해
   // 특정 스프링 빈의 이름을 지칭할 수 있다.
   //     ==>Resource 어노테이션 하나를 사용했을 때와 동일
   
   //  DispatcherServlet ==> MockMvc 생성을 위해서
   //                    WebApplicationContext 객체가 필요
   //                  ==> 스프링 프레임워크에서 ioc 컨테이너 생성시 자동으로 생성
   
   
   
   //localhost/hello/view
   @Test
   public void viewTest() throws Exception {
      MvcResult mvcResult = mockMvc.perform(get("/hello/view"))
    		  					.andExpect(status().isOk())
							    .andExpect(view().name("hello"))
							    .andExpect(model().attributeExists("userVo"))
							    .andDo(print())
							    .andReturn();
      
      ModelAndView mav = mvcResult.getModelAndView();
      
      assertEquals("hello", mav.getViewName());
      
      UserVo userVo = (UserVo)mav.getModel().get("userVo");
      assertEquals("브라운", userVo.getUsernm());
      
   }
   
   @Test
   public void pathVariableTest() throws Exception {
	   MvcResult mvcResult = mockMvc.perform(get("/hello/path/cony"))
								.andExpect(status().isOk())
							    .andExpect(model().attributeExists("subpath"))
							    .andDo(print())
							    .andReturn();

   }

}