package br.com.thiago.gestao_vagas.modules.companies.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.thiago.gestao_vagas.modules.companies.entities.JobEntity;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {
  List<JobEntity> findByDescriptionContainingIgnoreCase(String filter);
}
