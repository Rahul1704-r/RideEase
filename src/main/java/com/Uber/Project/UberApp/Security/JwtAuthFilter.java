package com.Uber.Project.UberApp.Security;


import com.Uber.Project.UberApp.Entity.User;
import com.Uber.Project.UberApp.Services.Implementation.UserServiceImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceImplementation userServiceImplementation;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain) throws ServletException, IOException {
        try{
            final String requestTokenHeader= request.getHeader("Authorization");
            if(requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer ") ){
                filterchain.doFilter(request,response);
                return;
            }

            String token=requestTokenHeader.split("Bearer ")[1];
            //String token = requestTokenHeader.substring(7).trim();
            Long userId=jwtService.getUserIdFromToken(token);

            if(userId!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
                User user = userServiceImplementation.getUserByID(userId);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterchain.doFilter(request, response);
        }catch (Exception ex){
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
