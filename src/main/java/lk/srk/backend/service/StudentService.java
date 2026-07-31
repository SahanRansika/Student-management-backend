package lk.srk.backend.service;

import lk.srk.backend.model.Student;
import lk.srk.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        try {
            return studentRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch students: " + e.getMessage());
        }
    }

    public Optional<Student> getStudentById(String id) {
        try {
            return studentRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch student: " + e.getMessage());
        }
    }

    public Student createStudent(Student student) {
        try {
            if (studentRepository.existsByStudentId(student.getStudentId())) {
                throw new RuntimeException("Student ID already exists: " + student.getStudentId());
            }
            if (studentRepository.existsByEmail(student.getEmail())) {
                throw new RuntimeException("Email already exists: " + student.getEmail());
            }
            return studentRepository.save(student);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create student: " + e.getMessage());
        }
    }

    public Student updateStudent(String id, Student studentDetails) {
        try {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

            student.setFullName(studentDetails.getFullName());
            student.setEmail(studentDetails.getEmail());
            student.setPhoneNumber(studentDetails.getPhoneNumber());
            student.setCourse(studentDetails.getCourse());
            student.setAge(studentDetails.getAge());
            student.setGender(studentDetails.getGender());
            student.setAddress(studentDetails.getAddress());

            return studentRepository.save(student);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update student: " + e.getMessage());
        }
    }

    public void deleteStudent(String id) {
        try {
            if (!studentRepository.existsById(id)) {
                throw new RuntimeException("Student not found with id: " + id);
            }
            studentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete student: " + e.getMessage());
        }
    }

    public List<Student> searchStudents(String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return studentRepository.findAll();
            }
            return studentRepository.searchStudents(searchTerm.trim());
        } catch (Exception e) {
            throw new RuntimeException("Failed to search students: " + e.getMessage());
        }
    }

    public long getTotalStudents() {
        try {
            return studentRepository.count();
        } catch (Exception e) {
            throw new RuntimeException("Failed to count students: " + e.getMessage());
        }
    }
}