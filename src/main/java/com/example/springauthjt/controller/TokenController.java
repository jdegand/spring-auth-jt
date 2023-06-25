package com.example.springauthjt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springauthjt.dto.AuthRequest;
import com.example.springauthjt.dto.JwtResponse;
import com.example.springauthjt.dto.RefreshTokenRequest;
import com.example.springauthjt.entity.RefreshToken;
import com.example.springauthjt.service.JwtService;
import com.example.springauthjt.service.RefreshTokenService;

@RestController
@RequestMapping("/token")
@CrossOrigin("*")
public class TokenController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/authenticate")
    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication validUser = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
        if (validUser.isAuthenticated()) {

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getName());
            return JwtResponse.builder()
                    .accessToken(jwtService.generateToken(authRequest.getName()))
                    .token(refreshToken.getToken()).build();

            // return jwtService.generateToken(authRequest.getName());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/refresh")
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getName());
                    return JwtResponse.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));
    }
}