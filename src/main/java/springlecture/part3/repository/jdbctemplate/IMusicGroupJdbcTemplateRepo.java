package springlecture.part3.repository.jdbctemplate;

import java.util.Optional;

public interface IMusicGroupJdbcTemplateRepo {
    Optional<Long> findIdByName(String name);
}
