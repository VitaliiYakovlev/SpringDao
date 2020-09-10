package springlecture.part3.constants;

public class SqlScripts {
    public static final String INSERT_ALBUM = "INSERT INTO albums (id, name, year_release, music_group_id, genre_id) " +
            "VALUES (nextval('base_sequence'), ?, ?, ?, ?) " +
            "ON CONFLICT ON CONSTRAINT uk_albums_name_groupid DO NOTHING;";

    public static final String SELECT_ALBUMS_TEMPLATE = "SELECT a.name as album_name, a.year_release, g.name as genre_name, mg.name as group_name " +
            "FROM albums a " +
            "INNER JOIN genres g ON g.id = a.genre_id " +
            "INNER JOIN music_groups mg ON mg.id = a.music_group_id " +
            "WHERE LOWER(a.name) IN (%s);";
    public static final String SELECT_EXISTS_ALBUM_AND_MUSIC_GROUP_ID = "SELECT EXISTS" +
            "(SELECT 1 FROM albums WHERE LOWER(name) = LOWER(?) and music_group_id = ?)";
    public static final String SELECT_GENRE = "SELECT id FROM genres WHERE LOWER(name) = LOWER(?);";
    public static final String SELECT_MUSIC_GROUP_BY_NAME = "SELECT id FROM music_groups WHERE LOWER(name) = LOWER(?);";

    public static final String UPDATE_ALBUM_TEMPLATE = "UPDATE albums SET %s WHERE LOWER(name) = LOWER(?) and music_group_id = ?;";
}