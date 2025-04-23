package ap.student.project.backend.service;

import ap.student.project.backend.dao.MessageRepository;
import ap.student.project.backend.dto.*;
import ap.student.project.backend.entity.*;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;
    @Autowired
    private EntityManager entityManager;

    private User testUser;
    private Chat testChat;
    private ChatDTO testChatDTO;
    private UserDTO testUserDTO;
    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO("a", "b", "c", "d", Language.ENGLISH);
        testUser = userService.save(testUserDTO);

        testChatDTO = new ChatDTO(ChatStatus.IN_PROGRESS);
        testChat = chatService.save(testChatDTO);
        testChat.setChatMembers(new HashSet<>());
    }

    @Test
    void save_shouldPersistChat() {

        Chat saved = chatService.save(testChatDTO);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getChatStatus()).isEqualTo(ChatStatus.IN_PROGRESS);
    }

    @Test
    void findById_shouldReturnChat() {
        Chat found = chatService.findById(testChat.getId());
        assertThat(found).isNotNull();
        assertThat(found.getChatStatus()).isEqualTo(ChatStatus.IN_PROGRESS);
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        assertThrows(NotFoundException.class, () -> chatService.findById(0));
    }

    @Test
    void findAll_shouldReturnList() {
        List<ChatSummaryDTO> chats = chatService.findAll();
        assertThat(chats).isNotEmpty();
    }

    @Test
    void addChatMember_shouldAddUserToChat() {
        ChatMemberDTO dto = new ChatMemberDTO(testUser.getId(), testChat.getId());
        ChatMember chatMember = chatService.addChatMember(testChat.getId(), dto);
        entityManager.refresh(testChat);
        entityManager.refresh(chatMember);
        Set<ChatMember> members = chatService.findById(testChat.getId()).getChatMembers();
        assertThat(members).hasSize(1);
        assertThat(members.iterator().next().getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void addChatMember_shouldThrowOnMissingUserId() {
        ChatMemberDTO invalidDto = new ChatMemberDTO(0, 0);
        assertThrows(MissingArgumentException.class, () -> chatService.addChatMember(testChat.getId(), invalidDto));
    }

    @Test
    void addMessage_shouldStoreMessage() {
        ChatMember savedMember = chatService.addChatMember(testChat.getId(), new ChatMemberDTO(testUser.getId(), testChat.getId()));
        entityManager.refresh(testChat);
        entityManager.refresh(savedMember);
        ChatMember member = testChat.getChatMembers().iterator().next();

        MessageDTO dto = new MessageDTO("Hello world" ,member.getId());
        chatService.addMessage(dto);
        entityManager.flush();
        entityManager.refresh(testChat);
        entityManager.refresh(member);
        List<ChatMessageDTO> messages = chatService.findAllChatMessages(testChat.getId());
        assertThat(messages).hasSize(1);
        assertThat(messages.get(0).textContent()).isEqualTo("Hello world");
    }

    @Test
    void addMessage_shouldThrowIfMemberMissing() {
        MessageDTO dto = new MessageDTO("Test", 0);
        assertThrows(MissingArgumentException.class, () -> chatService.addMessage(dto));
    }

    @Test
    void updateChat_shouldUpdateChat() {
        ChatDTO updateDTO = new ChatDTO(ChatStatus.RESOLVED);
        chatService.updateChat(testChat.getId(), updateDTO);

        Chat updated = chatService.findById(testChat.getId());
        assertThat(updated.getChatStatus()).isEqualTo(ChatStatus.RESOLVED);
    }

    @Test
    void updateChat_shouldThrowWhenNotFound() {
        ChatDTO dto = new ChatDTO(ChatStatus.RESOLVED);
        assertThrows(NotFoundException.class, () -> chatService.updateChat(-1, dto));
    }

    @Test
    void findMemberById_shouldReturnMember() {
        chatService.addChatMember(testChat.getId(), new ChatMemberDTO(testUser.getId(), testChat.getId()));
        entityManager.refresh(testChat);
        ChatMember member = testChat.getChatMembers().iterator().next();

        ChatMember found = chatService.findMemberById(member.getId());
        assertThat(found.getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void findMemberById_shouldThrowIfNotFound() {
        assertThrows(NotFoundException.class, () -> chatService.findMemberById(-1));
    }

    @Test
    void findChatByMemberId_shouldReturnChat() {
        chatService.addChatMember(testChat.getId(), new ChatMemberDTO(testUser.getId(), testChat.getId()));
        entityManager.refresh(testChat);
        ChatMember member = testChat.getChatMembers().iterator().next();

        Chat found = chatService.findChatByMemberId(member.getId());
        assertThat(found.getId()).isEqualTo(testChat.getId());
    }

    @Test
    void findChatByMemberId_shouldThrowIfNotFound() {
        assertThrows(NotFoundException.class, () -> chatService.findChatByMemberId(-1));
    }

    @Test
    void findAllChatMessages_shouldReturnOrderedMessages() {
        chatService.addChatMember(testChat.getId(), new ChatMemberDTO(testUser.getId(), testChat.getId()));
        entityManager.refresh(testChat);
        ChatMember member = testChat.getChatMembers().iterator().next();

        chatService.addMessage(new MessageDTO("First", member.getId()));
        chatService.addMessage(new MessageDTO("Second", member.getId()));

        entityManager.flush();

        List<Message> messages = messageRepository.findAll();

        assertThat(messages).hasSize(2);  // Check if two messages were saved
        assertThat(messages.get(0).getTextContent()).isEqualTo("First");
        assertThat(messages.get(1).getTextContent()).isEqualTo("Second");
    }
}
