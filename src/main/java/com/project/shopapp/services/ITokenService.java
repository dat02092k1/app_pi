package com.project.shopapp.services;

import com.project.shopapp.models.User;

public interface ITokenService {
    void addToken(User user, String token, boolean isMobileDevice);
}
