package org.example.Service;

import org.example.Repository.DnaRecordRepository;
import org.example.dto.StatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    @Autowired
    public DnaRecordRepository dnaRecordRepository;

    public StatsResponse getStats() {
        long mutantCount = dnaRecordRepository.countMutants();
        long humanCount = dnaRecordRepository.countHumans();

        double ratio = humanCount == 0 ? 0.0 : (double) mutantCount / humanCount;

        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}