package springlecture.part3.dto.fromuser;

import lombok.Value;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class EditMusicAlbumDto {

    @Size(min = 2, max = 255, message = "length of album name must be in a range from 2 to 255")
    @NotNull(message = "oldName must not be null")
    String oldName;

    @Size(min = 2, max = 127, message = "length of music group name must be in a range from 2 to 127")
    @NotNull(message = "oldName must not be null")
    String oldMusicGroup;

    @Size(min = 2, max = 255, message = "length of album name must be in a range from 2 to 255")
    String name;

    @Min(value = 1860, message = "must be no less than 1860")
    Short releaseYear;

    @Size(min = 2, max = 127, message = "length of genre name must be in a range from 2 to 127")
    String genre;

    @Size(min = 2, max = 127, message = "length of music group name must be in a range from 2 to 127")
    String musicGroup;
}
