package br.com.thiago.gestao_vagas.modules.companies.controllers;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import br.com.thiago.gestao_vagas.modules.companies.dto.CreateJobDTO;
import br.com.thiago.gestao_vagas.modules.companies.entities.CompanyEntity;
import br.com.thiago.gestao_vagas.modules.companies.repositories.CompanyRepository;
import br.com.thiago.gestao_vagas.providers.JWTCompanyProvider;
import br.com.thiago.gestao_vagas.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class CreateJobControllerTest {

        private MockMvc mvc;

        @Autowired
        private WebApplicationContext context;

        @Autowired
        private CompanyRepository companyRepository;

        @Autowired
        private JWTCompanyProvider jwtProvider;

        @Before
        public void setup() {
                mvc = MockMvcBuilders
                                .webAppContextSetup(this.context)
                                .apply(SecurityMockMvcConfigurers.springSecurity())
                                .build();
        }

        @Test
        @DisplayName("Should be able to create a new job")
        public void createNewJob() throws Exception {

                var createJobDTO = CreateJobDTO.builder()
                                .benefits("test")
                                .description("test")
                                .level("test")
                                .build();

                var company = CompanyEntity.builder()
                                .description("company")
                                .email("company@example.com.br")
                                .name("company")
                                .password("Company@123")
                                .username("Company")
                                .website("http://example.com")
                                .build();

                company = this.companyRepository.saveAndFlush(company);

                Instant expiresIn = Instant.now().plus(Duration.ofHours(2));

                var token = this.jwtProvider.createTokenWithClains(company.getId().toString(), Arrays.asList("COMPANY"),
                                expiresIn);

                this.mvc.perform(
                                MockMvcRequestBuilders.post("/companies/jobs")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(createJobDTO))
                                                .header("Authorization", token))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

}
