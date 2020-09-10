package springlecture.part3.service.musicalbum.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import springlecture.part3.constants.ErrorConstants;
import springlecture.part3.dto.ErrorDto;
import springlecture.part3.dto.MusicAlbumDto;
import springlecture.part3.dto.fromuser.EditMusicAlbumDto;
import springlecture.part3.entity.Album;
import springlecture.part3.entity.Genre;
import springlecture.part3.entity.MusicGroup;
import springlecture.part3.exception.UnprocessableEntityException;
import springlecture.part3.repository.hibernate.IAlbumHibernateRepo;
import springlecture.part3.repository.hibernate.IGenreHibernateRepo;
import springlecture.part3.repository.hibernate.IMusicGroupHibernateRepo;
import springlecture.part3.service.musicalbum.IMusicAlbumService;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class HibernateAlbumBase implements IMusicAlbumService {

    private final IMusicGroupHibernateRepo musicGroupRepo;
    private final IGenreHibernateRepo genreRepo;
    private final IAlbumHibernateRepo albumRepo;

    public HibernateAlbumBase(IMusicGroupHibernateRepo musicGroupRepo,
                                 IGenreHibernateRepo genreRepo,
                                 IAlbumHibernateRepo albumRepo) {
        this.musicGroupRepo = musicGroupRepo;
        this.genreRepo = genreRepo;
        this.albumRepo = albumRepo;
    }

    @Override
    @Transactional
    public void addMusicAlbum(MusicAlbumDto musicAlbum) {
        MusicGroup group = getMusicGroup(musicAlbum.getMusicGroup());
        Genre genre = getGenre(musicAlbum.getGenre());
        if (albumRepo.existsByNameAndGroup(musicAlbum.getName(), group)) {
            throw new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getName(), ErrorConstants.Msgs.ALREADY_EXISTS));
        }
        albumRepo.save(musicAlbum.getName(), musicAlbum.getReleaseYear(), group, genre);
    }


    @Override
    @Transactional(readOnly = true)
    public Set<MusicAlbumDto> getMusicAlbum(Set<String> names) {
        return albumRepo.findByNames(names.stream().map(String::toLowerCase).collect(Collectors.toSet()))
                .stream()
                .map(entity -> new MusicAlbumDto(entity.getName(), entity.getYearRelease(), entity.getGenre().getName(), entity.getMusicGroup().getName()))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void editMusicAlbum(EditMusicAlbumDto musicAlbum) {
        MusicGroup oldMusicGroup = getMusicGroup(musicAlbum.getOldMusicGroup());
        Album editedAlbum = albumRepo.findByNameAndMusicGroup(musicAlbum.getOldName(), oldMusicGroup).orElseThrow(() ->
                new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getOldName(), ErrorConstants.Msgs.NOT_FOUND)));
        MusicGroup newMusicGroup = musicAlbum.getMusicGroup() == null ? null : getMusicGroup(musicAlbum.getMusicGroup());
        if ((musicAlbum.getName() != null || newMusicGroup != null) &&
                albumRepo.existsByNameAndGroup(
                        StringUtils.isEmpty(musicAlbum.getName()) ? editedAlbum.getName() : musicAlbum.getName(),
                        newMusicGroup == null ? oldMusicGroup : newMusicGroup)) {
            throw new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.ALBUM, musicAlbum.getName(), ErrorConstants.Msgs.ALREADY_EXISTS));
        }
        editMusicAlbum(newMusicGroup, musicAlbum, editedAlbum);
    }

    private MusicGroup getMusicGroup(String name) {
        return musicGroupRepo.findByName(name).orElseThrow(() ->
                new UnprocessableEntityException(new ErrorDto(ErrorConstants.Fields.MUSIC_GROUP, name, ErrorConstants.Msgs.NOT_FOUND)));
    }

    private Genre getGenre(String name) {
        return genreRepo.findByName(name).orElseThrow(() ->
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
