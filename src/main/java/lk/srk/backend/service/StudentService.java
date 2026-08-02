package lk.srk.backend.service;

import lk.srk.backend.model.Student;
import lk.srk.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // ============================================
    // GENERATE STUDENT ID - FIXED
    // ============================================

    private String generateStudentId() {
        try {
            // Find the highest student ID number
            List<Student> allStudents = studentRepository.findAll();

            if (allStudents.isEmpty()) {
                return "STU001";
            }

            int maxNumber = 0;
            for (Student student : allStudents) {
                String studentId = student.getStudentId();
                if (studentId != null && studentId.startsWith("STU")) {
                    try {
                        int number = Integer.parseInt(studentId.substring(3));
                        if (number > maxNumber) {
                            maxNumber = number;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore invalid IDs
                    }
                }
            }

            int nextNumber = maxNumber + 1;
            return String.format("STU%03d", nextNumber);

        } catch (Exception e) {
            // If something goes wrong, use timestamp-based ID
            return "STU" + System.currentTimeMillis() % 10000;
        }
    }

    // ============================================
    // CREATE STUDENT - FIXED
    // ============================================

    public Student createStudent(Student student) {
        try {
            // ✅ ALWAYS generate a new student ID
            String generatedId = generateStudentId();
            student.setStudentId(generatedId);
            System.out.println("📝 Auto-generated Student ID: " + generatedId);

            // Validate required fields
            if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
                throw new RuntimeException("Full name is required");
            }
            if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
                throw new RuntimeException("Email is required");
            }
            if (!student.getEmail().contains("@")) {
                throw new RuntimeException("Invalid email format");
            }

            // Trim and set fields
            student.setFullName(student.getFullName().trim());
            student.setEmail(student.getEmail().trim());
            if (student.getPhoneNumber() != null) {
                student.setPhoneNumber(student.getPhoneNumber().trim());
            }
            if (student.getCourse() != null) {
                student.setCourse(student.getCourse().trim());
            }
            if (student.getAddress() != null) {
                student.setAddress(student.getAddress().trim());
            }
            student.setRegistrationDate(LocalDateTime.now());

            // Check for duplicate email
            if (studentRepository.existsByEmail(student.getEmail())) {
                throw new RuntimeException("Email already exists: " + student.getEmail());
            }

            // Save the student
            Student savedStudent = studentRepository.save(student);
            System.out.println("✅ Student created with ID: " + savedStudent.getStudentId());
            return savedStudent;

        } catch (Exception e) {
            System.err.println("❌ Student creation error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create student: " + e.getMessage());
        }
    }

    // ============================================
    // OTHER METHODS (Unchanged)
    // ============================================

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }

    public Student updateStudent(String id, Student studentDetails) {
        try {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

            if (studentDetails.getFullName() != null) {
                student.setFullName(studentDetails.getFullName().trim());
            }
            if (studentDetails.getEmail() != null) {
                student.setEmail(studentDetails.getEmail().trim());
            }
            if (studentDetails.getPhoneNumber() != null) {
                student.setPhoneNumber(studentDetails.getPhoneNumber().trim());
            }
            if (studentDetails.getCourse() != null) {
                student.setCourse(studentDetails.getCourse().trim());
            }
            if (studentDetails.getAge() != null) {
                student.setAge(studentDetails.getAge());
            }
            if (studentDetails.getGender() != null) {
                student.setGender(studentDetails.getGender());
            }
            if (studentDetails.getAddress() != null) {
                student.setAddress(studentDetails.getAddress().trim());
            }

            return studentRepository.save(student);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update student: " + e.getMessage());
        }
    }

    public void deleteStudent(String id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    public List<Student> searchStudents(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return studentRepository.findAll();
        }
        return studentRepository.searchStudents(searchTerm.trim());
    }

    public long getTotalStudents() {
        return studentRepository.count();
    }
}