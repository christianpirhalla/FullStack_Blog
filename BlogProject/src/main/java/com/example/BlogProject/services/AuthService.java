package com.example.BlogProject.services;


import com.example.BlogProject.dto.AuthenticationResponse;
import com.example.BlogProject.dto.LoginRequest;
import com.example.BlogProject.dto.RefreshTokenRequest;
import com.example.BlogProject.dto.RegisterRequest;
import com.example.BlogProject.entities.NotificationEmail;
import com.example.BlogProject.entities.VerificationToken;
import com.example.BlogProject.exceptions.SpringBlogException;
import com.example.BlogProject.repositories.UserRepository;
import com.example.BlogProject.repositories.VerificationTokenRepository;
import com.example.BlogProject.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import sun.plugin.liveconnect.SecurityContextHelper;

import java.time.Instant;
import java.util.UUID;


@AllArgsConstructor
@Service
@Slf4j
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;


    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername((registerRequest.getUsername()));
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        log.info("User Registered Successfully, Sending Authentiction Email");

        String token = generateVerificationToken(user);
        String message = mailContentBuilder.build("Thank you for singing up to ZCW Blog Appliation, please click on the below URL " +
                "to activate your account: "
        + ACTIVATION_EMAIL + "/" + token);
        //when user clicks on the URL, we take the token from the URL param, look it up in the DB,
        //and fetch the user who created the token, and enable the user

        mailService.sendMail(new NotificationEmail("Please activate your Account", user.getEmail(),
                message));
    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private String encodePassword(String password){ return passwordEncoder.encode(password);}

    public AuthenticationResponse login(LoginRequest loginRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationtoken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHelper.
                getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws SpringBlogException {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
            return AuthenticationResponse.builder()
                    .authenticationToken(token)
                    .refreshToken(refreshTokenRequest.getRefreshToken())

                    .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                        .username(refreshTokenRequest.getUsername())
                        .build();
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringBlogException("User not found with username - " +
                username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional(readOnly =  true)
    public User getCurrentUser() {
        User principal = (User) SecurityContextHelper.
                getConext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found - " + principal.getUsername()));
    }

    public boolean isLoggedIn(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

}
