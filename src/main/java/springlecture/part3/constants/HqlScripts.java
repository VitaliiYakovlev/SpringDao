package springlecture.part3.constants;

public class HqlScripts {
    public static final String SELECT_ALBUM_BY_NAME = "FROM Album WHERE LOWER(name) IN (:name)";
    public static final String SELECT_ALBUM_BY_NAME_AND_MUSIC_GROUP = "FROM Album WHERE LOWER(name) = LOWER(:name) AND musicGroup = :musicGroup";
    public static final String SELECT_GENRE_BY_NAME = "FROM Genre WHERE LOWER(name) = LOWER(:name)";
    public static final String SELECT_MUSIC_GROUP_BY_NAME = "FROM MusicGroup WHERE LOWER(name) = LOWER(:name)";
}
