package org.application.user;

import lombok.NoArgsConstructor;
import org.data.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.HashMap;

@NoArgsConstructor(force = true)
@Service
public class JWTUserService  implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public JWTUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HashMap<String, String> foundUser = userRepository.findByUsername(username);
        return new User(foundUser.get("name"), foundUser.get("password"), Collections.emptyList());
    }
}
