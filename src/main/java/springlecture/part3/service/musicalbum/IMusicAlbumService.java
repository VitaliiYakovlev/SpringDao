package springlecture.part3.service.musicalbum;

import springlecture.part3.dto.MusicAlbumDto;
import springlecture.part3.dto.fromuser.EditMusicAlbumDto;
import java.util.Set;

public interface IMusicAlbumService {
    void addMusicAlbum(MusicAlbumDto musicAlbum);
    void editMusicAlbum(EditMusicAlbumDto musicAlbum);
    Set<MusicAlbumDto> getMusicAlbum(Set<String> names);
}
