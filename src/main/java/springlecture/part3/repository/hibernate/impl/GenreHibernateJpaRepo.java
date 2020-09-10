package springlecture.part3.repository.hibernate.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import springlecture.part3.anotation.qualifier.JpaQualifier;
import springlecture.part3.constants.JpqlScripts;
import springlecture.part3.entity.Genre;
import springlecture.part3.repository.hibernate.IGenreHibernateRepo;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
@JpaQualifier
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateJpa")
public class GenreHibernateJpaRepo implements IGenreHibernateRepo {

    private final String NAME_PARAMETER = "name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Genre> findByName(String name) {
        TypedQuery<Genre> query = entityManager.createQuery(JpqlScripts.SELECT_GENRE_BY_NAME, Genre.class);
        query.setParameter(NAME_PARAMETER, name);
        return query.getResultStream().findAny();
    }
}
