package practice.service.test;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import practice.dto.UserDto;
import practice.entity.UserEntity;
import practice.exception.UserAlreadyExistException;
import practice.repository.UserRepository;
import practice.service.impl.UserServiceImpl;
import practice.utils.JwtUtils;
import practice.vo.UserVo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    @InjectMocks
    UserServiceImpl userService;


    @Nested
    @DisplayName("login method")
    class LoginMethod {
        @Nested
        class Success {
            @Test
            @DisplayName("Wrong user name")
            public void LoginSuccess(){

            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("Wrong user name")
            public void WrongUserName() {
                when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(any(), "123456"))).thenThrow(AuthenticationCredentialsNotFoundException.class);

                UserDto user = UserDto.builder()
                        .name("fasf")
                        .password("123456")
                        .build();

                assertThrows(AuthenticationException.class, () -> {
                    userService.login(user);
                });
            }
            @Test
            @DisplayName("Wrong user password")
            public void WrongUserPassword() {
                when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("matt", any()))).thenThrow(AuthenticationCredentialsNotFoundException.class);

                UserDto user = UserDto.builder()
                        .name("matt")
                        .password("123456")
                        .build();

                assertThrows(AuthenticationException.class, () -> {
                    userService.login(user);
                });
            }

        }
    }

    @Nested
    @DisplayName("register method")
    class RegisterMethod {
        @Nested
        class Success {
            @Test
            @DisplayName("New user register success")
            public void registerSuccess() {
                UserDto user = UserDto.builder()
                        .name("matt")
                        .email("test@gmail.com")
                        .build();

                configureUserDoesNotExist("matt", "test@gmail.com");

                userService.register(user);

                verify(userRepository, times(1)).save(any(UserEntity.class));
            }

            @Nested
            class Failed {

                @Test
                @DisplayName("When user name is duplicate")
                public void duplicateUserName() {
                    configureUserExistsByName("matt");

                    UserDto user = UserDto.builder()
                            .name("matt")
                            .email("test@gmail.com")
                            .build();

                    assertThrows(UserAlreadyExistException.class, () -> {
                        userService.register(user);
                    });
                }

                @Test
                @DisplayName("When user email is duplicate")
                public void duplicateEmail() {
                    configureUserExistsByEmail("test@gmail.com");

                    UserDto user = UserDto.builder()
                            .name("matt")
                            .email("test@gmail.com")
                            .password("123456")
                            .build();

                    assertThrows(UserAlreadyExistException.class, () -> {
                        userService.register(user);
                    });

                }

                @Test
                @DisplayName("When both user email and name are duplicate")
                public void duplicateEmailAndUsername() {
                    configureUserExists("matt", "test@gmail.com");

                    UserDto user = UserDto.builder()
                            .name("matt")
                            .email("test@gmail.com")
                            .build();

                    assertThrows(UserAlreadyExistException.class, () -> {
                        userService.register(user);
                    });
                }
            }

            private void configureUserExists(String name, String email) {
                when(userRepository.findByNameOrEmail(name, email)).thenReturn(Optional.of(new UserEntity()));
            }

            private void configureUserExistsByName(String name) {
                configureUserExists(eq(name), any());
            }

            private void configureUserExistsByEmail(String email) {
                configureUserExists(any(), eq(email));
            }

            private void configureUserDoesNotExist(String name, String email) {
                when(userRepository.findByNameOrEmail(name, email)).thenReturn(Optional.empty());
            }
        }

        @Nested
        @DisplayName("searchUsers method")
        class SearchUsersMethod {

            @Nested
            public class SuccessResult {
                @Test
                @DisplayName("query name string matches some users")
                public void nameMatchesUsers() {
                    List<UserEntity> users = List.of(
                            UserEntity
                                    .builder()
                                    .id(1)
                                    .name("matt")
                                    .build(),
                            UserEntity
                                    .builder()
                                    .id(2)
                                    .name("matthew")
                                    .build()
                    );

                    configureSearchUsersReturn(1, "matt", users);

                    List<UserVo> result = userService.searchUsers(1, "matt");
                    List<UserVo> expected = List.of(
                            UserVo
                                    .builder()
                                    .id(1)
                                    .name("matt")
                                    .build(),
                            UserVo
                                    .builder()
                                    .id(2)
                                    .name("matthew")
                                    .build()
                    );

                    assertThat(result).usingRecursiveComparison()
                            .isEqualTo(expected);
                }
            }

            @Nested
            public class EmptyResult {
                @Test
                @DisplayName("Both group id and username are invalid")
                public void noMatchedUsers() {
                    when(userRepository.findByNameContainingAndNotInGroup(any(), any())).thenReturn(List.of());

                    List<UserVo> result = userService.searchUsers(12345, "fasf");

                    assertEquals(0, result.size());
                }

                @Test
                @DisplayName("Group has no user")
                public void NoUserInGroup() {
                    configureSearchUsersReturnEmptyList(eq(1), any());

                    List<UserVo> result = userService.searchUsers(1, "fasf");

                    assertEquals(0, result.size());
                }


            }

            private void configureSearchUsersReturnEmptyList(int groupId, String name) {
                lenient().when(userRepository.findByNameContainingAndNotInGroup(name, groupId)).thenReturn(List.of());
            }

            private void configureSearchUsersReturn(int groupId, String name, List<UserEntity> users) {
                lenient().when(userRepository.findByNameContainingAndNotInGroup(name, groupId)).thenReturn(users);
            }
        }

    }


}