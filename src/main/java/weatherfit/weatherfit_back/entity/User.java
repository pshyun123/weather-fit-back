package weatherfit.weatherfit_back.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import weatherfit.weatherfit_back.constant.Authority;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@NoArgsConstructor
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
    @Column(name = "profileImage", nullable = false, length = 100000)
    private String profileImage;
    @Column(name = "ageGroup", nullable = false)
    private String ageGroup;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "is_deleted",columnDefinition = "TINYINT(1)" ,nullable = false)
    private boolean isDeleted;

    @Builder
    public User(String email, String password, String name, String profileImage, String ageGroup, Authority authority, boolean isDeleted) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage != null ? profileImage : "default.jpg";
        this.ageGroup = ageGroup;
        this.authority = authority != null ? authority : Authority.ROLE_USER;
        this.isDeleted = isDeleted;
    }
}

