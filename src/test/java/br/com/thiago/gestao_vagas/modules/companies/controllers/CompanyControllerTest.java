package br.com.thiago.gestao_vagas.modules.companies.controllers;

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

import br.com.thiago.gestao_vagas.modules.companies.entities.CompanyEntity;
import br.com.thiago.gestao_vagas.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class CompanyControllerTest {

  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @Before
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(this.context)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
  }

  @Test
  @DisplayName("Should be able to create a new company")
  public void createNewCompany() throws Exception {

    var company = CompanyEntity.builder()
        .description("company")
        .email("company@example.com.br")
        .name("company")
        .password("Company@123")
        .username("Company")
        .website("http://example.com")
        .build();

    this.mvc.perform(
        MockMvcRequestBuilders.post("/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.objectToJSON(company)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(company.getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(company.getUsername()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(company.getEmail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.website").value(company.getWebsite()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(company.getDescription()));

  }

}