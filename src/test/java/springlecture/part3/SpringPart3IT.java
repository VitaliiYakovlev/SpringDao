package springlecture.part3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springlecture.part3.dto.fromuser.EditMusicAlbumDto;
import springlecture.part3.entity.Album;
import springlecture.part3.entity.Genre;
import springlecture.part3.entity.MusicGroup;
import springlecture.part3.repository.springdata.IAlbumSpringDataRepo;
import springlecture.part3.repository.springdata.IGenreSpringDataRepo;
import springlecture.part3.repository.springdata.IMusicGroupSpringDataRepo;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringPart3IT {

    private static final Genre GENRE_1 = new Genre(1L, "nu metal");
    private static final Genre GENRE_2 = new Genre(2L, "alternative rock");
    private static final Genre GENRE_3 = new Genre(3L, "progressive metal");
    private static final MusicGroup MUSIC_GROUP_1 = new MusicGroup(4L, "Slipknot", (short) 1992, null);
    private static final MusicGroup MUSIC_GROUP_2 = new MusicGroup(5L, "Papa Roach", (short) 1993, null);
    private static final MusicGroup MUSIC_GROUP_3 = new MusicGroup(6L, "Animals As Leaders", (short) 2007, null);
    private static final Album ALBUM_1 = new Album(7L, "Iowa", (short) 2001, MUSIC_GROUP_1, GENRE_1);
    private static final Album ALBUM_2 = new Album(8L, "Getting Away with Murder", (short) 2004, MUSIC_GROUP_2, GENRE_2);
    private static final Album ALBUM_3 = new Album(9L, "Weightless", (short) 2011, MUSIC_GROUP_3, GENRE_3);

    private static final List<Genre> genres = Arrays.asList(GENRE_1, GENRE_2, GENRE_3);
    private static final List<MusicGroup> musicGroups = Arrays.asList(MUSIC_GROUP_1, MUSIC_GROUP_2, MUSIC_GROUP_3);
    private static final List<Album> albums = Arrays.asList(ALBUM_1, ALBUM_2, ALBUM_3);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IAlbumSpringDataRepo albumRepo;

    @Autowired
    private IGenreSpringDataRepo genreRepo;

    @Autowired
    private IMusicGroupSpringDataRepo musicGroupRepo;

    @BeforeEach
    void setUp() {
        genres.forEach(genre -> genreRepo.save(genre));
        musicGroups.forEach(musicGroup -> musicGroupRepo.save(musicGroup));
        albums.forEach(album -> albumRepo.save(album));
    }

    @Test
    @Transactional
    public void test() throws Exception {
        EditMusicAlbumDto editMusicAlbumDto = new EditMusicAlbumDto(ALBUM_1.getName(), MUSIC_GROUP_1.getName(),
                "Iowa Changed", (short) 2002, GENRE_3.getName(), null);
        Album exp = new Album(ALBUM_1.getId(), editMusicAlbumDto.getName(), editMusicAlbumDto.getReleaseYear(), MUSIC_GROUP_1, GENRE_3);

        mockMvc.perform(post("/dal/edit-music-album")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editMusicAlbumDto)))
                .andExpect(status().isOk());

        Optional<Album> act = albumRepo.findById(ALBUM_1.getId());

        assertTrue(act.isPresent());
        assertEquals(exp, act.get());
    }
}
