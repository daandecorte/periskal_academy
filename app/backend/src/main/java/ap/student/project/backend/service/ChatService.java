package ap.student.project.backend.service;

import ap.student.project.backend.dao.ChatMemberRepository;
import ap.student.project.backend.dao.ChatRepository;
import ap.student.project.backend.dao.MessageRepository;
import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.*;
import ap.student.project.backend.entity.Chat;
import ap.student.project.backend.entity.ChatMember;
import ap.student.project.backend.entity.Message;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Service class for managing chat-related operations.
 */
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserService userService;

    /**
     * Constructs a new ChatService with the required repositories and services.
     *
     * @param chatRepository Repository for Chat entity operations
     * @param messageRepository Repository for Message entity operations
     * @param chatMemberRepository Repository for ChatMember entity operations
     * @param userService Service for user-related operations
     */
    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository, ChatMemberRepository chatMemberRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.userService = userService;
    }

    /**
     * Saves a new chat based on the provided DTO.
     *
     * @param chatDTO Data transfer object containing chat information
     * @return The saved Chat entity
     */
    public Chat save(ChatDTO chatDTO) {
        Chat chat = new Chat();
        BeanUtils.copyProperties(chatDTO, chat);
        this.chatRepository.save(chat);
        return chat;
    }

    /**
     * Finds a chat by its ID.
     *
     * @param id The ID of the chat to find
     * @return The found Chat entity
     * @throws NotFoundException If no chat with the given ID exists
     */
    public Chat findById(int id) {
        Chat chat = this.chatRepository.findById(id).orElse(null);
        if (chat == null) {
            throw new NotFoundException("Chat with id" + id + " not found");
        }
        return chat;
    }

    /**
     * Retrieves all chats as summary DTOs.
     *
     * @return A list of chat summary DTOs containing basic chat information
     */
    public List<ChatSummaryDTO> findAll() {
        List<Chat> chats = this.chatRepository.findAll();
        List<ChatSummaryDTO> chatSummaries = new ArrayList<>();
        for (Chat chat : chats) {
            Set<ChatMember> chatMembers = chat.getChatMembers();
            List<ChatMemberSummaryDTO> chatMemberSummaries = new ArrayList<>();
            for (ChatMember chatMember : chatMembers) {
                ChatMemberSummaryDTO chatMemberSummaryDTO = new ChatMemberSummaryDTO(chatMember.getId(), chatMember.getUser());
                chatMemberSummaries.add(chatMemberSummaryDTO);
            }

            ChatSummaryDTO chatSummaryDTO = new ChatSummaryDTO(chat.getId(), chatMemberSummaries, chat.getChatStatus());
            chatSummaries.add(chatSummaryDTO);
        }
        return chatSummaries;
    }

    /**
     * Adds a new member to an existing chat.
     *
     * @param id The ID of the chat to add the member to
     * @param chatMemberDTO Data transfer object containing member information
     * @return The saved ChatMember entity
     * @throws MissingArgumentException If the user_id is missing from the DTO
     * @throws NotFoundException If the chat or user is not found
     */
    public ChatMember addChatMember(int id, ChatMemberDTO chatMemberDTO) {
        if(chatMemberDTO.user_id()==0) {
            throw new MissingArgumentException("user_id is missing");
        }
        Chat chat = this.findById(id);
        User user = this.userService.findById(chatMemberDTO.user_id());
        ChatMember chatMember = new ChatMember();
        chatMember.setChat(chat);
        chatMember.setUser(user);
        return this.chatMemberRepository.save(chatMember);
    }

    /**
     * Adds a new message to a chat.
     *
     * @param messageDTO Data transfer object containing message information
     * @throws MissingArgumentException If the chat_member_id is missing from the DTO
     * @throws NotFoundException If the chat member is not found
     */
    public void addMessage(MessageDTO messageDTO) {
        if(messageDTO.chat_member_id()==0) {
            throw new MissingArgumentException("chat_member_id is missing");
        }
        ChatMember chatMember = this.chatMemberRepository.findById(messageDTO.chat_member_id()).orElse(null);
        if(chatMember==null) {
            throw new NotFoundException("chat member with id " + messageDTO.chat_member_id() + " not found");
        }
        Message message = new Message();
        message.setChatMember(chatMember);
        message.setTextContent(messageDTO.textContent());
        message.setDateTime(LocalDateTime.now());
        this.messageRepository.save(message);
    }

    /**
     * Updates an existing chat with new information.
     *
     * @param id The ID of the chat to update
     * @param chatDTO Data transfer object containing updated chat information
     * @throws NotFoundException If the chat is not found
     */
    public void updateChat(int id, ChatDTO chatDTO) {
        Chat chat = this.findById(id);
        if(chat==null) {
            throw new NotFoundException("Chat with id" + id + " not found");
        }
        BeanUtils.copyProperties(chatDTO, chat);
        this.chatRepository.save(chat);
    }

    /**
     * Finds a chat member by its ID.
     *
     * @param id The ID of the chat member to find
     * @return The found ChatMember entity
     * @throws NotFoundException If no chat member with the given ID exists
     */
    public ChatMember findMemberById(int id) {
        ChatMember chatMember = this.chatMemberRepository.findById(id).orElse(null);
        if (chatMember == null) {
            throw new NotFoundException("Chat member with id " + id + " not found");
        }
        return chatMember;
    }

    /**
     * Finds a chat by its member's ID.
     *
     * @param id The ID of the chat member
     * @return The Chat entity associated with the member
     * @throws NotFoundException If no chat member with the given ID exists
     */
    public Chat findChatByMemberId(int id) {
        ChatMember chatMember = this.chatMemberRepository.findById(id).orElse(null);
        if (chatMember == null) {
            throw new NotFoundException("Chat member with id " + id + " not found");
        }
        return chatMember.getChat();
    }

    /**
     * Retrieves all messages from a chat sorted by date and time.
     *
     * @param id The ID of the chat to retrieve messages from
     * @return A list of chat message DTOs
     * @throws NotFoundException If the chat is not found
     */
    public List<ChatMessageDTO> findAllChatMessages(int id) {
        Chat chat = this.findById(id);
        List<ChatMessageDTO> chatMessages = new ArrayList<>();
        Set<ChatMember> chatMembers = chat.getChatMembers();
        for (ChatMember chatMember : chatMembers) {
            for (Message message: chatMember.getMessages()) {
                ChatMessageDTO chatMessageDTO = new ChatMessageDTO(chatMember.getId(), message.getDateTime(), message.getTextContent());
                chatMessages.add(chatMessageDTO);
            }
        }
        chatMessages.stream().sorted(Comparator.comparing(ChatMessageDTO::dateTime)).toList();
        return chatMessages;
    }
}
