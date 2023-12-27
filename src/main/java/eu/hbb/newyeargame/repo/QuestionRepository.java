package eu.hbb.newyeargame.repo;

import eu.hbb.newyeargame.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<QuestionEntity, Long> {

    Page<QuestionEntity> findAll(Pageable pageable);
    Optional<List<QuestionEntity>> findAllByThemeidOrderById(long id);

}
