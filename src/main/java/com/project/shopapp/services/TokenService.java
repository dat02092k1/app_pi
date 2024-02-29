package com.project.shopapp.services;

import com.project.shopapp.models.Token;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService{
    private static final int MAX_TOKENS = 3;

    @Value("${jwt.expiration}")
    private int expiration;
    private final TokenRepository tokenRepository;

    @Transactional
    @Override
    public void addToken(User user, String token, boolean isMobileDevice) {
        List<Token> userTokens = tokenRepository.findByUser(user);

        int tokenCount = userTokens.size();

        if (tokenCount >= MAX_TOKENS) {
            boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            }
            else {
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }
        long expirationInSeconds = expiration;
        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(expirationInSeconds);

        // create new token for user
        Token newToken = Token.builder()
                .token(token)
                .tokenType("Bearer")
                .expirationDate(expirationDate)
                .isMobile(isMobileDevice)
                .user(user)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(newToken);
    }
}
