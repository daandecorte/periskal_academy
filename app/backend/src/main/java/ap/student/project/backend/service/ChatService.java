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

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserService userService;

    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository, ChatMemberRepository chatMemberRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.chatMemberRepository = chatMemberRepository;
        this.userService = userService;
    }

    public Chat save(ChatDTO chatDTO) {
        Chat chat = new Chat();
        BeanUtils.copyProperties(chatDTO, chat);
        this.chatRepository.save(chat);
        return chat;
    }
    public Chat findById(int id) {
        Chat chat = this.chatRepository.findById(id).orElse(null);
        if (chat == null) {
            throw new NotFoundException("Chat with id" + id + " not found");
        }
        return chat;
    }
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

    public void updateChat(int id, ChatDTO chatDTO) {
        Chat chat = this.findById(id);
        if(chat==null) {
            throw new NotFoundException("Chat with id" + id + " not found");
        }
        BeanUtils.copyProperties(chatDTO, chat);
        this.chatRepository.save(chat);
    }
    public ChatMember findMemberById(int id) {
        ChatMember chatMember = this.chatMemberRepository.findById(id).orElse(null);
        if (chatMember == null) {
            throw new NotFoundException("Chat member with id " + id + " not found");
        }
        return chatMember;
    }
    public Chat findChatByMemberId(int id) {
        ChatMember chatMember = this.chatMemberRepository.findById(id).orElse(null);
        if (chatMember == null) {
            throw new NotFoundException("Chat member with id " + id + " not found");
        }
        return chatMember.getChat();
    }
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
