package rs.raf.userbackend.jwt;

import io.jsonwebtoken.Claims;

public interface TokenService {
    String generate(Claims claims);
    Claims parseToken(String jwt);
}
