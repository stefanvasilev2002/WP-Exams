package mk.ukim.finki.wp.sep2022.service.impl;

import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.sep2022.repository.MatchLocationRepository;
import mk.ukim.finki.wp.sep2022.service.MatchLocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchLocationServiceImpl implements MatchLocationService {
    private final MatchLocationRepository matchLocationRepository;

    public MatchLocationServiceImpl(MatchLocationRepository matchLocationRepository) {
        this.matchLocationRepository = matchLocationRepository;
    }

    @Override
    public MatchLocation findById(Long id) {
        return matchLocationRepository.findById(id).orElseThrow(InvalidMatchLocationIdException::new);
    }

    @Override
    public List<MatchLocation> listAll() {
        return matchLocationRepository.findAll();
    }

    @Override
    public MatchLocation create(String name) {
        return matchLocationRepository.save(new MatchLocation(name));
    }
}
