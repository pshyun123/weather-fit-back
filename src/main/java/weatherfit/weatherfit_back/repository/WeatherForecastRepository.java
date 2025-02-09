package weatherfit.weatherfit_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weatherfit.weatherfit_back.entity.WeatherForecast;

import java.util.Optional;


@Repository
public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
    boolean existsById(Long id);
    Optional<WeatherForecast> findById(Long id);
}

