package br.com.thiago.gestao_vagas.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestUtils {

  public static String objectToJSON(Object object) {
    try {
      final ObjectMapper objectMapper = new ObjectMapper();

      objectMapper.registerModule(new JavaTimeModule());

      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}
