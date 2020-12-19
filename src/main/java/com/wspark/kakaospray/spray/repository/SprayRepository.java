package com.wspark.kakaospray.spray.repository;

import com.wspark.kakaospray.spray.model.Spray;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SprayRepository extends JpaRepository<Spray, Long> {

  Optional<Spray> findByToken(String token);

  @Query(value = "select s " +
      "from Spray s " +
      "join fetch s.sprayItems " +
      "where s.token = :token")
  Optional<Spray> findByTokenWithSprayItems(String token);

}
