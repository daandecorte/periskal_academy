package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserExamRepository;
import ap.student.project.backend.dao.UserModuleRepository;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserExam;
import ap.student.project.backend.entity.UserModule;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserModuleRepository userModuleRepository;
    private final UserExamRepository userExamRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserModuleRepository userModuleRepository,
                       UserExamRepository userExamRepository
    ) {
        this.userRepository = userRepository;
        this.userModuleRepository = userModuleRepository;
        this.userExamRepository = userExamRepository;
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
    /*
    public List<UserExam> addUserExam(int id, int examId) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if(user==null) throw new NotFoundException("user not found");
        List<UserExam> userExams=user.getUserExams();
        if(userExams==null) user.setUserExams(new ArrayList<>());
    }
    */

    public User assembleUser(UserDTO userDTO) {
        return new User(userDTO.userId(), userDTO.fleetManagerId(), userDTO.name(), userDTO.email(), userDTO.role(), userDTO.language(), userDTO.userExams(), userDTO.userModules());
    }
}
