package kr.or.ddit.yogurt.model;

public class DailyVo {
	private int cid;
	private int pid;
	private String dt;
	private int cnt;
	
	public DailyVo() {}
	
	public DailyVo(int cid, int pid, String dt, int cnt) {
		this.cid = cid;
		this.pid = pid;
		this.dt = dt;
		this.cnt = cnt;
	}
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	
	@Override
	public String toString() {
		return "DailyVo [cid=" + cid + ", pid=" + pid + ", dt=" + dt + ", cnt=" + cnt + "]";
	}
	
	
}
