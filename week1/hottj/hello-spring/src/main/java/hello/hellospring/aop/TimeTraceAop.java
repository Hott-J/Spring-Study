package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))") //전부다 적용
    public Object execute(ProceedingJoinPoint joinPoint)throws Throwable{
        long start = System.currentTimeMillis();
        System.out.println("START: "+joinPoint.toString());
        try {
            Object result = joinPoint.proceed();
            return result;
        }finally{
            long finish=System.currentTimeMillis();
            long timeMs=finish-start;
            System.out.println("END:"+joinPoint.toString()+" "+timeMs+"ms");
        }
    }
}
