package org.application.user;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.data.repository.user.UserRepository;
import org.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(this.encoder.encode(user.getPassword()));
        return this.userRepository.createUser(user);
    }

    public User findUser(String user, String password) {
        User foundUser = userRepository.findUser(user);

        if (foundUser == null) {
            return null;
        }

        if (!isCorrectPassword(password, foundUser.getPassword())) {
            foundUser.setPassword(null);
        }
        return foundUser;
    }


    private boolean isCorrectPassword(String passwordByParam, String foundUserPassword) {
        return this.encoder.matches(passwordByParam, foundUserPassword);
    }
}
