package uk.ac.leedsbeckett.student.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.leedsbeckett.student.exception.StudentException;
import uk.ac.leedsbeckett.student.model.Student;
import uk.ac.leedsbeckett.student.repository.StudentRepository;
import uk.ac.leedsbeckett.student.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Validated
@Component
public class ProfileService {

    private final PortalService portalService;
    private final StudentRepository studentRepository;

    public ProfileService(PortalService portalService, StudentRepository studentRepository) {
        this.portalService = portalService;
        this.studentRepository = studentRepository;
    }

    public ModelAndView getProfile(User user, Student student, @NotNull @NotEmpty String view) {
        return portalService.loadPortalUserDetails(user, student, view);
    }

    public ModelAndView getProfileToEdit(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(StudentException::new);
        ModelAndView modelAndView = new ModelAndView("update");
        modelAndView.addObject("student", student);
        return modelAndView;
    }

    public ModelAndView editProfile(Student providedStudent) {
        Student studentFromDatabase = studentRepository.findById(providedStudent.getId()).orElseThrow(StudentException::new);
        Student studentToSave = new Student();
        studentToSave.populateStudentId();
        BeanUtils.copyProperties(studentFromDatabase, studentToSave);

        if (providedStudent.getFirstname() != null && !providedStudent.getFirstname().isEmpty()) {
            studentToSave.setFirstname(providedStudent.getFirstname());
        }
        if (providedStudent.getSurname() != null && !providedStudent.getSurname().isEmpty()) {
            studentToSave.setSurname(providedStudent.getSurname());
        }
        Boolean changed = !studentFromDatabase.equals(studentToSave);
        Student returnedStudent = studentRepository.saveAndFlush(studentToSave);
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("student", returnedStudent);
        modelAndView.addObject("isStudent", true);
        modelAndView.addObject("updated", changed);
        modelAndView.addObject("message", changed ? "You have Successfully Updated your Profile" : "Your Profile Did not update because name already in use, Enter a new name!!!");
        return modelAndView;
    }
}
