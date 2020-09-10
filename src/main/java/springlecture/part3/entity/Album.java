package springlecture.part3.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "albums",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "music_group_id"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BaseGenerator")
    @SequenceGenerator(name = "BaseGenerator", sequenceName = "base_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "year_release")
    private short yearRelease;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "music_group_id", referencedColumnName = "id", nullable = false)
    private MusicGroup musicGroup;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", referencedColumnName = "id", nullable = false)
    private Genre genre;

    public Album(String name, short yearRelease, MusicGroup musicGroup, Genre genre) {
        this.name = name;
        this.yearRelease = yearRelease;
        this.musicGroup = musicGroup;
        this.genre = genre;
    }
}
