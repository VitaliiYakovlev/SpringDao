package springlecture.part3.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class MusicAlbumDto {
    @Size(min = 2, max = 255, message = "length of album name must be in a range from 2 to 255")
    @NotNull(message = "must not be null")
    String name;

    @Min(value = 1860, message = "must be no less than 1860")
    short releaseYear;

    @Size(min = 2, max = 127, message = "length of genre name must be in a range from 2 to 127")
    String genre;

    @Size(min = 2, max = 127, message = "length of music group name must be in a range from 2 to 127")
    String musicGroup;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MusicAlbumDto(@JsonProperty("name") String name,
                         @JsonProperty("releaseYear") short releaseYear,
                         @JsonProperty("genre") String genre,
                         @JsonProperty("musicGroup") String musicGroup) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.musicGroup = musicGroup;
    }
}
