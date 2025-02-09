package weatherfit.weatherfit_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weatherfit.weatherfit_back.entity.Weather;

import java.util.Optional;


@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    boolean existsById(Long id);

    Optional<Weather> findById(Long id);

    
}

