package springlecture.part3.repository.hibernate;

import springlecture.part3.entity.MusicGroup;
import java.util.Optional;

public interface IMusicGroupHibernateRepo {
    Optional<MusicGroup> findByName(String name);
}
