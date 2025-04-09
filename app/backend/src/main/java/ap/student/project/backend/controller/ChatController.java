package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ChatDTO;
import ap.student.project.backend.dto.ChatMemberDTO;
import ap.student.project.backend.dto.MessageDTO;
import ap.student.project.backend.entity.Chat;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    @GetMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllChats() {
        return ResponseEntity.ok(this.chatService.findAll());
    }
    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addChat(@RequestBody ChatDTO chatDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.chatService.save(chatDTO));
    }
    @PostMapping(value = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addMessage(@RequestBody MessageDTO messageDTO) {
        try {
            this.chatService.addMessage(messageDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(messageDTO);
        }
        catch(MissingArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping(value = "/chat/{id}/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addChatMember(@PathVariable("id") int id, @RequestBody ChatMemberDTO chatMemberDTO) {
        try {
            this.chatService.addChatMember(id, chatMemberDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(chatMemberDTO);
        }
        catch(MissingArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping(value = "/chat/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChat(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.chatService.findById(id));
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    @GetMapping(value = "/members/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChatMemberById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.chatService.findMemberById(id));
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping(value="/members/{id}/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChatByChatMemberId(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.chatService.findChatByMemberId(id));
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping(value = "/chat/{id}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChatMessages(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.chatService.findAllChatMessages(id));
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping(value = "/chat/{id}")
    public ResponseEntity updateChat(@PathVariable("id") int id, @RequestBody ChatDTO chatDTO) {
        try{
            this.chatService.updateChat(id, chatDTO);
            return ResponseEntity.ok(chatDTO);
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
