package eu.hbb.newyeargame.repo;

import eu.hbb.newyeargame.entity.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, Long> {

    Page<GameEntity> findAll(Pageable pageable);
    Optional<GameEntity> findByAuthor(String author);
    Optional<GameEntity> findByName(String name);
    Optional<GameEntity> findById(long id);


}