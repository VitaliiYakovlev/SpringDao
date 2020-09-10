package springlecture.part3.service.musicalbum.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import springlecture.part3.constants.SqlScripts;
import springlecture.part3.dto.ErrorDto;
import springlecture.part3.dto.MusicAlbumDto;
import springlecture.part3.dto.fromuser.EditMusicAlbumDto;
import springlecture.part3.exception.InvalidInputDataException;
import springlecture.part3.exception.UnprocessableEntityException;
import springlecture.part3.service.musicalbum.IMusicAlbumService;
import springlecture.part3.constants.ErrorConstants;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service("jdbcAlbumService")
public class JdbcAlbumService implements IMusicAlbumService {

    private static final String ALBUM_NAME = "album_name";
    private static final String COMA_AND_SPACE = ", ";
    private static final String GENRE_NAME = "genre_name";
    private static final String GROUP_NAME = "group_name";
    private static final String SET_NAME = "name = ?";
    private static final String SET_YEAR_RELEASE = "year_release = ?";
    private static final String SET_MUSIC_GROUP = "music_group_id = ?";
    private static final String SET_GENRE = "genre_id = ?";
    private static final String QUESTION = "LOWER(?)";
    private static final String YEAR_RELEASE = "year_release";

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void addMusicAlbum(MusicAlbumDto musicAlbum) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SqlScripts.INSERT_ALBUM)) {
            long musicGroupId = getId(musicAlbum.getMusicGroup(), SqlScripts.SELECT_MUSIC_GROUP_BY_NAME, connection).orElseThrow(() ->
                    new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.MUSIC_GROUP, musicAlbum.getMusicGroup(), ErrorConstants.Msgs.NOT_FOUND)));
            long genreId = getId(musicAlbum.getGenre(), SqlScripts.SELECT_GENRE, connection).orElseThrow(() ->
                    new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.GENRE, musicAlbum.getGenre(), ErrorConstants.Msgs.NOT_FOUND)));
            editPrepareStatementForCreate(ps, musicAlbum.getName(), musicAlbum.getReleaseYear(), musicGroupId, genreId);
            executeUpdateAndValidateResultForAlbum(ps, musicAlbum.getName(), ErrorConstants.Msgs.ALREADY_EXISTS);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editMusicAlbum(EditMusicAlbumDto musicAlbum) {
        String updateScript = getUpdateScript(musicAlbum);
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(updateScript)) {
            long musicGroupId = getId(musicAlbum.getOldMusicGroup(), SqlScripts.SELECT_MUSIC_GROUP_BY_NAME, connection).orElseThrow(() ->
                    new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.MUSIC_GROUP, musicAlbum.getOldMusicGroup(), ErrorConstants.Msgs.NOT_FOUND)));
            editPreparedStatementForUpdate(ps, connection, musicAlbum, musicGroupId);
            validateAlbumConstraint(musicAlbum, musicGroupId, connection);
            executeUpdateAndValidateResultForAlbum(ps, musicAlbum.getOldName(), ErrorConstants.Msgs.NOT_FOUND);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<MusicAlbumDto> getMusicAlbum(Set<String> names) {
        String selectScript = getSelectAlbumScript(names.size());
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(selectScript)) {
            editPrepareStatementForSelect(ps, names);
            ResultSet rs = ps.executeQuery();
            return parseResultSetToMusicAlbumDtoSer(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }

    private void editPrepareStatementForCreate(PreparedStatement ps, String name, int releaseYear, long musicGroupId, long genreId) throws SQLException {
        ps.setString(1, name);
        ps.setInt(2, releaseYear);
        ps.setLong(3, musicGroupId);
        ps.setLong(4, genreId);
    }

    private String getUpdateScript(EditMusicAlbumDto musicAlbum) {
        StringBuilder setPart = new StringBuilder();
        if (!StringUtils.isEmpty(musicAlbum.getName())) {
            setPart.append(SET_NAME);
        }
        if (musicAlbum.getReleaseYear() != null) {
            addComaAndSpaceIfRequired(setPart);
            setPart.append(SET_YEAR_RELEASE);
        }
        if (!StringUtils.isEmpty(musicAlbum.getGenre())) {
            addComaAndSpaceIfRequired(setPart);
            setPart.append(SET_GENRE);
        }
        if (!StringUtils.isEmpty(musicAlbum.getMusicGroup())) {
            addComaAndSpaceIfRequired(setPart);
            setPart.append(SET_MUSIC_GROUP);
        }
        if (setPart.length() == 0) {
            throw new InvalidInputDataException(new ErrorDto(ErrorConstants.Fields.ALL_PARAMETERS, null, ErrorConstants.Msgs.NULL_OR_EMPTY));
        }
        return String.format(SqlScripts.UPDATE_ALBUM_TEMPLATE, setPart);
    }

    private void validateAlbumConstraint(EditMusicAlbumDto musicAlbum, long musicGroupId, Connection connection) {
        if (StringUtils.isEmpty(musicAlbum.getName())) {
            return;
        }
        String albumName = StringUtils.isEmpty(musicAlbum.getName()) ? musicAlbum.getOldName() : musicAlbum.getName();
        try (PreparedStatement ps = connection.prepareStatement(SqlScripts.SELECT_EXISTS_ALBUM_AND_MUSIC_GROUP_ID)) {
            ps.setString(1, albumName);
            ps.setLong(2, musicGroupId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getBoolean(1)) {
                throw new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getName(), ErrorConstants.Msgs.ALREADY_EXISTS));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getSelectAlbumScript(int size) {
        StringBuilder condition = new StringBuilder(QUESTION);
        for (int i = 1; i < size; i++) {
            condition.append(COMA_AND_SPACE).append(QUESTION);
        }
        return String.format(SqlScripts.SELECT_ALBUMS_TEMPLATE, condition);
    }

    private void editPreparedStatementForUpdate(PreparedStatement ps, Connection connection, EditMusicAlbumDto musicAlbum,
                                                long oldMusicGroupId) throws SQLException {
        int index = 1;
        if (!StringUtils.isEmpty(musicAlbum.getName())) {
            ps.setString(index++, musicAlbum.getName());
        }
        if (musicAlbum.getReleaseYear() != null) {
            ps.setInt(index++, musicAlbum.getReleaseYear());
        }
        if (!StringUtils.isEmpty(musicAlbum.getGenre())) {
            long genreId = getId(musicAlbum.getGenre(), SqlScripts.SELECT_GENRE, connection).orElseThrow(() ->
                    new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.GENRE, musicAlbum.getGenre(), ErrorConstants.Msgs.NOT_FOUND)));
            ps.setLong(index++, genreId);
        }
        if (!StringUtils.isEmpty(musicAlbum.getMusicGroup())) {
            long musicGroupId = getId(musicAlbum.getMusicGroup(), SqlScripts.SELECT_MUSIC_GROUP_BY_NAME, connection).orElseThrow(() ->
                    new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.MUSIC_GROUP, musicAlbum.getMusicGroup(), ErrorConstants.Msgs.NOT_FOUND)));
            ps.setLong(index++, musicGroupId);
        }
        ps.setString(index++, musicAlbum.getOldName());
        ps.setLong(index, oldMusicGroupId);
    }

    private void editPrepareStatementForSelect(PreparedStatement ps, Set<String> names) throws SQLException {
        int index = 0;
        for (String name : names) {
            ps.setString(++index, name);
        }
    }

    private Set<MusicAlbumDto> parseResultSetToMusicAlbumDtoSer(ResultSet rs) throws SQLException {
        Set<MusicAlbumDto> result = new HashSet<>();
        while (rs.next()) {
            result.add(new MusicAlbumDto(
                    rs.getString(ALBUM_NAME),
                    rs.getShort(YEAR_RELEASE),
                    rs.getString(GENRE_NAME),
                    rs.getString(GROUP_NAME)
            ));
        }
        return result;
    }

    private void executeUpdateAndValidateResultForAlbum(PreparedStatement ps, String albumName, String message) throws SQLException {
        long result = ps.executeUpdate();
        if (result == 0) {
            throw new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, albumName, message));
        }
    }
    private Optional<Long> getId(String name, String script, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(script)) {
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            long result = resultSet.getLong(1);
            return result <= 0 ? Optional.empty() : Optional.of(result);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void addComaAndSpaceIfRequired(StringBuilder setPart) {
        if (setPart.length() > 0) {
            setPart.append(COMA_AND_SPACE);
        }
    }
}
