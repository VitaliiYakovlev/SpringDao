package springlecture.part3.service.musicalbum.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import springlecture.part3.anotation.qualifier.JpaQualifier;
import springlecture.part3.repository.hibernate.IAlbumHibernateRepo;
import springlecture.part3.repository.hibernate.IGenreHibernateRepo;
import springlecture.part3.repository.hibernate.IMusicGroupHibernateRepo;

@Service("hibernateJpaAlbumService")
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateJpa")
public class HibernateJpaAlbumService extends HibernateAlbumBase {

    @Autowired
    public HibernateJpaAlbumService(@JpaQualifier IMusicGroupHibernateRepo musicGroupRepo,
                                    @JpaQualifier IGenreHibernateRepo genreRepo,
                                    @JpaQualifier IAlbumHibernateRepo albumRepo) {
        super(musicGroupRepo, genreRepo, albumRepo);
    }
}
