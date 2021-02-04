package kr.or.ddit.user.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.user.model.UserVo;

public interface UserService {

	//사용자 아이디로 사용자 조회
	UserVo selectUser(String userid);
	
	//전체 사용자 정보 조회
	List<UserVo> selectAllUser();
	
	// 사용자 페이징 조회
	Map<String, Object>selectPagingUser(PageVo pageVo);

	//사용자 정보 수정
	int modifyUser(UserVo userVo);

	//사용자 정보 등록
	int registUser(UserVo userVo);
	
	//사용자 삭제
	int deleteUser(String userid);
}