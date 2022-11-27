package be.pxl.coronavirus.repository;

import be.pxl.coronavirus.domain.VirusTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirusTestRepository extends JpaRepository<VirusTest, Long> {
}