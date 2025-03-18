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
    private final UserModuleRepository userModuleRepository;
    private final UserExamRepository userExamRepository;
    private final ExamRepository examRepository;
    private final ModuleRepository moduleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserModuleRepository userModuleRepository,
                       UserExamRepository userExamRepository,
                       ExamRepository examRepository,
                       ModuleRepository moduleRepository
    ) {
        this.userRepository = userRepository;
        this.userModuleRepository = userModuleRepository;
        this.userExamRepository = userExamRepository;
        this.examRepository = examRepository;
        this.moduleRepository = moduleRepository;
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
    public UserModule addUserModule(int id, int moduleId) throws NotFoundException, DuplicateException {
        User user = userRepository.findById(id).orElse(null);
        if(user==null)
            throw new NotFoundException("user not found");
        Module module = moduleRepository.findById(moduleId).orElse(null);
        if(module==null)
            throw new NotFoundException("module not found");
        if(user.getUserModules()==null)
            user.setUserModules(new ArrayList<>());
        for (UserModule userModule : user.getUserModules()) {
            if(userModule.getModule().equals(module)) {
                throw new DuplicateException("user module already exists");
            }
        }
        UserModule userModule = new UserModule(null, module, user);
        userModuleRepository.save(userModule);
        user.getUserModules().add(userModule);
        userRepository.save(user);
        return userModule;
    }
/*
    public UserExam addUserExam(int id, int examId) throws NotFoundException, DuplicateException {
        User user = userRepository.findById(id).orElse(null);
        if(user==null)
            throw new NotFoundException("user not found");
        Exam exam = examRepository.findById(examId).orElse(null);
        if(exam==null)
            throw new NotFoundException("exam not found");
        if(user.getUserExams()==null)
            user.setUserExams(new ArrayList<>());
        for (UserExam userExam : user.getUserExams()) {
            if(userExam.getExam().equals(exam)) {
                throw new DuplicateException("user exam already exists");
            }
        }
        UserExam userExam = new UserExam(exam, new ArrayList<>(), user);
        userExamRepository.save(userExam);
        user.getUserExams().add(userExam);
        userRepository.save(user);
        return userExam;
    }
*/

    public User assemble(UserDTO userDTO) {
        return new User(userDTO.userId(), userDTO.language());
    }
}
