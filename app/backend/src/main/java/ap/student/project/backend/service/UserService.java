package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.ChatMember;
import ap.student.project.backend.entity.ExamAttempt;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(UserDTO userDTO) throws DuplicateException {
        User user = assemble(userDTO);
        if (userRepository.existsByPeriskalId(user.getPeriskalId())) {
            throw new DuplicateException("User with userid " + user.getPeriskalId() + " already exists");
        }
        return userRepository.save(user);
    }

    public void update(UserDTO userDTO) throws NotFoundException {
        User updatedUser = userRepository.findByPeriskalId(userDTO.periskalId());
        if (updatedUser == null) {
            throw new NotFoundException("User with userid " + userDTO.periskalId() + " not found");
        }
        updatedUser.setLanguage(userDTO.language());
        userRepository.save(updatedUser);
    }

    public User findById(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    public User findByPeriskalId(String periskalId) throws NotFoundException {
        User user = userRepository.findByPeriskalId(periskalId);
        if (user == null) {
            throw new NotFoundException("User with periskal id " + periskalId + " not found");
        }
        return user;
    }

    public boolean existsByPeriskalId(String userId) {
        User user = userRepository.findByPeriskalId(userId);
        return user != null;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public List<UserTraining> getAllUserModules(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new NotFoundException("user not found");
        List<UserTraining> userTrainings = user.getUserTrainings();
        if (userTrainings == null) throw new NotFoundException("user does not have any user trainings");
        return userTrainings;
    }

    public List<ExamAttempt> getAllExamAttempts(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new NotFoundException("user not found");
        List<UserTraining> userTrainings = user.getUserTrainings();
        if (userTrainings == null) throw new NotFoundException("user does not have any user trainings");
        List<ExamAttempt> examAttempts = new ArrayList<>();
        for (UserTraining userTraining : userTrainings) {
            examAttempts.addAll(userTraining.getExamAttempts());
        }
        if (examAttempts.isEmpty()) throw new NotFoundException("user does not have any exam attempts");
        return examAttempts;
    }
    public List<ChatMember> getChatMember(int id) throws NotFoundException {
        User user = this.findById(id);
        List<ChatMember> chatMembers = user.getChatMembers();
        if (chatMembers.isEmpty()) {
            throw new NotFoundException("this user has no chatmembers");
        }
        return chatMembers;
    }

    public User assemble(UserDTO userDTO) {
        return new User(userDTO.periskalId(), userDTO.firstname(), userDTO.lastname(), userDTO.shipname(), userDTO.language());
    }
}
