package springlecture.part3.repository.hibernate.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import springlecture.part3.anotation.qualifier.HqlQualifier;
import springlecture.part3.constants.HqlScripts;
import springlecture.part3.entity.Album;
import springlecture.part3.entity.Genre;
import springlecture.part3.entity.MusicGroup;
import springlecture.part3.repository.hibernate.IAlbumHibernateRepo;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@HqlQualifier
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateHql")
public class AlbumHibernateHqlRepo implements IAlbumHibernateRepo {

    private final String MUSIC_GROUP_PARAMETER = "musicGroup";
    private final String NAME_PARAMETER = "name";
    private final SessionFactory sessionFactory;

    @Autowired
    public AlbumHibernateHqlRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(String name, short yearRelease, MusicGroup musicGroup, Genre genre) {
        Album album = new Album(name, yearRelease, musicGroup, genre);
        Session session = sessionFactory.getCurrentSession();
        session.save(album);
    }

    @Override
    public boolean existsByNameAndGroup(String name, MusicGroup group) {
        Query<Album> albumQuery = getQueryForAlbumSelectionAndInitialiseParameters(name, group);
        albumQuery.setMaxResults(1);
        return albumQuery.uniqueResultOptional().isPresent();
    }

    @Override
    public Optional<Album> findByNameAndMusicGroup(String name, MusicGroup musicGroup) {
        return getQueryForAlbumSelectionAndInitialiseParameters(name, musicGroup).uniqueResultOptional();
    }

    @Override
    public List<Album> findByNames(Set<String> names) {
        Session session = sessionFactory.getCurrentSession();
        Query<Album> albumQuery = session.createQuery(HqlScripts.SELECT_ALBUM_BY_NAME, Album.class);
        albumQuery.setParameterList(NAME_PARAMETER, names);
        return albumQuery.list();
    }

    private Query<Album> getQueryForAlbumSelectionAndInitialiseParameters(String name, MusicGroup group) {
        Session session = sessionFactory.getCurrentSession();
        Query<Album> albumQuery = session.createQuery(HqlScripts.SELECT_ALBUM_BY_NAME_AND_MUSIC_GROUP, Album.class);
        albumQuery.setParameter(NAME_PARAMETER, name);
        albumQuery.setParameter(MUSIC_GROUP_PARAMETER, group);
        return albumQuery;
    }
}
