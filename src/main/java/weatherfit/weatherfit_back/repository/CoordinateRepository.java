package weatherfit.weatherfit_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import weatherfit.weatherfit_back.entity.Coordinate;

import java.util.List;
import java.util.Optional;

@Repository

public interface CoordinateRepository extends JpaRepository<Coordinate, Long>  {
    boolean existsById(Long id);


    Optional<Coordinate> findById(Long id);
     
    List<Coordinate> findByWeatherCondition(String weatherCondition);
    
    @Query("SELECT c FROM Coordinate c JOIN c.likes l WHERE c.weatherCondition = :weatherCondition AND l.user.email = :email")
    List<Coordinate> findByWeatherConditionAndLikesUserId(@Param("weatherCondition") String weatherCondition, @Param("email") String email);

    @Query("SELECT c FROM Coordinate c JOIN c.likes l WHERE l.user.email = :email")
    List<Coordinate> findByLikesUserId(@Param("email") String email);
}
