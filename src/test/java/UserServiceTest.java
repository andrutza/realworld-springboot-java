import com.example.webdemo.dto.request.user.UserDTO;
import com.example.webdemo.exception.ItemAlreadyExistsException;
import com.example.webdemo.model.User;
import com.example.webdemo.repo.UserRepository;
import com.example.webdemo.service.impl.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private static ModelMapper modelMapper;

    @MockBean
    private static BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("Test saveUser Success Login case")
    public void testSaveUserLogin() {
        User user = User.builder()
                .email("ana@gmail.com")
                .password("ana")
                .build();
        UserDTO userDTO = UserDTO.builder()
                .email("ana@gmail.com")
                .password("ana")
                .build();

        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("ana");
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        Optional<User> savedUser = userService.saveUser(userDTO);
        verify(userRepository, times(1)).save(user);

        assertTrue(savedUser.isPresent(), "User not found");
        assertSame(savedUser.get(), user, "The saved user is not the same as the mocked user");
    }

    @Test
    @DisplayName("Test saveUser Success Register case")
    public void testSaveUserRegister() {
        User user = User.builder()
                .email("ana@gmail.com")
                .username("ana")
                .password("ana")
                .build();
        UserDTO userDTO = UserDTO.builder()
                .email("ana@gmail.com")
                .username("ana")
                .password("ana")
                .build();

        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("ana");
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        Optional<User> savedUser = userService.saveUser(userDTO);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);

        assertTrue(savedUser.isPresent(), "User not found");
        assertSame(savedUser.get(), user, "The saved user is not the same as the mocked user");
    }

    @Test
    @DisplayName("Test saveUser Error case")
    public void testSaveUserAlreadyExists() {
        User user = User.builder()
                .email("ana@gmail.com")
                .username("ana")
                .password("ana")
                .build();
        UserDTO userDTO = UserDTO.builder()
                .email("ana@gmail.com")
                .username("ana")
                .password("ana")
                .build();

        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("ana");
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        Optional<User> savedUser = userService.saveUser(userDTO);
        verify(userRepository, times(1)).save(user);

        assertTrue(savedUser.isPresent(), "User not found");
        assertSame(savedUser.get(), user, "The saved user is not the same as the mocked user");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        ItemAlreadyExistsException exception = assertThrows(ItemAlreadyExistsException.class, () -> userService.saveUser(userDTO));

        assertEquals(exception.getMessage(), "User already exists!");
    }
}
