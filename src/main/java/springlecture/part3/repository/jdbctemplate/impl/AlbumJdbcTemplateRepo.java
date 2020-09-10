package springlecture.part3.repository.jdbctemplate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import springlecture.part3.constants.SqlScripts;
import springlecture.part3.dto.MusicAlbumDto;
import springlecture.part3.repository.jdbctemplate.IAlbumJdbcTemplateRepo;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class AlbumJdbcTemplateRepo implements IAlbumJdbcTemplateRepo {

    private static final String ALBUM_NAME = "album_name";
    private static final String COMA_AND_SPACE = ", ";
    private static final String GENRE_NAME = "genre_name";
    private static final String GROUP_NAME = "group_name";
    private static final String SET_GENRE_ID = "genre_id = ?";
    private static final String SET_MUSIC_GROUP_ID = "music_group_id = ?";
    private static final String SET_NAME = "name = ?";
    private static final String SET_YEAR_RELEASE = "year_release = ?";
    private static final String QUESTION = "LOWER(?)";
    private static final String YEAR_RELEASE = "year_release";
    private static final RowMapper<MusicAlbumDto> musicAlbumRowMapper = (rs, rowNum) ->
            new MusicAlbumDto(rs.getString(ALBUM_NAME), rs.getShort(YEAR_RELEASE), rs.getString(GENRE_NAME), rs.getString(GROUP_NAME));
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AlbumJdbcTemplateRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(String name, short yearRelease, long musicGroupId, long genreId) {
        int updatedRows = jdbcTemplate.update(SqlScripts.INSERT_ALBUM, name, yearRelease, musicGroupId, genreId);
        return updatedRows > 0;
    }
    @Override
    public boolean existsByNameAndGroupId(String name, long groupId) {
        return jdbcTemplate.queryForObject(SqlScripts.SELECT_EXISTS_ALBUM_AND_MUSIC_GROUP_ID, new Object[]{name, groupId}, Boolean.class);
    }
    @Override
    public Set<MusicAlbumDto> findByNames(Set<String> names) {
        String script = getSelectScript(names.size());
        return new HashSet<>(jdbcTemplate.query(script, names.toArray(), musicAlbumRowMapper));
    }

    private String getSelectScript(int size) {
        StringBuilder condition = new StringBuilder(QUESTION);
        for (int i = 1; i < size; i++) {
            condition.append(COMA_AND_SPACE).append(QUESTION);
        }
        return String.format(SqlScripts.SELECT_ALBUMS_TEMPLATE, condition);
    }

    @Override
    public boolean update(String oldName, long newMusicGroupId, String name, Short releaseYear, Long genreId, Long musicGroupId) {
        List<Object> requestParams = new ArrayList<>();
        List<Integer> requestTypes = new ArrayList<>();
        StringBuilder setPart = new StringBuilder();
        addParameterToRequestIfNotNull(name, Types.VARCHAR, requestParams, requestTypes, setPart, SET_NAME);
        addParameterToRequestIfNotNull(releaseYear, Types.INTEGER, requestParams, requestTypes, setPart, SET_YEAR_RELEASE);
        addParameterToRequestIfNotNull(genreId, Types.BIGINT, requestParams, requestTypes, setPart, SET_GENRE_ID);
        addParameterToRequestIfNotNull(musicGroupId, Types.BIGINT, requestParams, requestTypes, setPart, SET_MUSIC_GROUP_ID);
        requestParams.add(oldName);
        requestParams.add(newMusicGroupId);
        requestTypes.add(Types.VARCHAR);
        requestTypes.add(Types.BIGINT);
        String sqlScript = String.format(SqlScripts.UPDATE_ALBUM_TEMPLATE, setPart);
        int updatedRows = jdbcTemplate.update(sqlScript, requestParams.toArray(), requestTypes.stream().mapToInt(i -> i).toArray());
        return updatedRows > 0;
    }
    private void addParameterToRequestIfNotNull(Object parameter, Integer parameterType, List<Object> requestParams,
                                                List<Integer> requestTypes, StringBuilder setPart, String setParameter) {
        if (parameter == null) {
            return;
        }
        requestParams.add(parameter);
        requestTypes.add(parameterType);
        if (setPart.length() > 0) {
            setPart.append(COMA_AND_SPACE);
        }
        setPart.append(setParameter);
    }
}
