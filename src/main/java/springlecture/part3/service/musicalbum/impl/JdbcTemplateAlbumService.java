package springlecture.part3.service.musicalbum.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springlecture.part3.dto.ErrorDto;
import springlecture.part3.dto.MusicAlbumDto;
import springlecture.part3.dto.fromuser.EditMusicAlbumDto;
import springlecture.part3.exception.UnprocessableEntityException;
import springlecture.part3.repository.jdbctemplate.IAlbumJdbcTemplateRepo;
import springlecture.part3.repository.jdbctemplate.IGenreJdbcTemplateRepo;
import springlecture.part3.repository.jdbctemplate.IMusicGroupJdbcTemplateRepo;
import springlecture.part3.service.musicalbum.IMusicAlbumService;
import springlecture.part3.constants.ErrorConstants;
import java.util.Set;

@Service("jdbcTemplateAlbumService")
public class JdbcTemplateAlbumService implements IMusicAlbumService {

    private final IMusicGroupJdbcTemplateRepo musicGroupRepo;
    private final IGenreJdbcTemplateRepo genreRepo;
    private final IAlbumJdbcTemplateRepo albumRepo;

    @Autowired
    public JdbcTemplateAlbumService(IMusicGroupJdbcTemplateRepo musicGroupRepo,
                                    IGenreJdbcTemplateRepo genreRepo,
                                    IAlbumJdbcTemplateRepo albumRepo) {
        this.musicGroupRepo = musicGroupRepo;
        this.genreRepo = genreRepo;
        this.albumRepo = albumRepo;
    }

    @Override
    @Transactional
    public void addMusicAlbum(MusicAlbumDto musicAlbum) {
        long musicGroupId = musicGroupRepo.findIdByName(musicAlbum.getMusicGroup()).orElseThrow(() ->
                new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.MUSIC_GROUP, musicAlbum.getMusicGroup(), ErrorConstants.Msgs.NOT_FOUND)));
        long genreId = genreRepo.findIdByName(musicAlbum.getGenre()).orElseThrow(() ->
                new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.GENRE, musicAlbum.getGenre(), ErrorConstants.Msgs.NOT_FOUND)));
        boolean isSaved = albumRepo.save(musicAlbum.getName(), musicAlbum.getReleaseYear(), musicGroupId, genreId);
        if (!isSaved) {
            throw new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getName(), ErrorConstants.Msgs.ALREADY_EXISTS));
        }
    }

    @Override
    @Transactional
    public void editMusicAlbum(EditMusicAlbumDto musicAlbum) {
        Long newGenreId = StringUtils.isEmpty(musicAlbum.getGenre()) ? null :
                genreRepo.findIdByName(musicAlbum.getGenre()).orElseThrow(() ->
                        new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.GENRE, musicAlbum.getGenre(), ErrorConstants.Msgs.NOT_FOUND)));
        Long newMusicGroupId = StringUtils.isEmpty(musicAlbum.getMusicGroup()) ? null :
                musicGroupRepo.findIdByName(musicAlbum.getMusicGroup()).orElseThrow(() ->
                        new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.MUSIC_GROUP, musicAlbum.getMusicGroup(), ErrorConstants.Msgs.NOT_FOUND)));
        long oldMusicGroupId = musicGroupRepo.findIdByName(musicAlbum.getOldMusicGroup()).orElseThrow(() ->
                new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getOldName(), ErrorConstants.Msgs.NOT_FOUND)));
        if ((!StringUtils.isEmpty(musicAlbum.getName()) || newMusicGroupId != null) &&
                albumRepo.existsByNameAndGroupId(musicAlbum.getName(), newMusicGroupId == null ? oldMusicGroupId : newMusicGroupId)) {
            throw new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getName(), ErrorConstants.Msgs.ALREADY_EXISTS));
        }
        boolean isUpdated = albumRepo.update(musicAlbum.getOldName(), oldMusicGroupId, musicAlbum.getName(), musicAlbum.getReleaseYear(), newGenreId, newMusicGroupId);
        if (!isUpdated) {
            throw new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getOldName(), ErrorConstants.Msgs.NOT_FOUND));
        }
    }

    @Override
    public Set<MusicAlbumDto> getMusicAlbum(Set<String> names) {
        return albumRepo.findByNames(names);
    }
}
