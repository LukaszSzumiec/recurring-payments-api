package com.lukaszszumiec.recurring_payments_api.application;


import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
    UserDetails loadByEmail(String email);
}