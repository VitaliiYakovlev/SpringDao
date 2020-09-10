package springlecture.part3.repository.springdata;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import springlecture.part3.entity.Genre;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "db.connector", havingValue = "springData")
public interface IGenreSpringDataRepo extends CrudRepository<Genre, Long> {
    Optional<Genre> findByNameIgnoreCase(String name);
}
