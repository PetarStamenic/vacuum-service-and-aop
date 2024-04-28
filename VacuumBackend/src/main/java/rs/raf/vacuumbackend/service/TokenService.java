package rs.raf.vacuumbackend.service;

import io.jsonwebtoken.Claims;

public interface TokenService {
    Claims parseToken(String jwt);
}
