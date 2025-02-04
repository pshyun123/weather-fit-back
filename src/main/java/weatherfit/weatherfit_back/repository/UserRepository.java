package weatherfit.weatherfit_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import weatherfit.weatherfit_back.entity.User;

import java.util.Optional;

@Repository
// 사용자 Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    
}


// UserService의의 updateUser와 deleteUser 메서드에서 사용하는 save()와 delete() 메서드는 
// 이미 JpaRepository에서 제공되고 있어서 추가 구현이 필요하지 않습니다.