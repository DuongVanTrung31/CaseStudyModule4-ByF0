package cg.casestudy4f0.model.dto;

import cg.casestudy4f0.model.entity.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpForm {
    private String username;
    private String password;
    private String email;
    private String roleName;
}
