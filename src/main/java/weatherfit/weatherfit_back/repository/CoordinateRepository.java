package weatherfit.weatherfit_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weatherfit.weatherfit_back.entity.Coordinate;

import java.util.Optional;

@Repository

public interface CoordinateRepository extends JpaRepository<Coordinate, Long>  {
    boolean existsById(Long id);


    Optional<Coordinate> findById(Long id);
     

}
