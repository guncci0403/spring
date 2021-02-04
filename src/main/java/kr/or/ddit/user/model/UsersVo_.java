package kr.or.ddit.user.model;

import java.util.List;

public class UsersVo_ {
	
	private List<String> userid;

	public List<String> getUserid() {
		return userid;
	}

	public void setUserid(List<String> userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "UsersVo [userid=" + userid + "]";
	}
}
