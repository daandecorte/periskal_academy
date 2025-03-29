package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.ExamAttempt;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserModule;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDTO userDTO) throws DuplicateException {
        User user = assemble(userDTO);
        if (userRepository.existsByPeriskalId(user.getPeriskalId())) {
            throw new DuplicateException("User with userid " + user.getPeriskalId() + " already exists");
        }
        userRepository.save(user);
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

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public List<UserModule> getAllUserModules(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new NotFoundException("user not found");
        List<UserModule> userModules = user.getUserModules();
        if (userModules == null) throw new NotFoundException("user does not have any user modules");
        return userModules;
    }

    public List<ExamAttempt> getAllExamAttempts(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new NotFoundException("user not found");
        List<UserModule> userModules = user.getUserModules();
        if (userModules == null) throw new NotFoundException("user does not have any user modules");
        List<ExamAttempt> examAttempts = new ArrayList<>();
        for (UserModule userModule : userModules) {
            examAttempts.addAll(userModule.getExamAttempts());
        }
        if (examAttempts.isEmpty()) throw new NotFoundException("user does not have any exam attempts");
        return examAttempts;
    }

    public User assemble(UserDTO userDTO) {
        return new User(userDTO.periskalId(), userDTO.firstname(), userDTO.lastname(), userDTO.shipname(), userDTO.language());
    }
}
