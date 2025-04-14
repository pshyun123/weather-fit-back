package weatherfit.weatherfit_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import weatherfit.weatherfit_back.entity.Coordinate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateDTO {

    private Long id;
    private String coordinateImg;
    private String preference;
    private String targetAgeGroup;
    private String tpo;
    private String weatherCondition;
    private boolean isLiked;

    public static CoordinateDTO of(Coordinate coordinate) {
        return CoordinateDTO.builder()
                .id(coordinate.getId())
                .tpo(coordinate.getTpo())
                .weatherCondition(coordinate.getWeatherCondition())
                .coordinateImg(coordinate.getCoordinateImg())
                .preference(coordinate.getPreference())
                .targetAgeGroup(coordinate.getTargetAgeGroup())
                .isLiked(false)
                .build();
    }

    public static CoordinateDTO from(Coordinate coordinate) {
        return CoordinateDTO.builder()
                .id(coordinate.getId())
                .weatherCondition(coordinate.getWeatherCondition())
                .preference(coordinate.getPreference())
                .targetAgeGroup(coordinate.getTargetAgeGroup())
                .coordinateImg(coordinate.getCoordinateImg())
                .isLiked(false)
                .build();
    }
}
