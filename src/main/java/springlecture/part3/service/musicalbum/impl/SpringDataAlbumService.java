package springlecture.part3.service.musicalbum.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springlecture.part3.constants.ErrorConstants;
import springlecture.part3.dto.ErrorDto;
import springlecture.part3.dto.MusicAlbumDto;
import springlecture.part3.dto.fromuser.EditMusicAlbumDto;
import springlecture.part3.entity.Album;
import springlecture.part3.entity.Genre;
import springlecture.part3.entity.MusicGroup;
import springlecture.part3.exception.UnprocessableEntityException;
import springlecture.part3.repository.springdata.IAlbumSpringDataRepo;
import springlecture.part3.repository.springdata.IGenreSpringDataRepo;
import springlecture.part3.repository.springdata.IMusicGroupSpringDataRepo;
import springlecture.part3.service.musicalbum.IMusicAlbumService;
import java.util.Set;
import java.util.stream.Collectors;

@Service("springDataAlbumService")
@ConditionalOnProperty(name = "db.connector", havingValue = "springData")
public class SpringDataAlbumService implements IMusicAlbumService {

    private final IMusicGroupSpringDataRepo musicGroupRepo;
    private final IGenreSpringDataRepo genreRepo;
    private final IAlbumSpringDataRepo albumRepo;

    @Autowired
    public SpringDataAlbumService(IMusicGroupSpringDataRepo musicGroupRepo,
                                  IGenreSpringDataRepo genreRepo,
                                  IAlbumSpringDataRepo albumRepo) {
        this.musicGroupRepo = musicGroupRepo;
        this.genreRepo = genreRepo;
        this.albumRepo = albumRepo;
    }

    @Override
    @Transactional
    public void addMusicAlbum(MusicAlbumDto musicAlbum) {
        MusicGroup group = getMusicGroup(musicAlbum.getMusicGroup());
        Genre genre = getGenre(musicAlbum.getGenre());
        if (albumRepo.existsByNameIgnoreCaseAndMusicGroup(musicAlbum.getName(), group)) {
            throw new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getName(), ErrorConstants.Msgs.ALREADY_EXISTS));
        }
        Album album = new Album(musicAlbum.getName(), musicAlbum.getReleaseYear(), group, genre);
        albumRepo.save(album);
    }

    @Override
    @Transactional
    public void editMusicAlbum(EditMusicAlbumDto musicAlbum) {
        MusicGroup oldMusicGroup = getMusicGroup(musicAlbum.getOldMusicGroup());
        Album editedAlbum = albumRepo.findByNameIgnoreCaseAndMusicGroup(musicAlbum.getOldName(), oldMusicGroup).orElseThrow(() ->
                new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getOldName(), ErrorConstants.Msgs.NOT_FOUND)));
        MusicGroup newMusicGroup = musicAlbum.getMusicGroup() == null ? null : getMusicGroup(musicAlbum.getMusicGroup());
        if ((musicAlbum.getName() != null || newMusicGroup != null) &&
                albumRepo.existsByNameIgnoreCaseAndMusicGroup(musicAlbum.getName(), newMusicGroup == null ? oldMusicGroup : newMusicGroup)) {
            throw new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getName(), ErrorConstants.Msgs.ALREADY_EXISTS));
        }
        editMusicAlbum(newMusicGroup, musicAlbum, editedAlbum);
    }

    @Override
    public Set<MusicAlbumDto> getMusicAlbum(Set<String> names) {
        return albumRepo.findByNameInIgnoreCase(names).stream()
                .map(entity -> new MusicAlbumDto(entity.getName(), entity.getYearRelease(), entity.getGenre().getName(), entity.getMusicGroup().getName()))
                .collect(Collectors.toSet());
    }

    private MusicGroup getMusicGroup(String name) {
        return musicGroupRepo.findByNameIgnoreCase(name).orElseThrow(() ->
                new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.MUSIC_GROUP, name, ErrorConstants.Msgs.NOT_FOUND)));
    }

    private Genre getGenre(String name) {
        return genreRepo.findByNameIgnoreCase(name).orElseThrow(() ->
                new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.GENRE, name, ErrorConstants.Msgs.NOT_FOUND)));
    }

    private void editMusicAlbum(MusicGroup newMusicGroup, EditMusicAlbumDto musicAlbum, Album editedAlbum) {
        if (newMusicGroup != null) {
            editedAlbum.setMusicGroup(newMusicGroup);
        }
        if (musicAlbum.getName() != null) {
            editedAlbum.setName(musicAlbum.getName());
        }
        if (musicAlbum.getReleaseYear() != null) {
            editedAlbum.setYearRelease(musicAlbum.getReleaseYear());
        }
        if (musicAlbum.getGenre() != null) {
            editedAlbum.setGenre(getGenre(musicAlbum.getGenre()));
        }
    }
}
