package jp.profield.chat;

import java.util.HashMap;

public class WebscoketBean {
	
	WebscoketBean getWebscoketBeans;
	
	public static HashMap<String,Object> mLiveSession = new HashMap<String, Object>();
	public static HashMap<String,Object> mLiveUsers= new HashMap<String, Object>();
	public static HashMap<String, Object> getmLiveSession() {
		return mLiveSession;
	}
	public static void setmLiveSession(HashMap<String, Object> mLiveSession) {
		WebscoketBean.mLiveSession = mLiveSession;
	}
	public static HashMap<String, Object> getmLiveUsers() {
		return mLiveUsers;
	}
	public static void setmLiveUsers(HashMap<String, Object> mLiveUsers) {
		WebscoketBean.mLiveUsers = mLiveUsers;
	}

	
	public WebscoketBean getInstance(){
		
		if(getWebscoketBeans==null){
			return new WebscoketBean();
		}else{
			return getWebscoketBeans;
		}
			
	}
	
	
}
