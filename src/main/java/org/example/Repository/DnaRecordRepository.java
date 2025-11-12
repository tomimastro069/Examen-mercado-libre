package org.example.Repository;

import org.example.Entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    Optional<DnaRecord> findByDnaHash(String dnaHash);

    @Query("SELECT COUNT(d) FROM DnaRecord d WHERE d.isMutant = true")
    long countMutants();

    @Query("SELECT COUNT(d) FROM DnaRecord d WHERE d.isMutant = false")
    long countHumans();
}