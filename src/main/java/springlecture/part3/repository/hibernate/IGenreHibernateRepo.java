package springlecture.part3.repository.hibernate;

import springlecture.part3.entity.Genre;
import java.util.Optional;

public interface IGenreHibernateRepo {
    Optional<Genre> findByName(String name);
}
