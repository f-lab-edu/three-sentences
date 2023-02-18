package com.sh.threesentences.auth.config;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class EmailAuthentication extends UsernamePasswordAuthenticationToken {

    public EmailAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public EmailAuthentication(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getEmail() {
        return getName();
    }
}
