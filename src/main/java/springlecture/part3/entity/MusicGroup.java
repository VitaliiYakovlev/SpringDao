package springlecture.part3.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "music_groups")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MusicGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BaseGenerator")
    @SequenceGenerator(name = "BaseGenerator", sequenceName = "base_sequence", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false, length = 127)
    private String name;

    @Column(name = "year_creation")
    private short yearCreation;

    @Column(name = "year_decay")
    private Short yearDecay;

    @OneToMany(mappedBy = "musicGroup")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Album> albums;

    public MusicGroup(Long id, String name, short yearCreation, Short yearDecay) {
        this.id = id;
        this.name = name;
        this.yearCreation = yearCreation;
        this.yearDecay = yearDecay;
    }
}
