package kr.or.ddit.user.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.classic.Logger;
import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.service.UserService;
import kr.or.ddit.validator.UserVoValidator;

@RequestMapping("user")
@Controller
public class UserController {
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

	@Resource(name="userService")
	private UserService userService;

	
	@RequestMapping(path = "allUser", method = RequestMethod.GET) 
	public String allUser(Model model) {
	  
		model.addAttribute("userList", userService.selectAllUser());
		
		return "user/allUser";
	
	}
	@RequestMapping(path = "allUserTiles", method = RequestMethod.GET) 
	public String allUserTiles(Model model) {
		
		model.addAttribute("userList", userService.selectAllUser());
		
		return "tiles.user.allUser";
		
	}
	
	@RequestMapping("pagingUser")
	public String pagingUser(@RequestParam(defaultValue = "1") int page,
							@RequestParam(defaultValue = "5") int pageSize,
							Model model) {
	
		PageVo pageVo  = new PageVo(page, pageSize);
		
		//Map<String, Object> map = userService.selectPagingUser(pageVo);
		
		model.addAllAttributes(userService.selectPagingUser(pageVo));
		
		return "user/pagingUser";
	}
	
	@RequestMapping("pagingUserTiles")
	public String pagingUserTiles(@RequestParam(defaultValue = "1") int page,
								@RequestParam(defaultValue = "5") int pageSize,
								Model model) {
		
		PageVo pageVo  = new PageVo(page, pageSize);
		
		model.addAllAttributes(userService.selectPagingUser(pageVo));
		
		return "tiles.user.pagingUser";
	}
	
	//사용자 리스트 없는 상태의 화면만 응답으로 생성
	@RequestMapping("pagingUserAjaxView")
	public String pagingUserAjaxView() {
		return "tiles.user.pagingUserAjax";
	}
	
	@RequestMapping("pagingUserAjax")
	public String pagingUserAjax(@RequestParam(defaultValue = "1") int page,
								@RequestParam(defaultValue = "5") int pageSize,
								Model model) {
		
		PageVo pageVo  = new PageVo(page, pageSize);
		
		model.addAllAttributes(userService.selectPagingUser(pageVo));
		
		return "jsonView";
	}
	
	@RequestMapping("pagingUserAjaxHtml")
	public String pagingUserAjaxHtml(@RequestParam(defaultValue = "1") int page,
								@RequestParam(defaultValue = "5") int pageSize,
								Model model) {
		
		PageVo pageVo  = new PageVo(page, pageSize);
		
		model.addAllAttributes(userService.selectPagingUser(pageVo));
		
		return "user/pagingUserAjaxHtml";
	}
	/*
	 * pagingUserAjaxHtml ==> /WEB-INF/views/user/pagingUserAjaxHtml.jsp
	 */
	
	//@RequestMapping("pagingUser")
	public String pagingUser(PageVo pageVo) {
		
		logger.debug("pageVo : {}", pageVo);
		
		return "";
	}
	
	@RequestMapping(path="userSelect", method = RequestMethod.GET)
	public String UserSelect(String userid, Model model) {
		
		logger.debug("userid : {}", userid);
		model.addAttribute("user", userService.selectUser(userid));
		
		return "tiles.user.userSelect";
		//return "user/userSelect";
	}
	
	@RequestMapping(path="userModify", method = RequestMethod.GET)
	public String UserModify(String userid, Model model) {
		
		model.addAttribute("user", userService.selectUser(userid));
		
		return "user/userModify";
	}
	
