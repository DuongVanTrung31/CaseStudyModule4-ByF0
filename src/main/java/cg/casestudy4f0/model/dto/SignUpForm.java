package cg.casestudy4f0.model.dto;


import lombok.Data;



@Data
public class SignUpForm {
    private String username;
    private String password;
    private String email;
    private String roleName;
}
