package com.project.alarmeweb.service.impl;

import com.project.alarmeweb.dto.User;
import com.project.alarmeweb.dto.UserSession;
import com.project.alarmeweb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Autowired
    private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		// TODO Auto-generated method stubUserServiceImpl.java
		List<User> getMemberList = userService.getMemberList(loginId, null);

		logger.info("getMemberList login Data : {}", getMemberList.get(0) );

		if(getMemberList == null){
			throw new UsernameNotFoundException("no user");
		}

		UserSession userSession = new UserSession(
                                        getMemberList.get(0).getLoginId(),
                                        getMemberList.get(0).getPasswd(),
										true,
										true,
										true,
										true,
										getAuthorities(getMemberList.get(0).getMbrAuthId())
									);

		userSession.setLoginId(getMemberList.get(0).getMbrName());
		userSession.setAuthId(getMemberList.get(0).getMbrAuthId());

		return userSession;
	}

	public Collection<GrantedAuthority> getAuthorities(int mbrAuthId) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String role = null;

		if(mbrAuthId == 1){
			role = "ROLE_ADMIN";
		}else {
			role = "ROLE_USER";
		}

		authorities.add(new SimpleGrantedAuthority(role));

		return authorities;
	}
}
