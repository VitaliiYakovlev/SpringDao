package springlecture.part3.repository.hibernate;

import springlecture.part3.entity.Album;
import springlecture.part3.entity.Genre;
import springlecture.part3.entity.MusicGroup;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IAlbumHibernateRepo {

    void save(String name, short yearRelease, MusicGroup musicGroup, Genre genre);
    boolean existsByNameAndGroup(String name, MusicGroup group);
    Optional<Album> findByNameAndMusicGroup(String name, MusicGroup musicGroup);
    List<Album> findByNames(Set<String> names);
}
