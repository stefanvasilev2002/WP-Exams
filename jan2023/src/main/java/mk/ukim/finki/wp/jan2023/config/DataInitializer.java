package mk.ukim.finki.wp.jan2023.config;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.jan2023.model.Gender;
import mk.ukim.finki.wp.jan2023.service.CandidateService;
import mk.ukim.finki.wp.jan2023.service.PartyService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class DataInitializer {

    private final PartyService partyService;

    private final CandidateService candidateService;

    public DataInitializer(PartyService partyService, CandidateService candidateService) {
        this.partyService = partyService;
        this.candidateService = candidateService;
    }

    private Gender randomizeGender(int i) {
        if(i % 2 == 0) return Gender.MALE;
        return Gender.FEMALE;
    }
    @PostConstruct
    public void initData() {
        for (int i = 1; i < 6; i++) {
            this.partyService.create("Party: " + i);
        }

        for (int i = 1; i < 11; i++) {
            this.candidateService.create("Candidate: " + i, "Bio: " + i , LocalDate.now().minusYears(25+i), this.randomizeGender(i), this.partyService.listAll().get((i-1)%5).getId());
        }
    }
}
