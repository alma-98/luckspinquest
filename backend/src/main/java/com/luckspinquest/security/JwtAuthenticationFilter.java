package com.luckspinquest.security;

import com.luckspinquest.repository.UserRepository;
import com.luckspinquest.repository.UserRoleRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        System.out.println(
                "=== JWT FILTER === " +
                request.getMethod() +
                " " +
                request.getRequestURI()
        );

        System.out.println(
                "Authorization present: " +
                (authorization != null)
        );

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {
            boolean tokenValid = jwtService.isTokenValid(token);

            System.out.println(
                    "JWT token valid: " + tokenValid
            );

            if (tokenValid) {

                String username = jwtService.extractUsername(token);

                System.out.println(
                        "JWT username: " + username
                );

                if (SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

                    userRepository.findByUserUsername(username)
                            .filter(user ->
                                    "ACTIVE".equalsIgnoreCase(
                                            user.getUserStatus()
                                    )
                            )
                            .ifPresent(user -> {

                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                user.getUserUsername(),
                                                null,
                                                userRoleRepository
                                                        .findByUserUserIdWithRole(user.getUserId())
                                                        .stream()
                                                        .filter(userRole ->
                                                                userRole.getRole() != null &&
                                                                "ACTIVE".equalsIgnoreCase(
                                                                        userRole.getRole().getRoleStatus()
                                                                )
                                                        )
                                                        .map(userRole ->
                                                                new SimpleGrantedAuthority(
                                                                        "ROLE_" +
                                                                        userRole.getRole()
                                                                                .getRoleCode()
                                                                                .toUpperCase()
                                                                )
                                                        )
                                                        .toList()
                                        );

                                authentication.setDetails(user.getUserId());

                                SecurityContextHolder
                                        .getContext()
                                        .setAuthentication(authentication);

                                System.out.println(
                                        "SecurityContext authenticated: " +
                                        authentication.getName() +
                                        " authorities=" +
                                        authentication.getAuthorities()
                                );
                            });
                }
            }
        } catch (Exception e) {

            System.out.println(
                    "JWT FILTER ERROR: " +
                    e.getClass().getName() +
                    " - " +
                    e.getMessage()
            );

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
