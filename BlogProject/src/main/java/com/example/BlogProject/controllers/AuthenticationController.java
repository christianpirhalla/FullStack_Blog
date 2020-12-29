package com.example.BlogProject.controllers;

import com.example.BlogProject.services.AuthService;
import com.example.BlogProject.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

   @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest){
       authService.signup(registerRequest);
       return new ResponseEntity<>("User registration is successful",
               HttpStatus.OK);
   }

   @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
      return authService.login(loginRequest);
   }

   @PostMapping("/logout")
    public ResponseEntity<String>  logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!");
   }

   @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable("token") String token){
       authService.verifyAccount(token);
       return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
   }

   @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
       return authService.refreshToken(refreshTokenRequest);
   }

}
