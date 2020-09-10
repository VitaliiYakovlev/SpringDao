package springlecture.part3.repository.hibernate.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import springlecture.part3.anotation.qualifier.HqlQualifier;
import springlecture.part3.constants.HqlScripts;
import springlecture.part3.entity.MusicGroup;
import springlecture.part3.repository.hibernate.IMusicGroupHibernateRepo;
import java.util.Optional;

@Repository
@HqlQualifier
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateHql")
public class MusicGroupHibernateHqlRepo implements IMusicGroupHibernateRepo {

    private final String NAME_PARAMETER = "name";
    private final SessionFactory sessionFactory;

    @Autowired
    public MusicGroupHibernateHqlRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<MusicGroup> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query<MusicGroup> musicGroupQuery = session.createQuery(HqlScripts.SELECT_MUSIC_GROUP_BY_NAME, MusicGroup.class);
        musicGroupQuery.setParameter(NAME_PARAMETER, name);
        return musicGroupQuery.uniqueResultOptional();
    }
}
