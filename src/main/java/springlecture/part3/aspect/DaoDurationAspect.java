package springlecture.part3.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class DaoDurationAspect {

    @Around("execution(* springlecture.part3.service.musicalbum.IMusicAlbumService.*(..))")
    public Object aroundServiceMethodExecution(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        Object res = jp.proceed();
        long end = System.currentTimeMillis();
        log.info("Execution of method {} took {} msec.", jp.getSignature(), (end - start));
        return res;
    }
}
