package springlecture.part3.dto.touser;

import lombok.Value;
import java.util.List;

@Value
public class MusicGroupDto {
    String name;
    int creationYear;
    int decayYear;
    int availableAlbums;
    List<String> genres;
}