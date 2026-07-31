package lk.srk.backend.dto;

import lombok.Data;

@Data
public class StudentRequest {
    private String studentId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String course;
    private Integer age;
    private String gender;
    private String address;
}