	@RequestMapping(path="userModify", method = RequestMethod.POST)
	public String UserModify(UserVo userVo, MultipartFile profile, Model model, RedirectAttributes ra) {
		
		logger.debug("modify post");
		logger.debug(userVo.getFilename());
		logger.debug("real");
		logger.debug(userVo.getRealfilename());
		
		int updateCnt = 0;
		String originalFilename = "";
		String filename = "";
		
		if(profile.getSize() > 0) {
			originalFilename = profile.getOriginalFilename();
			filename = UUID.randomUUID().toString() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			
			userVo.setFilename(originalFilename);
			userVo.setRealfilename("d:\\upload\\" + filename);
			
			try {
				profile.transferTo(new File(userVo.getRealfilename()));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}else {
			originalFilename = userVo.getFilename();
			filename = userVo.getRealfilename();
			userVo.setFilename(originalFilename);
			userVo.setRealfilename(filename);
		}

		updateCnt = userService.modifyUser(userVo);
		
		// 사용자 수정이 정상적으로 된 경우 ==> 해당 사용자의 상세조회 페이지로 이동
		if (updateCnt == 1) {
			ra.addAttribute("userid", userVo.getUserid());
			//return "redirect:/user/userSelect";
			return "redirect:/user/userSelect";
		}
		// 사용자 수정이 비정상적으로 된 경우 ==> 해당 사용자의 정보 수정 페이지로 이동
		else {
			return UserModify(userVo.getUserid(), model);
		}
	}
	
	@RequestMapping(path="deleteUser", method = RequestMethod.POST)
	public String UserDelete(String userid, RedirectAttributes ra) {
		
		int deleteCnt = 0;
		try {
			deleteCnt = userService.deleteUser(userid);
		}catch(Exception e) {
			deleteCnt = -1;
		}
		
		if(deleteCnt == 1) {
			return "redirect:/user/pagingUser";
		}
		else {
			ra.addAttribute("userid", userid);
			return "redirect:/user/userSelect";
			//return "redirect:/user/user?userid="+ userid;
		}
	}
	
	@RequestMapping(path="userRegist", method = RequestMethod.GET)
	public String UserRegist() {
		//return "user/userRegist";
		return "tiles.user.userRegist";
	}
	
	//bindingResult 객체는 command 객체 바로 뒤에 인자로 기술해야 한다.
	@RequestMapping(path="userRegist", method = RequestMethod.POST)
	public String UserRegist(@Valid UserVo userVo, BindingResult result, MultipartFile profile, Model model) {
		
		//new UserVoValidator().validate(userVo, result);

		System.out.println("userVO"+userVo);

		if(result.hasErrors()) {
			logger.debug("result has error");
			return "tiles.user.userRegist";
			//return "user/userRegist";
		}
		
		Date reg_dt = new Date(); 
		userVo.setReg_dt(reg_dt);

		int updateCnt = 0;
		String originalFilename = "";
		String filename = "";
		
		if(profile.getSize() > 0) {
			originalFilename = profile.getOriginalFilename();
			filename = UUID.randomUUID().toString() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			
			userVo.setFilename(originalFilename);
			userVo.setRealfilename("d:\\upload\\" + filename);
			
			try {
				profile.transferTo(new File(userVo.getRealfilename()));
				
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}else {
			userVo.setFilename(originalFilename);
			userVo.setRealfilename(filename);
			
			try {
				profile.transferTo(new File(userVo.getRealfilename()));
				
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		
		updateCnt = userService.registUser(userVo);
		
		// 사용자 등록이 정상적으로 된 경우 ==> 사용자의 페이징리스트로 이동
		if (updateCnt == 1) {
			return "redirect:/user/pagingUser";
		}
		// 사용자 등록이 비정상적으로 된 경우 ==> 해당 사용자의 정보 수정 페이지로 이동
		else {
			//return "redirect:/user/userRegist";
			//return "user/userRegist";
			return "tiles.user.userRegist";
		}
	}
	
	@RequestMapping("excelDownload")
	public String excelDownload(Model model) {
		List<String> header = new ArrayList<String>();
		header.add("사용자 아이디");
		header.add("사용자 이름");
		header.add("사용자 별명");

		model.addAttribute("header", header);
		model.addAttribute("data", userService.selectAllUser());
		
		
		return "userExcelDownloadView";
	}
	
	// localhost/user/profile
	@RequestMapping("profile")
	public void profile(HttpServletResponse resp, String userid, HttpServletRequest req) {	
	
		resp.setContentType("image");
		
		// userid 파라미터를 이용하여
		// userService 객체를 통해 사용자의 사진 파일 이름을 획득
		// 파일 입출력을 통해 사진을 읽어들여 resp객체의 outputStream으로 응답 생성
		
		UserVo userVo = userService.selectUser(userid);
		
		String path = "";
		if(userVo.getRealfilename() == null) {
			path = req.getServletContext().getRealPath("/image/unknown.png");
		}
		else {
			path= userVo.getRealfilename();
		}
		
		logger.debug("path : {}", path);
		
		try {
			FileInputStream fis = new FileInputStream(path);
			ServletOutputStream sos = resp.getOutputStream();
			
			byte[] buff = new byte[512];
			while(fis.read(buff) != -1) {
				sos.write(buff);
			}
			
			fis.close();
			sos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("profileDownload")
	public void profileDownload(HttpServletResponse resp, String userid, HttpServletRequest req) {	
	
		UserVo userVo = userService.selectUser(userid);
	
		String path = "";
		String filename = "";
		if(userVo.getRealfilename() == null) {
			path = req.getServletContext().getRealPath("/image/unknown.png");
			filename = "unknown.png";
		}
		else {
			path= userVo.getRealfilename();
			filename = userVo.getFilename();
		}
		
		resp.setHeader("Content-Disposition", "attachment; filename=" + filename);
		
		// userid 파라미터를 이용하여
		// userService 객체를 통해 사용자의 사진 파일 이름을 획득
		// 파일 입출력을 통해 사진을 읽어들여 resp객체의 outputStream으로 응답 생성
		
		logger.debug("path : {}", path);
		
		try {
			FileInputStream fis = new FileInputStream(path);
			ServletOutputStream sos = resp.getOutputStream();
			
			byte[] buff = new byte[512];
			while(fis.read(buff) != -1) {
				sos.write(buff);
			}
			
			fis.close();
			sos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}


