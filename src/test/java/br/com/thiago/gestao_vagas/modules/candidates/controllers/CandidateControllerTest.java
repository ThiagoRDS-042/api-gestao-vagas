package br.com.thiago.gestao_vagas.modules.candidates.controllers;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

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

import br.com.thiago.gestao_vagas.modules.canditates.dto.ProfileCandidateResponseDTO;
import br.com.thiago.gestao_vagas.modules.canditates.entities.CandidateEntity;
import br.com.thiago.gestao_vagas.modules.canditates.repositories.CandidateRepository;
import br.com.thiago.gestao_vagas.modules.companies.entities.CompanyEntity;
import br.com.thiago.gestao_vagas.modules.companies.entities.JobEntity;
import br.com.thiago.gestao_vagas.modules.companies.repositories.CompanyRepository;
import br.com.thiago.gestao_vagas.modules.companies.repositories.JobRepository;
import br.com.thiago.gestao_vagas.providers.JWTCandidateProvider;
import br.com.thiago.gestao_vagas.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class CandidateControllerTest {
        private MockMvc mvc;

        @Autowired
        private WebApplicationContext context;

        @Autowired
        private CompanyRepository companyRepository;

        @Autowired
        private JobRepository jobRepository;

        @Autowired
        private CandidateRepository candidateRepository;

        @Autowired
        private JWTCandidateProvider jwtCandiateProvider;

        @Before
        public void setup() {
                mvc = MockMvcBuilders
                                .webAppContextSetup(this.context)
                                .apply(SecurityMockMvcConfigurers.springSecurity())
                                .build();
        }

        @Test
        @DisplayName("Should be able to create a new candidate")
        public void createNewCandidate() throws Exception {

                var candidate = CandidateEntity.builder()
                                .username("canidate")
                                .email("candidate@example.com")
                                .password("password")
                                .name("candidate")
                                .description("candidate")
                                .build();

                this.mvc.perform(
                                MockMvcRequestBuilders.post("/candidates")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(candidate)))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("Should be able to get a candidate profile")
        public void candidateProfile() throws Exception {
                var candidate = CandidateEntity.builder()
                                .username("canidate")
                                .email("candidate@example.com")
                                .password("password")
                                .name("candidate")
                                .description("candidate")
                                .curriculum("candidate")
                                .build();

                candidate = this.candidateRepository.saveAndFlush(candidate);

                Instant expiresIn = Instant.now().plus(Duration.ofHours(2));

                var token = this.jwtCandiateProvider.createTokenWithClains(candidate.getId().toString(),
                                Arrays.asList("CANDIDATE"),
                                expiresIn);

                var response = ProfileCandidateResponseDTO.builder()
                                .id(candidate.getId())
                                .description(candidate.getDescription())
                                .email(candidate.getEmail())
                                .name(candidate.getName())
                                .username(candidate.getUsername())
                                .build();

                this.mvc.perform(
                                MockMvcRequestBuilders.get("/candidates/profile")
                                                .header("Authorization", token))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().json(TestUtils.objectToJSON(response)));
        }

        @Test
        @DisplayName("Should be able to list all jobs by filter")
        public void listAllJobsByFilter() throws Exception {
                var candidate = CandidateEntity.builder()
                                .id(UUID.randomUUID())
                                .username("canidate")
                                .email("candidate@example.com")
                                .password("password")
                                .name("candidate")
                                .description("candidate")
                                .curriculum("candidate")
                                .build();

                Instant expiresIn = Instant.now().plus(Duration.ofHours(2));

                var token = this.jwtCandiateProvider.createTokenWithClains(candidate.getId().toString(),
                                Arrays.asList("CANDIDATE"),
                                expiresIn);

                var company = CompanyEntity.builder()
                                .description("company")
                                .email("company@example.com.br")
                                .name("company")
                                .password("Company@123")
                                .username("Company")
                                .website("http://example.com")
                                .build();

                company = this.companyRepository.saveAndFlush(company);

                var job = JobEntity.builder()
                                .benefits("Job")
                                .companyId(company.getId())
                                .description("Job description")
                                .level("all jobs")
                                .build();

                job = this.jobRepository.saveAndFlush(job);

                this.mvc.perform(
                                MockMvcRequestBuilders.get("/candidates/jobs")
                                                .queryParam("filter", job.getDescription())
                                                .header("Authorization", token))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.*").isArray())
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(job.getId().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].companyId")
                                                .value(job.getCompanyId().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description")
                                                .value(job.getDescription()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].level").value(job.getLevel()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].benefits").value(job.getBenefits()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt")
                                                .value(job.getCreatedAt().toString()));
        }

        @Test
        @DisplayName("Should be able to apply job")
        public void applyJob() throws Exception {
                var candidate = CandidateEntity.builder()
                                .username("canidate")
                                .email("candidate@example.com")
                                .password("password")
                                .name("candidate")
                                .description("candidate")
                                .curriculum("candidate")
                                .build();

                candidate = this.candidateRepository.saveAndFlush(candidate);

                Instant expiresIn = Instant.now().plus(Duration.ofHours(2));

                var token = this.jwtCandiateProvider.createTokenWithClains(candidate.getId().toString(),
                                Arrays.asList("CANDIDATE"),
                                expiresIn);

                var company = CompanyEntity.builder()
                                .description("company")
                                .email("company@example.com.br")
                                .name("company")
                                .password("Company@123")
                                .username("Company")
                                .website("http://example.com")
                                .build();

                company = this.companyRepository.saveAndFlush(company);

                var job = JobEntity.builder()
                                .benefits("Job")
                                .companyId(company.getId())
                                .description("Job description")
                                .level("all jobs")
                                .build();

                job = this.jobRepository.saveAndFlush(job);

                this.mvc.perform(
                                MockMvcRequestBuilders.post("/candidates/jobs/{jobId}/apply", job.getId())
                                                .header("Authorization", token))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.candidateId")
                                                .value(candidate.getId().toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.jobId").value(job.getId().toString()));

        }

}
