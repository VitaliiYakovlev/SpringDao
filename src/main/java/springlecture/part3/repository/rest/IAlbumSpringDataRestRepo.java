package springlecture.part3.repository.rest;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import springlecture.part3.entity.Album;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "Album", path = "albums")
public interface IAlbumSpringDataRestRepo extends PagingAndSortingRepository<Album, Long> {

    List<Album> findByNameIgnoreCase(@Param("name") String name);
}
