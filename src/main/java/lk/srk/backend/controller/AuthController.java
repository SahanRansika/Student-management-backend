package lk.srk.backend.controller;

import lk.srk.backend.dto.LoginRequest;
import lk.srk.backend.dto.LoginResponse;
import lk.srk.backend.model.User;
import lk.srk.backend.service.JwtService;
import lk.srk.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("🔐 Login attempt: " + loginRequest.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            User user = userService.findByEmail(loginRequest.getEmail()).orElse(null);

            System.out.println("✅ Login successful: " + loginRequest.getEmail());

            return ResponseEntity.ok(new LoginResponse(
                    token,
                    userDetails.getUsername(),
                    user != null ? user.getRole() : "ADMIN",
                    user != null ? user.getFullName() : ""
            ));

        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            System.out.println("📝 Registration attempt: " + user.getEmail());

            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email is required");
                return ResponseEntity.badRequest().body(error);
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Password is required");
                return ResponseEntity.badRequest().body(error);
            }

            if (user.getPassword().length() < 6) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Password must be at least 6 characters");
                return ResponseEntity.badRequest().body(error);
            }

            if (userService.existsByEmail(user.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email already exists: " + user.getEmail());
                return ResponseEntity.badRequest().body(error);
            }

            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("ADMIN");
            }

            user.setEmail(user.getEmail().trim());
            user.setFullName(user.getFullName() != null ? user.getFullName().trim() : "");

            User savedUser = userService.saveUser(user);

            System.out.println("✅ User registered: " + savedUser.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("email", savedUser.getEmail());
            response.put("role", savedUser.getRole());
            response.put("fullName", savedUser.getFullName());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            String token = authHeader.substring(7);
            String email = jwtService.extractUsername(token);
            User user = userService.findByEmail(email).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("fullName", user.getFullName());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.ok(Map.of("authenticated", false));
            }

            String token = authHeader.substring(7);
            String email = jwtService.extractUsername(token);

            if (email != null && userService.existsByEmail(email)) {
                return ResponseEntity.ok(Map.of("authenticated", true));
            }

            return ResponseEntity.ok(Map.of("authenticated", false));

        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
    }
}