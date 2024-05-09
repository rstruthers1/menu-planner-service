package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.auth.RegisterResponse;
import com.homemenuplanner.models.User;
import com.homemenuplanner.dtos.ErrorResponse;
import com.homemenuplanner.dtos.auth.LoginRequest;
import com.homemenuplanner.dtos.auth.LoginResponse;
import com.homemenuplanner.dtos.auth.RegisterRequest;
import com.homemenuplanner.repositories.UserRepository;
import com.homemenuplanner.security.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private JwtService jwtService;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Value("${jwt.cookie.secure}")
    private boolean jwtCookieSecure;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginReq) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();
            String token = jwtService.generateToken(email);
            // TODO: use properties to set cookie values. They might be different for production
            ResponseCookie jwtCookie = ResponseCookie.from("token", token)
                    .httpOnly(true)  // Makes the cookie inaccessible to JavaScript
                    .secure(jwtCookieSecure)   // Ensures the cookie is sent only over HTTPS
                    .path("/")      // Cookie accessible for entire domain
                    .maxAge(24 * 60 * 60) // Sets max age of cookie to 1 day
//                    .sameSite("Lax")  // Prevents CSRF
                    .build();

            LoginResponse loginRes = new LoginResponse(email);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(loginRes);
        }
        catch (UsernameNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        catch (BadCredentialsException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "User cannot be null"));
        }

        if (registerRequest.getFirstName() == null || registerRequest.getFirstName().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "First name cannot be null or empty"));
        }

        if (registerRequest.getLastName() == null || registerRequest.getLastName().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Last name cannot be null or empty"));
        }

        if (registerRequest.getEmail() == null || registerRequest.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Email cannot be null or empty"));
        }

        if (registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "Password cannot be null or empty"));
        }

        if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, "User with email " + registerRequest.getEmail() + " already exists"));
        }

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User savedUser = userRepository.save(user);

        RegisterResponse registerResponse = new RegisterResponse(savedUser.getId(),
                savedUser.getFirstName(), savedUser.getLastName(),
                savedUser.getEmail());
        return ResponseEntity.ok(registerResponse);
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout() {
        ResponseCookie jwtCookie = ResponseCookie.from("token", "")
                .httpOnly(true)  // Makes the cookie inaccessible to JavaScript
//                .secure(true)   // Ensures the cookie is sent only over HTTPS
                .path("/")      // Cookie accessible for entire domain
                .maxAge(0) // Sets max age of cookie to 0
//                .sameSite("Strict")  // Prevents CSRF
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).build();
    }

}
