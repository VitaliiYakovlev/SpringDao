package springlecture.part3.repository.hibernate.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import springlecture.part3.anotation.qualifier.CriteriaQualifier;
import springlecture.part3.entity.Album;
import springlecture.part3.entity.Genre;
import springlecture.part3.entity.MusicGroup;
import springlecture.part3.repository.hibernate.IAlbumHibernateRepo;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@CriteriaQualifier
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateCriteria")
public class AlbumHibernateCriteriaRepo implements IAlbumHibernateRepo {

    private final String MUSIC_GROUP_PARAMETER = "musicGroup";
    private final String NAME_PARAMETER = "name";
    private final SessionFactory sessionFactory;

    @Autowired
    public AlbumHibernateCriteriaRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean existsByNameAndGroup(String name, MusicGroup musicGroup) {
        return findByNameAndMusicGroup(name, musicGroup).isPresent();
    }

    @Override
    public Optional<Album> findByNameAndMusicGroup(String name, MusicGroup musicGroup) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Album> criteriaQuery = builder.createQuery(Album.class);
        Root<Album> root = criteriaQuery.from(Album.class);
        Predicate selectByName = builder.equal(builder.lower(root.get(NAME_PARAMETER)), name.toLowerCase());
        Predicate selectByGroup = builder.equal(root.get(MUSIC_GROUP_PARAMETER), musicGroup);
        criteriaQuery.select(root).where(builder.and(selectByName, selectByGroup));
        return session.createQuery(criteriaQuery).uniqueResultOptional();
    }

    @Override
    public void save(String name, short yearRelease, MusicGroup musicGroup, Genre genre) {
        Album album = new Album(name, yearRelease, musicGroup, genre);
        Session session = sessionFactory.getCurrentSession();
        session.save(album);
    }

    @Override
    public List<Album> findByNames(Set<String> names) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Album> criteriaQuery = builder.createQuery(Album.class);
        Root<Album> root = criteriaQuery.from(Album.class);
        criteriaQuery.select(root).where(builder.lower(root.get(NAME_PARAMETER)).in(names));
        return session.createQuery(criteriaQuery).list();
    }
}
