package springlecture.part3.repository.hibernate.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import springlecture.part3.anotation.qualifier.CriteriaQualifier;
import springlecture.part3.entity.MusicGroup;
import springlecture.part3.repository.hibernate.IMusicGroupHibernateRepo;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
@CriteriaQualifier
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateCriteria")
public class MusicGroupHibernateCriteriaRepo implements IMusicGroupHibernateRepo {

    private final String NAME_PARAMETER = "name";
    private final SessionFactory sessionFactory;

    @Autowired
    public MusicGroupHibernateCriteriaRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<MusicGroup> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MusicGroup> criteriaQuery = builder.createQuery(MusicGroup.class);
        Root<MusicGroup> root = criteriaQuery.from(MusicGroup.class);
        criteriaQuery.select(root).where(builder.equal(builder.lower(root.get(NAME_PARAMETER)), name.toLowerCase()));
        return session.createQuery(criteriaQuery).uniqueResultOptional();
    }
}
