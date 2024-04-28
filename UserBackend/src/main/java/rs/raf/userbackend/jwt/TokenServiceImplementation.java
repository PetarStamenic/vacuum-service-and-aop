package rs.raf.userbackend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImplementation implements TokenService{

    @Value("secret_key")
    private static String jwtSecret = "secret_key";
    @Override
    public String generate(Claims claims) {
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,jwtSecret).compact();
    }

    @Override
    public Claims parseToken(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
    }
}
