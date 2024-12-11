package br.com.thiago.gestao_vagas.modules.candidates.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import br.com.thiago.gestao_vagas.modules.canditates.dto.AuthCandidateRequestDTO;
import br.com.thiago.gestao_vagas.modules.canditates.entities.CandidateEntity;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.CandidateRepository;
import br.com.thiago.gestao_vagas.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class AuthCandidateControllerTest {

  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private CandidateRepository candidateRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(this.context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
  }

  @Test
  @DisplayName("Should be able to authenticate a candidate")
  public void authenticateCandidate() throws Exception {
    String password = "password";

    var candidate = CandidateEntity.builder()
        .username("canidate")
        .email("candidate@example.com")
        .password(this.passwordEncoder.encode(password))
        .name("candidate")
        .description("candidate")
        .build();

    candidate = this.candidateRepository.saveAndFlush(candidate);

    var auth = new AuthCandidateRequestDTO(candidate.getUsername(), password);

    this.mvc.perform(
        MockMvcRequestBuilders.post("/candidates/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.objectToJSON(auth)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.expires_in").isNumber());
  }
}
