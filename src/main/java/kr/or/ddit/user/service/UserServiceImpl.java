package kr.or.ddit.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.user.model.UserVo;
import kr.or.ddit.user.repository.UserDao;


@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Resource(name="userDao")
	private UserDao userDao;
	
	public UserServiceImpl() {}
	
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public UserVo selectUser(String userid) {
		return userDao.selectUser(userid); 
	}
	
	@Override
	public List<UserVo> selectAllUser() {
		return userDao.selectAllUser();
	}

	@Override
	public Map<String, Object> selectPagingUser(PageVo pageVo) {
	
		Map<String, Object> map = new HashMap<String, Object>();

		int userCnt = userDao.selectAllUserCnt();
		
		map.put("pageVo", pageVo);
		map.put("userList", userDao.selectPagingUser(pageVo));
		map.put("pagination", (int)Math.ceil((double)userCnt / pageVo.getPageSize()));
		
		return map;
	}

	@Override
	public int modifyUser(UserVo userVo) {
		return userDao.modifyUser(userVo);
	}

	@Override
	public int registUser(UserVo userVo) {
		return userDao.registUser(userVo);
	}

	@Override
	public int deleteUser(String userid) {
		return userDao.deleteUser(userid);
	}
	
	
	//getter, setter 만들기
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	


}
