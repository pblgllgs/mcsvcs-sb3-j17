package com.pblgllgs.gateway.filters;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${token.secret}")
    private String secret;

    public static class Config {
        private List<String> authorities;

        public List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(String authorities) {
            this.authorities = Arrays.asList(authorities.split(" "));
        }
        //        private String role;
//        private String authority;

//        public String getRole() {
//            return role;
//        }
//
//        public void setRole(String role) {
//            this.role = role;
//        }
//
//        public String getAuthority() {
//            return authority;
//        }
//
//        public void setAuthority(String authority) {
//            this.authority = authority;
//        }
    }

//    @Override
//    public List<String> shortcutFieldOrder() {
//        return Arrays.asList("role","authority");
//    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("authorities");
    }

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

//            if (!isJwtValid(jwt)) {
//                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
//            }
            List<String> authorities = getAuthorities(jwt);
            boolean hasRequiredAuthorities = authorities.stream().anyMatch(authority -> config.getAuthorities().contains(authority));
            if (!hasRequiredAuthorities) {
                return onError(exchange, "Insufficient permissions, user is not authorized to perform this operation", HttpStatus.FORBIDDEN);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        DataBufferFactory bufferFactory =  response.bufferFactory();
        DataBuffer dataBuffer =  bufferFactory.wrap(err.getBytes());

        return response.writeWith(Mono.just(dataBuffer));
    }

    private List<String> getAuthorities(String jwt) {
        List<String> returnValue = new ArrayList<>();

        String subject = null;
        byte[] secretKeyBytes = Base64.getEncoder().encode(secret.getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();

        try {

            Jwt<Header, Claims> parsedToken = jwtParser.parse(jwt);
            List<Map<String, String>> scopes = parsedToken.getBody().get("scope", List.class);
            scopes.stream().map(scope -> returnValue.add(scope.get("authority"))).toList();
        } catch (Exception ex) {
            return returnValue;
        }

        return returnValue;
    }

//    private boolean isJwtValid(String jwt) {
//        boolean returnValue = true;
//
//        String subject = null;
//        byte[] secretKeyBytes = Base64.getEncoder().encode(secret.getBytes());
//        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
//
//        JwtParser jwtParser = Jwts.parserBuilder()
//                .setSigningKey(signingKey)
//                .build();
//
//        try {
//
//            Jwt<Header, Claims> parsedToken = jwtParser.parse(jwt);
//            subject = parsedToken.getBody().getSubject();
//
//        } catch (Exception ex) {
//            returnValue = false;
//        }
//
//        if (subject == null || subject.isEmpty()) {
//            returnValue = false;
//        }
//
//        return returnValue;
//    }
}
