package springlecture.part3.service.musicalbum.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import springlecture.part3.anotation.qualifier.HqlQualifier;
import springlecture.part3.repository.hibernate.IAlbumHibernateRepo;
import springlecture.part3.repository.hibernate.IGenreHibernateRepo;
import springlecture.part3.repository.hibernate.IMusicGroupHibernateRepo;

@Service("hibernateHqlAlbumService")
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateHql")
public class HibernateHqlAlbumService extends HibernateAlbumBase {

    @Autowired
    public HibernateHqlAlbumService(@HqlQualifier IMusicGroupHibernateRepo musicGroupRepo,
                                    @HqlQualifier IGenreHibernateRepo genreRepo,
                                    @HqlQualifier IAlbumHibernateRepo albumRepo) {
        super(musicGroupRepo, genreRepo, albumRepo);
    }
}
