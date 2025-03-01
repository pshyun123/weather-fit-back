package weatherfit.weatherfit_back.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import weatherfit.weatherfit_back.entity.Like;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private Long id;
    private Long userId;
    private Long coordinateId;
    private String preference;
    private String targetAgeGroup;
    private String tpo;
    private String weatherCondition;
    private String coordinateImg;

    public static LikeDTO of(Like like) {
        return LikeDTO.builder()
                .id(like.getId())
                .userId(like.getUser().getId())
                .coordinateId(like.getCoordinate().getId())
                .preference(like.getCoordinate().getPreference())
                .targetAgeGroup(like.getCoordinate().getTargetAgeGroup())
                .tpo(like.getCoordinate().getTpo())
                .weatherCondition(like.getCoordinate().getWeatherCondition())
                .coordinateImg(like.getCoordinate().getCoordinateImg())
                .build();
    }
}