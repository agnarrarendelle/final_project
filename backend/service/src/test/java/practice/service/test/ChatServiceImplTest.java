package practice.service.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import practice.dto.ChatMessageDto;
import practice.entity.Chat;
import practice.entity.ChatHistory;
import practice.entity.GroupEntity;
import practice.entity.UserEntity;
import practice.exception.GroupNotFoundException;
import practice.repository.ChatHistoryRepository;
import practice.repository.ChatRepository;
import practice.repository.GroupRepository;
import practice.service.impl.ChatServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {
    @Mock
    GroupRepository groupRepository;

    @Mock
    ChatHistoryRepository chatHistoryRepository;

    @Mock
    ChatRepository chatRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    ChatServiceImpl chatService;

    @Nested
    @DisplayName("saveNewMessage method")
    class saveNewMessage {
        @Nested
        class Success {
            @Test
            @DisplayName("Get chat messages successfully")
            public void getChatMessagesSuccessfully() {
                int groupId = 5;
                ChatMessageDto dto = ChatMessageDto
                        .builder()
                        .message("test message")
                        .userId(1)
                        .build();

                Chat chat = Chat.builder().build();

                configureGroupRepositoryFindById(groupId,
                        Optional.of(GroupEntity
                                .builder()
                                .chat(chat)
                                .build()
                        )
                );

                chatService.saveNewMessage(groupId, dto);

                verify(chatHistoryRepository, times(1)).save(any(ChatHistory.class));
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("Group not exists")
            public void groupNotExist() {
                configureGroupRepositoryFindById(1, Optional.empty());

                ChatMessageDto dto = ChatMessageDto.builder().build();

                assertThrows(GroupNotFoundException.class, () -> {
                    chatService.saveNewMessage(1, dto);
                });
            }
        }

        private void configureGroupRepositoryFindById(int groupId, Optional<GroupEntity> res) {
            when(groupRepository.findById(groupId)).thenReturn(res);
        }
    }

    @Nested
    @DisplayName("getChat method")
    class getChat {
        @Nested
        class Success {
            @Test
            @DisplayName("get chat messages successfully")
            public void getChatMessagesSuccessfully() {
                List<ChatHistory> chatMessages = List.of(
                        ChatHistory
                                .builder()
                                .userId(1)
                                .user(UserEntity
                                        .builder()
                                        .name("user 1")
                                        .build())
                                .content("message 1")
                                .build(),
                        ChatHistory
                                .builder()
                                .userId(2)
                                .user(UserEntity
                                        .builder()
                                        .name("user 2")
                                        .build())
                                .content("message 2")
                                .build()
                );

                configureChatRepositoryFindByIdWithChatHistory(1, chatMessages);

                List<ChatMessageDto> res = chatService.getChat(1, 2);

                List<ChatMessageDto> expected = List.of(
                        ChatMessageDto
                                .builder()
                                .userId(1)
                                .message("message 1")
                                .userName("user 1")
                                .build(),
                        ChatMessageDto
                                .builder()
                                .userId(2)
                                .message("message 2")
                                .userName("user 2")
                                .build()
                );

                assertThat(res)
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(expected);
            }
        }

        private void configureChatRepositoryFindByIdWithChatHistory(int groupId, List<ChatHistory> res) {
            when(chatRepository.findByIdWithChatHistory(groupId)).thenReturn(Chat.builder().chatMessages(res).build());
        }
    }
}