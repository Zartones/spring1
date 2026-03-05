package online.bookstore.service;

import lombok.RequiredArgsConstructor;
import online.bookstore.dto.user.UserRegistrationRequestDto;
import online.bookstore.dto.user.UserResponseDto;
import online.bookstore.exception.RegistrationException;
import online.bookstore.mapper.UserMapper;
import online.bookstore.model.User;
import online.bookstore.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Registration failed");
        }
        User user = userMapper.toUser(requestDto);

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }
}
