package springlecture.part3.constants;

public class ErrorConstants {

    private ErrorConstants() {
    }

    public static class Fields {
        public static final String ALBUM = "album";
        public static final String ALL_PARAMETERS = "all parameters";
        public static final String GENRE = "genre";
        public static final String MUSIC_GROUP = "music group";
    public static final String REQUEST_PARAMS = "request parameters";

        private Fields() {
        }
    }

    public static class Msgs {
        public static final String ALREADY_EXISTS = "already exists";
        public static final String CONTAINS_NULL_OR_EMPTY = "contains null or empty";
        public static final String NOT_FOUND = "not found";
        public static final String NULL_OR_EMPTY = "null or empty";

        private Msgs() {
        }
    }
}
