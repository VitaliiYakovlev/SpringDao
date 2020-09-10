package springlecture.part3.repository.jdbctemplate;

import java.util.Optional;

public interface IGenreJdbcTemplateRepo {
    Optional<Long> findIdByName(String name);
}
