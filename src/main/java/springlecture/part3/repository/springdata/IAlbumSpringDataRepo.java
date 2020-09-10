package springlecture.part3.repository.springdata;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import springlecture.part3.entity.Album;
import springlecture.part3.entity.MusicGroup;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@ConditionalOnProperty(name = "db.connector", havingValue = "springData")
public interface IAlbumSpringDataRepo extends CrudRepository<Album, Long> {
    boolean existsByNameIgnoreCaseAndMusicGroup(String name, MusicGroup musicGroup);
    Optional<Album> findByNameIgnoreCaseAndMusicGroup(String name, MusicGroup musicGroup);
    List<Album> findByNameInIgnoreCase(Set<String> name);
}
