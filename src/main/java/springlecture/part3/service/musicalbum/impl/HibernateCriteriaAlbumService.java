package springlecture.part3.service.musicalbum.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import springlecture.part3.anotation.qualifier.CriteriaQualifier;
import springlecture.part3.repository.hibernate.IAlbumHibernateRepo;
import springlecture.part3.repository.hibernate.IGenreHibernateRepo;
import springlecture.part3.repository.hibernate.IMusicGroupHibernateRepo;

@Service("hibernateCriteriaAlbumService")
@ConditionalOnProperty(name = "db.connector", havingValue = "hibernateCriteria")
public class HibernateCriteriaAlbumService extends HibernateAlbumBase {

    @Autowired
    public HibernateCriteriaAlbumService(@CriteriaQualifier IMusicGroupHibernateRepo musicGroupRepo,
                                         @CriteriaQualifier IGenreHibernateRepo genreRepo,
                                         @CriteriaQualifier IAlbumHibernateRepo albumRepo) {
        super(musicGroupRepo, genreRepo, albumRepo);
    }
}
