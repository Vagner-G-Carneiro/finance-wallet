package com.finance.wallet.v12.infra.security;

import com.finance.wallet.v12.infra.exceptions.V12UserException;
import com.finance.wallet.v12.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static TokenService tokenService;
    private static UserRepository userRepository;
    private static HandlerExceptionResolver handler;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handler)
    {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.handler = handler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = this.recoverToken(request);
            if(token != null)
            {
                String login = this.tokenService.validateToken(token);
                if(!login.isEmpty())
                {
                    UserDetails user = userRepository.findByEmail(login).orElseThrow(() ->
                            V12UserException.notFound("Usuário não encontrado: " + login));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handler.resolveException(request, response, null, e);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
