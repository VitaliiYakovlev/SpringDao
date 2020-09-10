package springlecture.part3.repository.hibernate.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import springlecture.part3.anotation.qualifier.JpaQualifier;
import springlecture.part3.constants.JpqlScripts;
import springlecture.part3.entity.Album;
import springlecture.part3.entity.Genre;
import springlecture.part3.entity.MusicGroup;
import springlecture.part3.repository.hibernate.IAlbumHibernateRepo;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@JpaQualifier
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateJpa")
public class AlbumHibernateJpaRepo implements IAlbumHibernateRepo {

    private final String MUSIC_GROUP_PARAMETER = "musicGroup";
    private final String NAME_PARAMETER = "name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(String name, short yearRelease, MusicGroup musicGroup, Genre genre) {
        Album album = new Album(name, yearRelease, musicGroup, genre);
        entityManager.persist(album);
    }

    @Override
    public boolean existsByNameAndGroup(String name, MusicGroup group) {
        return findByNameAndMusicGroup(name, group).isPresent();
    }

    @Override
    public Optional<Album> findByNameAndMusicGroup(String name, MusicGroup musicGroup) {
        TypedQuery<Album> query = entityManager.createQuery(JpqlScripts.SELECT_ALBUM_BY_NAME_AND_MUSIC_GROUP, Album.class);
        query.setParameter(NAME_PARAMETER, name);
        query.setParameter(MUSIC_GROUP_PARAMETER, musicGroup);
        return query.getResultStream().findAny();
    }

    @Override
    public List<Album> findByNames(Set<String> names) {
        TypedQuery<Album> query = entityManager.createQuery(JpqlScripts.SELECT_ALBUM_BY_NAME, Album.class);
        query.setParameter(NAME_PARAMETER, names);
        return query.getResultList();
    }
}
