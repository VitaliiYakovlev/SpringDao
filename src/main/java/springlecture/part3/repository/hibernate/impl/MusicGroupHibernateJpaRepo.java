package springlecture.part3.repository.hibernate.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import springlecture.part3.anotation.qualifier.JpaQualifier;
import springlecture.part3.constants.JpqlScripts;
import springlecture.part3.entity.MusicGroup;
import springlecture.part3.repository.hibernate.IMusicGroupHibernateRepo;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
@JpaQualifier
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateJpa")
public class MusicGroupHibernateJpaRepo implements IMusicGroupHibernateRepo {

    private final String NAME_PARAMETER = "name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<MusicGroup> findByName(String name) {
        TypedQuery<MusicGroup> query = entityManager.createQuery(JpqlScripts.SELECT_MUSIC_GROUP_BY_NAME, MusicGroup.class);
        query.setParameter(NAME_PARAMETER, name);
        return query.getResultStream().findAny();
    }
}
