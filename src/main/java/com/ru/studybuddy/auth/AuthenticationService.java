package com.ru.studybuddy.auth;


import com.ru.studybuddy.errors.CookieException;
import com.ru.studybuddy.token.TokenService;
import com.ru.studybuddy.user.UserService;
import com.ru.studybuddy.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service // говорит спрингу что тт серверная логика
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthenticationResponse registration(RegisterRequest request, HttpServletResponse httpServletResponse) {
        User user = userService.setUser(request);
        login(request);
        String refreshToken = tokenService.generateAccessToken(user);
        String accessToken = tokenService.generateRefreshToken(user);
        tokenService.setTokenToRepository(refreshToken, user);
        setTokenToCookie(httpServletResponse, refreshToken);
        return buildResponse(accessToken, user);
    }

    public AuthenticationResponse login(AuthenticationRequest request, HttpServletResponse httpServletResponse) {
        User user = userService.getUser(request.getEmail());
        login(request);
        String refreshToken = tokenService.generateAccessToken(user);
        String accessToken = tokenService.generateRefreshToken(user);
        tokenService.setTokenToRepository(refreshToken, user);
        setTokenToCookie(httpServletResponse, refreshToken);
        return buildResponse(accessToken, user);
    }

    public AuthenticationResponse refresh(String requestToken, HttpServletResponse httpServletResponse) {
        tokenService.checkEquals(requestToken);
        String email = tokenService.getUsername(requestToken);
        User user = userService.getUser(email);
        String refreshToken = tokenService.generateAccessToken(user);
        String accessToken = tokenService.generateRefreshToken(user);
        tokenService.setTokenToRepository(refreshToken, user);
        setTokenToCookie(httpServletResponse, refreshToken);
        return buildResponse(accessToken, user);
    }

    public void logout(String refreshToken) {
        tokenService.deleteToken(refreshToken);

    }


    private void setTokenToCookie(HttpServletResponse response, String token) {
        try {
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } catch (Exception e) {
            throw new CookieException("Setting cookie exception");
        }
    }

    private AuthenticationResponse buildResponse(String accessToken, User user) {
        return AuthenticationResponse.builder()
                .token(accessToken)
                .user(user)
                .build();
    }

    private void login(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (RuntimeException e) {
            throw new AuthenticationServiceException("Password is wrong");
        }
    }

    private void login(RegisterRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }
}