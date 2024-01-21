package mk.ukim.finki.wp.jan2023.service.impl;

import mk.ukim.finki.wp.jan2023.model.Candidate;
import mk.ukim.finki.wp.jan2023.model.Gender;
import mk.ukim.finki.wp.jan2023.model.exceptions.InvalidCandidateIdException;
import mk.ukim.finki.wp.jan2023.repository.CandidateRepository;
import mk.ukim.finki.wp.jan2023.service.CandidateService;
import mk.ukim.finki.wp.jan2023.service.PartyService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;
    private final PartyService partyService;

    public CandidateServiceImpl(CandidateRepository candidateRepository, PartyService partyService) {
        this.candidateRepository = candidateRepository;
        this.partyService = partyService;
    }

    @Override
    public List<Candidate> listAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate findById(Long id) {
        return candidateRepository.findById(id).orElseThrow(InvalidCandidateIdException::new);
    }

    @Override
    public Candidate create(String name, String bio, LocalDate dateOfBirth, Gender gender, Long party) {
        return candidateRepository.save(new Candidate(
                name,
                bio,
                dateOfBirth,
                gender,
                partyService.findById(party)
        ));
    }

    @Override
    public Candidate update(Long id, String name, String bio, LocalDate dateOfBirth, Gender gender, Long party) {
        Candidate candidate = findById(id);

        candidate.setName(name);
        candidate.setBio(bio);
        candidate.setDateOfBirth(dateOfBirth);
        candidate.setGender(gender);
        candidate.setParty(partyService.findById(party));

        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate delete(Long id) {
        Candidate candidate = findById(id);
        candidateRepository.delete(candidate);
        return candidate;
    }

    @Override
    public Candidate vote(Long id) {
        Candidate candidate = findById(id);
        candidate.setVotes(candidate.getVotes() + 1);
        return candidateRepository.save(candidate);
    }

    @Override
    public List<Candidate> listCandidatesYearsMoreThanAndGender(Integer yearsMoreThan, Gender gender) {
        if (gender == null && yearsMoreThan == null){
            return listAllCandidates();
        }
        else if (gender != null && yearsMoreThan == null){
            return candidateRepository.findByGender(gender);
        }
        else if (yearsMoreThan != null && gender == null){
            return candidateRepository.findByDateOfBirthBefore(LocalDate.now().minusYears(yearsMoreThan));
        }
        else {
            return candidateRepository
                    .findByGenderAndDateOfBirthBefore(
                            gender,
                            LocalDate.now().minusYears(yearsMoreThan));
        }
    }
}
