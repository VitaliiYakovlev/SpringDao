package springlecture.part3.repository.hibernate.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import springlecture.part3.anotation.qualifier.HqlQualifier;
import springlecture.part3.constants.HqlScripts;
import springlecture.part3.entity.Genre;
import springlecture.part3.repository.hibernate.IGenreHibernateRepo;
import java.util.Optional;

@Repository
@HqlQualifier
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateHql")
public class GenreHibernateHqlRepo implements IGenreHibernateRepo {

    private final String NAME_PARAMETER = "name";
    private final SessionFactory sessionFactory;

    @Autowired
    public GenreHibernateHqlRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Genre> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query<Genre> musicGroupQuery = session.createQuery(HqlScripts.SELECT_GENRE_BY_NAME, Genre.class);
        musicGroupQuery.setParameter(NAME_PARAMETER, name);
        return musicGroupQuery.uniqueResultOptional();
    }
}
