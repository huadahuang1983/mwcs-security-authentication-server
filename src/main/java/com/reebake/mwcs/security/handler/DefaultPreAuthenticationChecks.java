package com.reebake.mwcs.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

@Slf4j
public class DefaultPreAuthenticationChecks implements UserDetailsChecker {

	@Override
	public void check(UserDetails toCheck) {
		
		if (!toCheck.isAccountNonLocked()) {
            log.debug("User account is locked");
            throw new LockedException("User account is locked");
        } else if (!toCheck.isEnabled()) {
            log.debug("User account is disabled");
            throw new DisabledException("User is disabled");
        } else if (!toCheck.isAccountNonExpired()) {
            log.debug("User account is expired");
            throw new AccountExpiredException("User account has expired");
        }
		
	}

}
