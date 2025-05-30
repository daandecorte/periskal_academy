package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ChatDTO;
import ap.student.project.backend.dto.ChatMemberDTO;
import ap.student.project.backend.dto.MessageDTO;
import ap.student.project.backend.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling chat-related HTTP requests.
 * Manages operations for chats including creation, retrieval, updates,
 * and message management within chats.
 */
@CrossOrigin
@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Retrieves all chats from the system.
     *
     * @return ResponseEntity containing a list of all chats with HTTP status 200 (OK)
     */
    @GetMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllChats() {
        return ResponseEntity.ok(this.chatService.findAll());
    }

    /**
     * Creates a new chat in the system.
     *
     * @param chatDTO The chat data transfer object containing chat information
     * @return ResponseEntity containing the created chat with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addChat(@RequestBody ChatDTO chatDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.chatService.save(chatDTO));
    }

    /**
     * Adds a new message to a chat.
     *
     * @param messageDTO The message data transfer object containing message information
     * @return ResponseEntity containing the added message with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addMessage(@RequestBody MessageDTO messageDTO) {
        this.chatService.addMessage(messageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageDTO);
    }

    /**
     * Adds a new member to a specific chat.
     *
     * @param id            The ID of the chat to add a member to
     * @param chatMemberDTO The chat member data transfer object containing member information
     * @return ResponseEntity containing the added chat member with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/chat/{id}/members", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addChatMember(@PathVariable("id") int id, @RequestBody ChatMemberDTO chatMemberDTO) {
        this.chatService.addChatMember(id, chatMemberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatMemberDTO);
    }

    /**
     * Retrieves a specific chat by its ID.
     *
     * @param id The ID of the chat to retrieve
     * @return ResponseEntity containing the chat with HTTP status 200 (OK)
     */
    @GetMapping(value = "/chat/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChat(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.chatService.findById(id));
    }

    /**
     * Retrieves a chat member by its ID.
     *
     * @param id The ID of the chat member to retrieve
     * @return ResponseEntity containing the chat member with HTTP status 200 (OK)
     */
    @GetMapping(value = "/members/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChatMemberById(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.chatService.findMemberById(id));
    }

    /**
     * Retrieves a chat associated with a specific member ID.
     *
     * @param id The member ID to find the chat for
     * @return ResponseEntity containing the chat with HTTP status 200 (OK)
     */
    @GetMapping(value = "/members/{id}/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChatByChatMemberId(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.chatService.findChatByMemberId(id));
    }

    /**
     * Retrieves all messages for a specific chat.
     *
     * @param id The ID of the chat to retrieve messages for
     * @return ResponseEntity containing a list of chat messages with HTTP status 200 (OK)
     */
    @GetMapping(value = "/chat/{id}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChatMessages(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.chatService.findAllChatMessages(id));
    }

    /**
     * Updates a specific chat with new information.
     *
     * @param id      The ID of the chat to update
     * @param chatDTO The chat data transfer object containing updated chat information
     * @return ResponseEntity containing the updated chat with HTTP status 200 (OK)
     */
    @PutMapping(value = "/chat/{id}")
    public ResponseEntity updateChat(@PathVariable("id") int id, @RequestBody ChatDTO chatDTO) {
        this.chatService.updateChat(id, chatDTO);
        return ResponseEntity.ok(chatDTO);
    }
}
