package com.project.alarmeweb.service;


import com.project.alarmeweb.dto.User;

import java.util.List;

public interface UserService {
	List<User> getMemberList(String loginId, String orderParam);
}
