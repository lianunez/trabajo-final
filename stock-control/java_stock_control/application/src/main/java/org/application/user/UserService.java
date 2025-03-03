package org.application.user;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.data.repository.user.UserRepository;
import org.domain.role.RoleDTO;
import org.domain.user.User;
import org.domain.user.UserCreateDTO;
import org.domain.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public List<UserDTO> getUsers() {
        List<User> users = userRepository.getUsers();
        List<UserDTO> mappedUsers = users.stream()
                .map(user -> {
                    RoleDTO roleDTO = RoleDTO.builder()
                            .name(user.getRole().getName())
                            .permissions(user.getRole().getPermission().getPermissions())
                            .build();
                    return UserDTO.builder()
                            .userName(user.getUserName())
                            .role(roleDTO)
                            .build();
                })
                .collect(Collectors.toList());
        return mappedUsers;
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

    @Transactional
    public void delete(Integer id) {
        Objects.requireNonNull(id);
        userRepository.delete(id);
    }

    public UserDTO buildDTO(User user, String token) {
        RoleDTO roleDTO = RoleDTO
                .builder()
                .name(user.getRole().getName())
                .permissions(user.getRole().getPermission().getPermissions())
                .build();
        return UserDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .jwt(token)
                .role(roleDTO)
                .build();
    }

    public User mapUser(UserCreateDTO createDTO) {
        return User.builder()
                .userName(createDTO.getName())
                .password(createDTO.getPassword())
                .build();
    }

    private boolean isCorrectPassword(String passwordByParam, String foundUserPassword) {
        return this.encoder.matches(passwordByParam, foundUserPassword);
    }
}
