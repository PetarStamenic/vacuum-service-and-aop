package rs.raf.vacuumbackend.jwt;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import okhttp3.*;
import rs.raf.vacuumbackend.staticNumers.ROLES;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class SecurityAspect {

    @Around("@annotation(rs.raf.vacuumbackend.jwt.jwt)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String token = null;

        jwt jwtAnnotation = method.getAnnotation(jwt.class);
        int permissions = jwtAnnotation.permisions();

        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            if (methodSignature.getParameterNames()[i].equalsIgnoreCase("authorization")) {
                if (joinPoint.getArgs()[i].toString().startsWith("Bearer")) {
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }

        String path = null;
        switch (permissions){
            case ROLES.CAN_SEARCH_VACUUM:{
                path = "/canUserSearch";
                break;
            }
            case ROLES.CAN_START_VACUUM:{
                path = "/canUserStart";
                break;
            }
            case ROLES.CAN_STOP_VACUUM:{
                path = "/canUserStop";
                break;
            }
            case ROLES.CAN_DISCHARGE_VACUUM:{
                path = "/canUserDischarge";
                break;
            }
            case ROLES.CAN_ADD_VACUUM:{
                path = "/canUserAdd";
                break;
            }
            case ROLES.CAN_REMOVE_VACUUM:{
                path = "/canUserRemove";
                break;
            }
        }
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/user"+path)
                .get()
                .addHeader("Authorization" , "Bearer " + token)
                .build();
        Call call = new OkHttpClient().newCall(request);
        Response response = call.execute();
        if(response.code() == 200)
            return joinPoint.proceed();
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}