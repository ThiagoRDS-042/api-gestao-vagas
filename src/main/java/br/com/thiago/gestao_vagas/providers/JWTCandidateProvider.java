package br.com.thiago.gestao_vagas.providers;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JWTCandidateProvider {

  @Value("${security.token.secret.candidate}")
  private String secretKey;

  public String createTokenWithClains(String identifier, List<String> clains, Instant expiresIn) {
    Algorithm algorithm = Algorithm.HMAC256(this.secretKey);

    var token = JWT.create()
        .withIssuer("javagas")
        .withClaim("roles", clains)
        .withExpiresAt(expiresIn)
        .withSubject(identifier)
        .sign(algorithm);

    return token;
  }

  public DecodedJWT validateToken(String token) {
    token = token.replace("Bearer ", "");

    Algorithm algorithm = Algorithm.HMAC256(this.secretKey);

    try {
      var tokenDecoded = JWT.require(algorithm).build().verify(token);

      return tokenDecoded;
    } catch (JWTVerificationException exception) {
      exception.printStackTrace();
      return null;
    }

  }
}
