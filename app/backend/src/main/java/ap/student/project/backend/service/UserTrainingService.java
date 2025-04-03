package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserTrainingRepository;
import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTrainingService {

    private final UserTrainingRepository userTrainingRepository;
    private final UserService userService;
    private final TrainingService trainingService;

    @Autowired
    public UserTrainingService(UserTrainingRepository userTrainingRepository, UserService userService, TrainingService trainingService) {
        this.userTrainingRepository = userTrainingRepository;
        this.userService = userService;
        this.trainingService = trainingService;
    }

    public void save(UserTrainingDTO userTrainingDTO) {
        UserTraining userTraining = new UserTraining();
        if(userTrainingDTO.user_id()==0) {
            throw new MissingArgumentException("user_id is missing");
        }
        if(userTrainingDTO.training_id()==0) {
            throw new MissingArgumentException("training_id is missing");
        }
        User user = userService.findById(userTrainingDTO.user_id());
        Training training = trainingService.findById(userTrainingDTO.training_id());
        userTraining.setTraining(training);
        userTraining.setUser(user);
        userTrainingRepository.save(userTraining);
    }
    public UserTraining findById(int id) throws NotFoundException {
        UserTraining userTraining = this.userTrainingRepository.findById(id).orElse(null);
        if (userTraining == null) {
            throw new NotFoundException("User Training With Id " + id + " Not Found");
        }
        return userTraining;
    }
    public List<UserTraining> findAll() {
        return userTrainingRepository.findAll();
    }
}
