package weatherfit.weatherfit_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import weatherfit.weatherfit_back.entity.Weather;

import java.util.Optional;
import java.util.List;


@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    boolean existsById(Long id);

    Optional<Weather> findById(Long id);
    
    @Query("SELECT w FROM Weather w ORDER BY w.weatherDate DESC, w.weatherTime DESC")
    List<Weather> findAllOrderByDateDesc();
    
    default Optional<Weather> findLatestWeather() {
        List<Weather> weathers = findAllOrderByDateDesc();
        return weathers.isEmpty() ? Optional.empty() : Optional.of(weathers.get(0));
    }



}

