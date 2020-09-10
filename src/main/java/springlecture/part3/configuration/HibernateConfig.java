package springlecture.part3.configuration;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@Conditional(HibernateCondition.class)
@EnableTransactionManagement
public class HibernateConfig {

    @Autowired
    DataSource dataSource;

    @Value("${spring.jpa.database-platform}")
    private String dialect;
    @Value("${spring.jpa.show-sql}")
    private boolean isShowSql;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        Properties hibernateSettings = new Properties();
        hibernateSettings.put(Environment.DIALECT, dialect);
        hibernateSettings.put(Environment.SHOW_SQL, isShowSql);
        hibernateSettings.put(Environment.HBM2DDL_AUTO, ddlAuto);
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan("springlecture.part3.entity");
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setHibernateProperties(hibernateSettings);
        return sessionFactory;
    }
}
