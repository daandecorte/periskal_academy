package ap.student.project.backend.service;

import ap.student.project.backend.controller.UserDTO;
import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserDTO userDTO) {
        User user = assembleUser(userDTO);
        userRepository.save(user);
    }

    public User findById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public User assembleUser(UserDTO userDTO) {
        return new User(userDTO.dongleId(), userDTO.fleetManagerId(), userDTO.name(), userDTO.email(), userDTO.role(), userDTO.language(), userDTO.userExams(), userDTO.userModules());
    }
}
