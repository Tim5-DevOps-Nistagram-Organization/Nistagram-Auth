package rs.ac.uns.ftn.devops.tim5.nistagramauth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.AccessTokenDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    @Value("1000000000")
    public Long expiration;
    @Value("myEventSecret")
    private String secret;

    public String getUsernameFromToken(String token) {
        Claims claims = this.getClaimsFromToken(token);
        if (claims == null)
            return null;
        return claims.getSubject();
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token.substring(7)).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public Date getExpirationDateFromToken(String token) {
        final Claims claims = this.getClaimsFromToken(token);
        if (claims == null)
            return null;
        return claims.getExpiration();
    }

    private boolean isTokenExpired(String token) {
        final Date expirationDate = this.getExpirationDateFromToken(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    public boolean validateToken(String token, String userDetailsUsername) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetailsUsername)
                && !isTokenExpired(token);
    }

    public AccessTokenDTO generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities());
        claims.put("created", new Date(System.currentTimeMillis()));
        return new AccessTokenDTO(Jwts.builder().setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact());
    }

}