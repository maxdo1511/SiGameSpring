package eu.hbb.newyeargame.repo;

import eu.hbb.newyeargame.entity.RoundEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends CrudRepository<RoundEntity, Long> {

    Page<RoundEntity> findAll(Pageable pageable);
    Optional<List<RoundEntity>> findAllByGameidOrderById(long id);

}
