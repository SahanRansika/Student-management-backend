package lk.srk.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "students")
public class Student {
    @Id
    private String id;

    @Indexed(unique = true)
    private String studentId;

    private String fullName;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;
    private String course;
    private Integer age;
    private String gender;
    private String address;
    private LocalDateTime registrationDate;

    public Student(String studentId, String fullName, String email, String phoneNumber,
                   String course, Integer age, String gender, String address) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.course = course;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.registrationDate = LocalDateTime.now();
    }
}