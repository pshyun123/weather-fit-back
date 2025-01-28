package weatherfit.weatherfit_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import weatherfit.weatherfit_back.constant.Authority;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "profileImage", nullable = false)
    private String profileImage;
    @Column(name = "ageGroup", nullable = false)
    private String ageGroup;

    @Enumerated(EnumType.STRING)
    private Authority authority;
}

