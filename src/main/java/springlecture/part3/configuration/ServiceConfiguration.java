package springlecture.part3.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springlecture.part3.service.musicalbum.IMusicAlbumService;

@Configuration
public class ServiceConfiguration {

    private final String BEAN_NAME_TEMPLATE = "%sAlbumService";

    @Autowired
    private ApplicationContext context;

    @Bean
    public IMusicAlbumService musicAlbumService(@Value("${db.connector}") String qualifier) {
        return context.getBean(String.format(BEAN_NAME_TEMPLATE, qualifier), IMusicAlbumService.class);
    }
}
