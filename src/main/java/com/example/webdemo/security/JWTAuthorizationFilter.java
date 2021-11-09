package com.example.webdemo.security;

import com.example.webdemo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.TOKEN_HEADER);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws JwtException {
        var tokenHeader = request.getHeader(SecurityConstants.TOKEN_HEADER);

        if (!tokenHeader.isEmpty() && tokenHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            var token = tokenHeader.replace("Token ", "");
            Claims claims;
            claims = Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET.getBytes())
                    .parseClaimsJws(token).getBody();
            var user = User.builder()
                    .email(claims.getSubject())
                    .id(claims.get("id", Long.class))
                    .password(claims.get("password", String.class))
                    .username(claims.get("username", String.class))
                    .bio(claims.get("bio", String.class))
                    .image(claims.get("image", String.class))
                    .build();

            if (user != null) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,
                        null, new ArrayList<>());
                usernamePasswordAuthenticationToken.setDetails(token);
                return usernamePasswordAuthenticationToken;
            }

            return null;
        }

        return null;
    }
}
