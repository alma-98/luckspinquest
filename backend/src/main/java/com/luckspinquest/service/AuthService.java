package com.luckspinquest.service;

import com.luckspinquest.entity.User;
import com.luckspinquest.entity.UserProfile;
import com.luckspinquest.entity.Wallet;
import com.luckspinquest.entity.Role;
import com.luckspinquest.entity.UserRole;
import com.luckspinquest.repository.UserRepository;
import com.luckspinquest.repository.UserProfileRepository;
import com.luckspinquest.repository.WalletRepository;
import com.luckspinquest.repository.RoleRepository;
import com.luckspinquest.repository.UserRoleRepository;
import com.luckspinquest.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final WalletRepository walletRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository,
            WalletRepository walletRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.walletRepository = walletRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public User register(
            String username,
            String name,
            String email,
            String phone,
            String rawPassword
    ) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username wajib diisi");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nama wajib diisi");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email wajib diisi");
        }

        if (rawPassword == null || rawPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "Password minimal 8 karakter"
            );
        }

        if (userRepository.existsByUserUsername(username)) {
            throw new IllegalArgumentException(
                    "Username sudah digunakan"
            );
        }

        if (userRepository.existsByUserEmail(email)) {
            throw new IllegalArgumentException(
                    "Email sudah digunakan"
            );
        }

        if (phone != null && !phone.isBlank()
                && userRepository.existsByUserPhone(phone)) {
            throw new IllegalArgumentException(
                    "Nomor telepon sudah digunakan"
            );
        }

        User user = new User();

        user.setUserUsername(username);
        user.setUserName(name);
        user.setUserEmail(email);
        user.setUserPhone(
                phone == null || phone.isBlank() ? null : phone
        );

        user.setUserPasswordHash(
                passwordEncoder.encode(rawPassword)
        );

        user.setUserStatus("ACTIVE");
        user.setEmailVerified(false);
        user.setPhoneVerified(false);

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        User savedUser = userRepository.save(user);

        // ====================================================
        // DEFAULT USER PROFILE
        // ====================================================

        UserProfile profile = new UserProfile();

        profile.setUser(savedUser);
        profile.setProfileDisplayName(savedUser.getUserName());
        profile.setProfileLanguage("id");
        profile.setProfileTimezone("Asia/Jakarta");
        profile.setCreatedAt(now);
        profile.setUpdatedAt(now);

        userProfileRepository.save(profile);

        // ====================================================
        // DEFAULT WALLET
        // ====================================================

        Wallet wallet = new Wallet();

        wallet.setUser(savedUser);
        wallet.setWalletBalance(0L);
        wallet.setWalletLockedBalance(0L);
        wallet.setWalletAvailableBalance(0L);
        wallet.setWalletCurrency("COIN");
        wallet.setWalletStatus("ACTIVE");

        walletRepository.save(wallet);

        // ====================================================
        // DEFAULT ROLE = USER
        // ====================================================

        Role userRole = roleRepository
                .findByRoleCode("USER")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Default role USER tidak ditemukan"
                        )
                );

        UserRole assignment = new UserRole();

        assignment.setUser(savedUser);
        assignment.setRole(userRole);
        assignment.setAssignedAt(now);
        assignment.setAssignedBy(null);
        assignment.setCreatedAt(now);

        userRoleRepository.save(assignment);

        return savedUser;
    }

    @Transactional
    public Map<String, Object> login(
            String usernameOrEmail,
            String rawPassword
    ) {

        if (usernameOrEmail == null || usernameOrEmail.isBlank()) {
            throw new IllegalArgumentException(
                    "Username atau email wajib diisi"
            );
        }

        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException(
                    "Password wajib diisi"
            );
        }

        User user = userRepository
                .findByUserUsername(usernameOrEmail)
                .or(() -> userRepository.findByUserEmail(usernameOrEmail))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Username/email atau password salah"
                        )
                );

        if (!passwordEncoder.matches(
                rawPassword,
                user.getUserPasswordHash()
        )) {
            throw new IllegalArgumentException(
                    "Username/email atau password salah"
            );
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getUserStatus())) {
            throw new IllegalArgumentException(
                    "Akun tidak aktif"
            );
        }

        user.setLastLoginAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        String token = jwtService.generateToken(
                user.getUserId(),
                user.getUserUsername(),
                user.getUserEmail()
        );

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("token", token);
        response.put("tokenType", "Bearer");
        response.put("expiresIn", jwtService.getExpirationMillis());

        Map<String, Object> userData = new LinkedHashMap<>();
        userData.put("userId", user.getUserId());
        userData.put("username", user.getUserUsername());
        userData.put("name", user.getUserName());
        userData.put("email", user.getUserEmail());
        userData.put("status", user.getUserStatus());

        response.put("user", userData);

        return response;
    }
}
