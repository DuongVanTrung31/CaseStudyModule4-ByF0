package cg.casestudy4f0.model.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class SignUpForm {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String roleName;
}
