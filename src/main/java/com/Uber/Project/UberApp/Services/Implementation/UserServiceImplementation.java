package com.Uber.Project.UberApp.Services.Implementation;

import com.Uber.Project.UberApp.Entity.User;
import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Repositories.UserRepositories;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserDetailsService {

    private final UserRepositories userRepositories;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositories.findByEmail(username).orElse(null);
    }

    public User getUserByID(Long userId) {
        return userRepositories.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User Not Found With Id"+userId));
    }
}
