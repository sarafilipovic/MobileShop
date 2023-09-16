package mobile.repositories;

import mobile.model.Kosarica;
import mobile.model.Mobile;
import mobile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KosaricaRepository extends JpaRepository  <Kosarica, Long>{
    Kosarica findByCreatedByAndMobile(User createdBy, Mobile mobile);
    List<Kosarica> findByCreatedBy(User createdBy);
}
