package springlecture.part3.anotation.qualifier;

import org.springframework.beans.factory.annotation.Qualifier;
import java.lang.annotation.*;

@Inherited
@Qualifier("hql")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER})
public @interface HqlQualifier {
}
