package practice.service.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.dto.GroupDto;
import practice.entity.GroupEntity;
import practice.entity.UserEntity;
import practice.entity.UserGroup;
import practice.exception.GroupNameTakenException;
import practice.exception.GroupNotFoundException;
import practice.exception.UserAlreadyInGroupException;
import practice.exception.UserNotFoundException;
import practice.repository.ChatRepository;
import practice.repository.GroupRepository;
import practice.repository.UserGroupRepository;
import practice.repository.UserRepository;
import practice.service.impl.GroupServiceImpl;
import practice.vo.GroupVo;
import practice.vo.UserVo;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    GroupRepository groupRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ChatRepository chatRepository;

    @Mock
    UserGroupRepository userGroupRepository;


    @InjectMocks
    GroupServiceImpl groupService;


    @Nested
    @DisplayName("addGroup method")
    class AddGroupMethod {
        @Nested
        class Success {
            @Test
            @DisplayName("new group added successfully")
            public void addGroupSuccess() {
                configureGroupNotExist("test group");
                configureUserRepositoryFindByIdExist(1);

                GroupDto group = GroupDto.builder().name("test group").build();

                GroupVo res = groupService.addGroup(1, group);

                assertEquals(res.getName(), "test group");
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("When group name is taken")
            public void duplicateGroupName() {
                configureGroupExist("test group");

                GroupDto group = GroupDto.builder().name("test group").build();
                assertThrows(GroupNameTakenException.class, () -> {
                    groupService.addGroup(1, group);
                });
            }

            @Test
            @DisplayName("User is not found")
            public void userNotFound() {
                configureUserRepositoryFindByIdNotExist(1);
                configureGroupNotExist("test group");

                GroupDto group = GroupDto.builder().name("test group").build();

                assertThrows(UserNotFoundException.class, () -> {
                    groupService.addGroup(1, group);
                });
            }
        }

        private void configureGroupExist(String groupName) {
            when(groupRepository.findByName(groupName)).thenReturn(Optional.of(GroupEntity.builder().build()));
        }

        private void configureGroupNotExist(String groupName) {
            when(groupRepository.findByName(groupName)).thenReturn(Optional.empty());
        }
    }

    @Nested
    @DisplayName("getGroups method")
    class getGroups {
        @Nested
        class Success {
            @Test
            @DisplayName("User not belongs to any group")
            public void emptyGroup() {
                configureUserGroups(1, List.of());

                List<GroupVo> res = groupService.getGroups(1);

                assertEquals(res.size(), 0);
            }

            @Test
            @DisplayName("User belongs to some groups")
            public void someGroups() {

                List<GroupEntity> groups = List.of(
                        GroupEntity.builder().id(1).name("g1").build(),
                        GroupEntity.builder().id(2).name("g2").build()
                );
                configureUserGroups(1, groups);

                List<GroupVo> res = groupService.getGroups(1);
                List<GroupVo> expected = groups.stream()
                        .map(g -> GroupVo.builder()
                                .id(g.getId())
                                .name(g.getName())
                                .build())
                        .toList();

                assertThat(res)
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(expected);
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("User is not found")
            public void userNotFound() {
                configureUserNotExist(1);

                GroupDto group = GroupDto.builder().name("test group").build();

                assertThrows(UserNotFoundException.class, () -> {
                    groupService.addGroup(1, group);
                });
            }
        }

        private void configureUserExist(int userId) {
            lenient().when(userRepository.findByIdWithGroups(userId)).thenReturn(Optional.of(UserEntity.builder().id(userId).build()));
        }

        private void configureUserNotExist(int userId) {
            lenient().when(userRepository.findByIdWithGroups(userId)).thenReturn(Optional.empty());
        }

        private void configureUserGroups(int userId, List<GroupEntity> groups) {
            when(userRepository.findByIdWithGroups(userId)).thenReturn(Optional.of(
                            UserEntity.builder()
                                    .id(userId)
                                    .groups(new HashSet<>(groups))
                                    .build()
                    )
            );
        }
    }

    @Nested
    @DisplayName("isUserInGroup method")
    class isUserInGroup {
        @Nested
        class Success {
            @Test
            @DisplayName("User not belongs to this group")
            public void userNotBelongToGroup() {
                configureFindByUserIdAndGroupId(1, 2, Optional.empty());
                boolean res = groupService.isUserInGroup(1, 2);

                assertFalse(res);
            }

            @Test
            @DisplayName("User belongs to this group")
            public void userBelongToGroup() {
                configureFindByUserIdAndGroupId(1, 2, Optional.of(UserGroup.builder().build()));

                boolean res = groupService.isUserInGroup(1, 2);

                assertTrue(res);
            }
        }


    }

    @Nested
    @DisplayName("addNewUser method")
    class addNewUser {
        @Nested
        class Success {
            @Test
            @DisplayName("User added tp group successfully")
            public void addedSuccessfully() {
                UserEntity user = UserEntity
                        .builder()
                        .id(1)
                        .name("test user")
                        .build();

                GroupEntity group = GroupEntity
                        .builder()
                        .id(1)
                        .name("test user")
                        .build();

                configureGroupRepositoryFindByIdWithUsersExist(group.getId(), List.of());
                configureUserRepositoryFindByIdExist(user.getId());

                configureFindByUserIdAndGroupId(user.getId(), group.getId(), Optional.empty());

                UserVo res = groupService.addNewUser(user.getId(), group.getId());
                UserVo expected = UserVo
                        .builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build();

                assertEquals(res.getId(), expected.getId());
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("Group does not exist")
            public void groupNotExist() {
                configureGroupRepositoryFindByIdWithUsersNotExist(1);

                assertThrows(GroupNotFoundException.class, () -> {
                    groupService.addNewUser(2, 1);
                });
            }

            @Test
            @DisplayName("User does not exist")
            public void userNotExist() {
                configureGroupRepositoryFindByIdWithUsersExist(1, List.of());
                configureUserRepositoryFindByIdNotExist(2);

                assertThrows(UserNotFoundException.class, () -> {
                    groupService.addNewUser(2, 1);
                });
            }

            @Test
            @DisplayName("User already in group")
            public void userAlreadyInGroup() {
                UserEntity user = UserEntity
                        .builder()
                        .id(1)
                        .name("test user")
                        .build();

                GroupEntity group = GroupEntity
                        .builder()
                        .id(1)
                        .name("test user")
                        .build();

                configureGroupRepositoryFindByIdWithUsersExist(1, List.of(user));
                configureUserRepositoryFindByIdExist(user.getId());

                configureFindByUserIdAndGroupId(user.getId(), 1,
                        Optional.of(UserGroup
                                .builder()
                                .userId(user.getId())
                                .groupId(group.getId()).build()
                        )
                );

                assertThrows(UserAlreadyInGroupException.class, () -> {
                    groupService.addNewUser(user.getId(), group.getId());
                });
            }
        }
    }

    @Nested
    @DisplayName("getUsers method")
    class getUsers {
        @Nested
        class Success {
            @Test
            @DisplayName("Group has no members")
            public void noMembers() {
                configureGroupRepositoryFindByIdWithUsersExist(1, List.of());

                List<UserVo> res = groupService.getUsers(1);

                assertEquals(res.size(), 0);
            }

            @Test
            @DisplayName("Group has some members")
            public void someMembers() {
                List<UserEntity> users = List.of(
                        UserEntity
                                .builder()
                                .id(1)
                                .name("user1")
                                .build(),
                        UserEntity
                                .builder()
                                .id(2)
                                .name("user2")
                                .build()
                );

                configureGroupRepositoryFindByIdWithUsersExist(1, users);

                List<UserVo> res = groupService.getUsers(1);

                List<UserVo> expected = users
                        .stream()
                        .map(u -> UserVo
                                .builder()
                                .id(u.getId())
                                .name(u.getName())
                                .build())
                        .toList();

                assertThat(res)
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(expected);
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("Group does not exist")
            public void groupNotExist() {
                configureGroupRepositoryFindByIdWithUsersNotExist(1);

                assertThrows(GroupNotFoundException.class, () -> {
                    groupService.getUsers(1);
                });
            }
        }
    }

    private void configureUserRepositoryFindByIdExist(int userId) {
        when(userRepository.findById(userId)).thenReturn(Optional.of(UserEntity.builder().id(userId).build()));
    }

    private void configureUserRepositoryFindByIdNotExist(int userId) {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
    }

    private void configureGroupRepositoryFindByIdWithUsersExist(int groupId, List<UserEntity> users) {
        lenient().when(groupRepository.findByIdWithUsers(groupId)).thenReturn(Optional.of(GroupEntity.builder().users(new HashSet<>(users)).build()));
    }

    private void configureGroupRepositoryFindByIdWithUsersNotExist(int groupId) {
        lenient().when(groupRepository.findByIdWithUsers(groupId)).thenReturn(Optional.empty());
    }

    private void configureFindByUserIdAndGroupId(int userId, int groupId, Optional<UserGroup> userGroup) {
        lenient().when(userGroupRepository.findByUserIdAndGroupId(groupId, userId)).thenReturn(userGroup);
    }
}