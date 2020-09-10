package springlecture.part3.repository.jdbctemplate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import springlecture.part3.constants.SqlScripts;
import springlecture.part3.repository.jdbctemplate.IMusicGroupJdbcTemplateRepo;
import java.util.Optional;

@Repository
public class MusicGroupJdbcTemplateRepo implements IMusicGroupJdbcTemplateRepo {

    private final JdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<Optional<Long>> resultSetExtractor =
            rs -> rs.next() ? Optional.of(rs.getLong(1)) : Optional.empty();

    @Autowired
    public MusicGroupJdbcTemplateRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Long> findIdByName(String name) {
        return jdbcTemplate.query(SqlScripts.SELECT_MUSIC_GROUP_BY_NAME, new Object[]{name}, resultSetExtractor);
    }
}