package weatherfit.weatherfit_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coordinate")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coordinate {
    @Id
    @Column(name = "coordinate_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tpo", nullable = false, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String tpo;

    @Column(name ="weather_condition", nullable = false)
    private String weatherCondition;

    @Column(name = "coordinateImg", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'default.jpg'")
    private String coordinateImg;

    @Column(name ="preference", nullable = false, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String preference;

    @Column(name = "targetAgeGroup", nullable = false, columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String targetAgeGroup;

    @OneToMany(mappedBy = "coordinate", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();
    

    // preference 필드의 getter 메서드 오버라이드
    public String getPreference() {
        System.out.println("Preference 원본 값: " + preference);
        if (preference != null) {
            System.out.println("Preference 길이: " + preference.length());
            System.out.println("Preference 바이트 배열: " + java.util.Arrays.toString(preference.getBytes()));
        }
        return preference;
    }

    // targetAgeGroup 필드의 getter 메서드 오버라이드
    public String getTargetAgeGroup() {
        System.out.println("TargetAgeGroup 원본 값: " + targetAgeGroup);
        if (targetAgeGroup != null) {
            System.out.println("TargetAgeGroup 길이: " + targetAgeGroup.length());
            System.out.println("TargetAgeGroup 바이트 배열: " + java.util.Arrays.toString(targetAgeGroup.getBytes()));
        }
        return targetAgeGroup;
    }
}

