package online.bookstore.service;

import lombok.RequiredArgsConstructor;
import online.bookstore.dto.user.UserRegistrationRequestDto;
import online.bookstore.dto.user.UserResponseDto;
import online.bookstore.exception.RegistrationException;
import online.bookstore.mapper.UserMapper;
import online.bookstore.model.User;
import online.bookstore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ShoppingCartService shoppingCartService;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("User with email: "
                    + requestDto.getEmail() + " already exists");
        }
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        shoppingCartService.addUser(user);

        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
