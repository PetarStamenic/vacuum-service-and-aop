package rs.raf.userbackend.jwt;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.raf.userbackend.repository.UserRepository;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class SecurityAspect {

    private String jwtSecret = "secret_key";
    private TokenService tokenService;
    private UserRepository userRepository;

    public SecurityAspect(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Around("@annotation(rs.raf.userbackend.jwt.jwt)")
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

        if(token == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Claims claims = tokenService.parseToken(token);
        if(claims == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Long id = claims.get("id",Long.class);
        int role = claims.get("permissions",Integer.class);
        if(!(userRepository.findUserByUserId(id).isPresent() && userRepository.findUserByUserId(id).get().getPermissions() == role))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if((role & permissions) == permissions){
            return joinPoint.proceed();
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
