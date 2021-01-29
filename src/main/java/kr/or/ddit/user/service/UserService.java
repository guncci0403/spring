package kr.or.ddit.user.service;

import kr.or.ddit.user.model.UserVo;

public interface UserService {

	//사용자 아이디로 사용자 조회
	UserVo getUser(String userid);
}
