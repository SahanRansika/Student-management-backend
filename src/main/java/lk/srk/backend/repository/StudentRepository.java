package lk.srk.backend.repository;

import lk.srk.backend.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    List<Student> findByFullNameContainingIgnoreCase(String name);
    List<Student> findByEmailContainingIgnoreCase(String email);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);

    @Query("{ '$or': [ { 'fullName': { $regex: ?0, $options: 'i' } }, { 'email': { $regex: ?0, $options: 'i' } } ] }")
    List<Student> searchStudents(String searchTerm);
}