package kr.or.ddit.user.repository;

import java.util.List;

import kr.or.ddit.common.model.PageVo;
import kr.or.ddit.user.model.UserVo;

public interface UserDao {
	//반환타입 메소드명();
	List<UserVo> selectAllUser();
	
	//사용자 아이디로 사용자 조회
	UserVo selectUser(String userid);

	// 사용자 페이징 조회
	List<UserVo> selectPagingUser(PageVo pageVo);

	// 사용자 전체 수 조회
	int selectAllUserCnt();
	
	//사용자 정보 수정
	int modifyUser(UserVo userVo);

	//사용자 정보 등록
	int registUser(UserVo userVo);
	
	//사용자 삭제
	int deleteUser(String userid);
}
