package springlecture.part3.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genres")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BaseGenerator")
    @SequenceGenerator(name = "BaseGenerator", sequenceName = "base_sequence", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false, length = 127)
    private String name;

    @OneToMany(mappedBy = "genre")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Album> albums;

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
