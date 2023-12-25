package eu.hbb.newyeargame.repo;

import eu.hbb.newyeargame.entity.ThemeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends CrudRepository<ThemeEntity, Long> {

    Page<ThemeEntity> findAll(Pageable pageable);
    Optional<List<ThemeEntity>> findAllByRoundid(long id);

}