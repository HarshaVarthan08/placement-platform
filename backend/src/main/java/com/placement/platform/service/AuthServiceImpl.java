package com.placement.platform.service;

import com.placement.platform.dto.AuthResponse;
import com.placement.platform.dto.LoginRequest;
import com.placement.platform.dto.MessageResponse;
import com.placement.platform.dto.RegisterRequest;
import com.placement.platform.entity.User;
import com.placement.platform.exception.EmailAlreadyExistsException;
import com.placement.platform.repository.UserRepository;
import com.placement.platform.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Explicit constructor injection (no @Autowired needed)
    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email is already registered: " + request.email());
        }

        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        userRepository.save(user);

        return new MessageResponse("Registration successful");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // This throws AuthenticationException if credentials are invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.email()));

        String jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken, user.getId());
    }
}
