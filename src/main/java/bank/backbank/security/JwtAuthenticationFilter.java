package bank.backbank.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;
        String password = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            username = JwtTokenUtil.getUsernameFromToken(token);

        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (JwtTokenUtil.validateToken(token, username)) {
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
//    private String getTokenFromRequest(HttpServletRequest request) {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer "))
//            return authHeader.substring(7);
//        return null;
//    }
}
