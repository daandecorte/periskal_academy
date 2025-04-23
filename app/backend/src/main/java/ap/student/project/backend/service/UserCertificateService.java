package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserCertificateRepository;
import ap.student.project.backend.dto.UserCertificateDTO;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserCertificate;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCertificateService {
    private final UserCertificateRepository userCertificateRepository;
    private final UserService userService;
    private final CertificateService certificateService;
    public UserCertificateService(UserCertificateRepository userCertificateRepository, UserService userService, CertificateService certificateService) {
        this.userCertificateRepository = userCertificateRepository;
        this.userService = userService;
        this.certificateService = certificateService;
    }
    public UserCertificate save(UserCertificateDTO userCertificateDTO) {
        UserCertificate userCertificate = new UserCertificate();
        if(userCertificateDTO.user_id()==0) {
            throw new MissingArgumentException("user_id is missing");
        }
        if(userCertificateDTO.certificate_id()==0) {
            throw new MissingArgumentException("certificate_id is missing");
        }
        userCertificate.setUser(userService.findById(userCertificateDTO.user_id()));
        userCertificate.setCertificate(certificateService.findById(userCertificateDTO.certificate_id()));
        BeanUtils.copyProperties(userCertificateDTO, userCertificate);
        return userCertificateRepository.save(userCertificate);
    }
    public List<UserCertificate> findAll() {
        return userCertificateRepository.findAll();
    }
    public List<UserCertificate> findByUserId(int id) {
        User user = this.userService.findById(id);
        if(user.getUserCertificates().isEmpty()) {
            throw new NotFoundException("User with id " + id + " has no certificates");
        }
        return user.getUserCertificates();
    }
    public UserCertificate findById(int id) {
        UserCertificate userCertificate = this.userCertificateRepository.findById(id).orElse(null);
        if(userCertificate == null) {
            throw new NotFoundException("User Certificate with id " + id + " not found");
        }
        return userCertificate;
    }
}
