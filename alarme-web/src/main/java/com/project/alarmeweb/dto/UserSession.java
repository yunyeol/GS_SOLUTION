package com.project.alarmeweb.dto;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserSession extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserSession(String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired, boolean accountNonLocked,
                       Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		// TODO Auto-generated constructor stub
	}

	public String loginId;
    public int authId;

    public String getLoginId(){return loginId;}
    public void setLoginId(String loginId){this.loginId = loginId;}

    public int getAuthId() {
        return authId;
    }
    public void setAuthId(int authId) {
        this.authId = authId;
    }

}
