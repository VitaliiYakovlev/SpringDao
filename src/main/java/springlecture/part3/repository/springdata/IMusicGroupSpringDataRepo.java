package springlecture.part3.repository.springdata;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import springlecture.part3.entity.MusicGroup;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "db.connector", havingValue = "springData")
public interface IMusicGroupSpringDataRepo extends CrudRepository<MusicGroup, Long> {
    Optional<MusicGroup> findByNameIgnoreCase(String name);
}
