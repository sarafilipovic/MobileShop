package mobile.repositories;

import mobile.model.Mobile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobileRepository extends JpaRepository <Mobile, Long> {}