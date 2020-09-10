package springlecture.part3.repository.jdbctemplate;

import springlecture.part3.dto.MusicAlbumDto;
import java.util.Set;

public interface IAlbumJdbcTemplateRepo {

    boolean save(String name, short yearRelease, long musicGroupId, long genreId);
    boolean update(String oldName, long oldMusicGroupId, String name, Short releaseYear, Long genreId, Long musicGroupId);
    boolean existsByNameAndGroupId(String name, long groupId);
    Set<MusicAlbumDto> findByNames(Set<String> names);
}
