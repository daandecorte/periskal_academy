package ap.student.project.backend.service;

import ap.student.project.backend.dao.ChatMemberRepository;
import ap.student.project.backend.dao.ChatRepository;
import ap.student.project.backend.dao.MessageRepository;
import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.ChatDTO;
import ap.student.project.backend.dto.ChatMemberDTO;
import ap.student.project.backend.dto.MessageDTO;
import ap.student.project.backend.entity.Chat;
import ap.student.project.backend.entity.ChatMember;
import ap.student.project.backend.entity.Message;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<Chat> findAll() {
        return this.chatRepository.findAll();
    }
    public void addChatMember(int id, ChatMemberDTO chatMemberDTO) {
        if(chatMemberDTO.user_id()==0) {
            throw new MissingArgumentException("user_id is missing");
        }
        Chat chat = this.findById(id);
        User user = this.userService.findById(chatMemberDTO.user_id());
        ChatMember chatMember = new ChatMember();
        chatMember.setChat(chat);
        chatMember.setUser(user);
        this.chatMemberRepository.save(chatMember);
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
}
