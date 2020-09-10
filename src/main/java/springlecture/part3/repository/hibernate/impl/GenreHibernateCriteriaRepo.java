package springlecture.part3.repository.hibernate.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import springlecture.part3.anotation.qualifier.CriteriaQualifier;
import springlecture.part3.entity.Genre;
import springlecture.part3.repository.hibernate.IGenreHibernateRepo;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
@CriteriaQualifier
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateCriteria")
public class GenreHibernateCriteriaRepo implements IGenreHibernateRepo {

    private final String NAME_PARAMETER = "name";

    private final SessionFactory sessionFactory;

    @Autowired
    public GenreHibernateCriteriaRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Genre> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Genre> criteriaQuery = builder.createQuery(Genre.class);
        Root<Genre> root = criteriaQuery.from(Genre.class);
        criteriaQuery.select(root).where(builder.equal(builder.lower(root.get(NAME_PARAMETER)), name.toLowerCase()));
        return session.createQuery(criteriaQuery).uniqueResultOptional();
    }
}
