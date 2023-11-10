package com.pblgllgs.album.filters;

/*
 *
 * @author pblgl
 * Created on 08-11-2023
 */

import java.io.IOException;
import java.util.Collection;

import com.pblgllgs.album.services.JwtClaimsParserService;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    private Environment environment;

    public AuthorizationFilter(AuthenticationManager authManager, Environment environment) {
        super(authManager);
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(environment.getProperty("authorization.token.header.name"));

        if (header == null || !header.startsWith(environment.getProperty("authorization.token.header.prefix"))) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(environment.getProperty("authorization.token.header.name"));

        if (authorizationHeader == null) {
            return null;
        }

        String token = authorizationHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");

        JwtClaimsParserService jwtClaimsParserService = new JwtClaimsParserService(token, environment.getProperty("token.secret"));
        String subject = jwtClaimsParserService.getJwtSubject();
        Collection<? extends GrantedAuthority> userAuthorities = jwtClaimsParserService.getUserAuthorities();

        return new UsernamePasswordAuthenticationToken(subject, null, userAuthorities);

    }
}