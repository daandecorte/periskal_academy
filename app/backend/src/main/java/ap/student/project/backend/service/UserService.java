package ap.student.project.backend.service;

import ap.student.project.backend.dao.*;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.*;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDTO userDTO) throws DuplicateException {
        User user = assemble(userDTO);
        if(userRepository.existsByUserId(user.getUserId())) {
            throw new DuplicateException("User with userid " + user.getUserId() + " already exists");
        }
        userRepository.save(user);
    }
    public void update(UserDTO userDTO) throws NotFoundException {
        User updatedUser = userRepository.findByUserId(userDTO.userId());
        if(updatedUser == null) {
            throw new NotFoundException("User with userid " + userDTO.userId() + " not found");
        }
        updatedUser.setLanguage(userDTO.language());
        userRepository.save(updatedUser);
    }

    public User findById(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return user;
    }
    public User findByUserId(String userId) throws NotFoundException {
        User user = userRepository.findByUserId(userId);
        if(user == null) {
            throw new NotFoundException("User with userid " + userId + " not found");
        }
        return user;
    }
    public boolean existsByUserId(String userId) {
        User user = userRepository.findByUserId(userId);
        return user != null;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public List<UserModule> getAllUserModules(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if(user==null) throw new NotFoundException("user not found");
        List<UserModule> userModules=user.getUserModules();
        if(userModules==null) throw new NotFoundException("user does not have any user modules");
        return userModules;
    }
    public List<UserExam> getAllUserExams(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if(user==null) throw new NotFoundException("user not found");
        List<UserExam> userExams=user.getUserExams();
        if(userExams==null) throw new NotFoundException("user does not have any user exams");
        return userExams;
    }
    public User assemble(UserDTO userDTO) {
        return new User(userDTO.userId(), userDTO.firstname(), userDTO.lastname(), userDTO.shipname(), userDTO.language());
    }
}
