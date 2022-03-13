package cg.casestudy4f0.model.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String avatar;

    @ManyToOne
    private Role role;
}
