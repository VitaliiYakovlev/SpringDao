package springlecture.part3.controller;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springlecture.part3.constants.ErrorConstants.Fields;
import springlecture.part3.dto.ErrorDto;
import springlecture.part3.dto.MusicAlbumDto;
import springlecture.part3.dto.fromuser.EditMusicAlbumDto;
import springlecture.part3.exception.InvalidInputDataException;
import springlecture.part3.service.musicalbum.IMusicAlbumService;
import javax.validation.Valid;
import java.util.Set;
import static springlecture.part3.constants.ErrorConstants.*;

@RestController
@RequestMapping("/dal")
public class MusicController {

    private final IMusicAlbumService musicAlbumService;

    public MusicController(IMusicAlbumService musicAlbumService) {
        this.musicAlbumService = musicAlbumService;
    }

    @PostMapping(path = "/add-new-album")
    public void createMusicAlbum(@Valid @RequestBody MusicAlbumDto musicAlbum, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidInputDataException(bindingResult);
        }
        musicAlbumService.addMusicAlbum(musicAlbum);
    }

    @PostMapping(path = "/edit-music-album", produces = MediaType.APPLICATION_JSON_VALUE)
    public void editMusicAlbum(@Valid @RequestBody EditMusicAlbumDto musicAlbum, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidInputDataException(bindingResult);
        }
        musicAlbumService.editMusicAlbum(musicAlbum);
    }

    @GetMapping(path = "/get-music-album", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<MusicAlbumDto> getMusicAlbum(@RequestParam(name = "name") Set<String> musicAlbumNames) {
        if (musicAlbumNames.stream().anyMatch(StringUtils::isEmpty)) {
            throw new InvalidInputDataException(new ErrorDto(Fields.REQUEST_PARAMS, null, Msgs.CONTAINS_NULL_OR_EMPTY));
        }
        return musicAlbumService.getMusicAlbum(musicAlbumNames);
    }
}
