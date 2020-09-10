package springlecture.part3.constants;

public class JpqlScripts {
    public static final String SELECT_ALBUM_BY_NAME = "SELECT a FROM Album a WHERE LOWER(name) IN :name";
    public static final String SELECT_ALBUM_BY_NAME_AND_MUSIC_GROUP = "SELECT a FROM Album a WHERE LOWER(name) = LOWER(:name) AND musicGroup = :musicGroup";
    public static final String SELECT_GENRE_BY_NAME = "SELECT g FROM Genre g WHERE LOWER(name) = LOWER(:name)";
    public static final String SELECT_MUSIC_GROUP_BY_NAME = "SELECT mg FROM MusicGroup mg WHERE LOWER(name) = LOWER(:name)";
}
