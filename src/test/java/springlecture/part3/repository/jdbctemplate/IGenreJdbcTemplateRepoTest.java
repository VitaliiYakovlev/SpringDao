package springlecture.part3.repository.jdbctemplate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import springlecture.part3.entity.Genre;
import springlecture.part3.repository.springdata.IGenreSpringDataRepo;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IGenreJdbcTemplateRepoTest {
    @Autowired
    private IGenreSpringDataRepo cut;
    private final Genre[] genres = new Genre[] {
            new Genre(3L, "Genre 1", Collections.emptySet()),  new Genre(6L, "Genre 2", Collections.emptySet())
    };
    @Before
    public void setUp() {
        Arrays.stream(genres).forEach(genre -> cut.save(genre));
    }
    @Test
    public void testPresent1() {
        Optional<Genre> act = cut.findByNameIgnoreCase("genre 1");

        assertTrue(act.isPresent());
        assertEquals(genres[0], act.get());
    }
    @Test
    public void testPresent2() {
        Optional<Genre> act = cut.findByNameIgnoreCase("Genre 2");

        assertTrue(act.isPresent());
        assertEquals(genres[1], act.get());
    }
    @Test
    public void testNotPresent3() {
        Optional<Genre> act = cut.findByNameIgnoreCase("Unknown Genre 1");

        assertFalse(act.isPresent());
    }
}