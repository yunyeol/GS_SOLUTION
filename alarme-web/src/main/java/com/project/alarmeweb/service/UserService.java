package com.project.alarmeweb.service;


import com.project.alarmeweb.dto.User;

import java.util.List;

public interface UserService {
	List<User> getMemberList(String loginId, String orderParam);
	int deleteUsers(User user);
	int updateActiveUsers(User user);
	List<User> getAuthList();
	List<User> getGrpList();
	int insertUsers(User user);
	int updateAuthUsers(User user);
	int updateGrpUsers(User user);
	int updatePwdUsers(User user);
}
