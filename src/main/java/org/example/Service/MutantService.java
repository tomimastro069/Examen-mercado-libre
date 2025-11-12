package org.example.Service;


import org.example.Entity.DnaRecord;
import org.example.Repository.DnaRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class MutantService {

    @Autowired
    private MutantDetector mutantDetector;

    @Autowired
    private DnaRecordRepository dnaRecordRepository;

    @Transactional
    public boolean analyzeDna(String[] dna) {
        String dnaHash = calculateHash(dna);


        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);

        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }


        boolean isMutant = mutantDetector.isMutant(dna);


        DnaRecord record = new DnaRecord(dnaHash, isMutant);
        dnaRecordRepository.save(record);

        return isMutant;
    }

    private String calculateHash(String[] dna) {
        try {
            String dnaString = String.join(",", Arrays.asList(dna));
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dnaString.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating DNA hash", e);
        }
    }
}