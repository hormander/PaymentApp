package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.GbsBanking;
import com.mycompany.myapp.repository.GbsBankingRepository;
import com.mycompany.myapp.service.GbsBankingService;
import com.mycompany.myapp.service.dto.GbsBankingDTO;
import com.mycompany.myapp.service.mapper.GbsBankingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.GbsBanking}.
 */
@Service
@Transactional
public class GbsBankingServiceImpl implements GbsBankingService {

    private final Logger log = LoggerFactory.getLogger(GbsBankingServiceImpl.class);

    private final GbsBankingRepository gbsBankingRepository;

    private final GbsBankingMapper gbsBankingMapper;

    public GbsBankingServiceImpl(GbsBankingRepository gbsBankingRepository, GbsBankingMapper gbsBankingMapper) {
        this.gbsBankingRepository = gbsBankingRepository;
        this.gbsBankingMapper = gbsBankingMapper;
    }

    @Override
    public GbsBankingDTO save(GbsBankingDTO gbsBankingDTO) {
        log.debug("Request to save GbsBanking : {}", gbsBankingDTO);
        GbsBanking gbsBanking = gbsBankingMapper.toEntity(gbsBankingDTO);
        gbsBanking = gbsBankingRepository.save(gbsBanking);
        return gbsBankingMapper.toDto(gbsBanking);
    }

    @Override
    public GbsBankingDTO update(GbsBankingDTO gbsBankingDTO) {
        log.debug("Request to update GbsBanking : {}", gbsBankingDTO);
        GbsBanking gbsBanking = gbsBankingMapper.toEntity(gbsBankingDTO);
        gbsBanking = gbsBankingRepository.save(gbsBanking);
        return gbsBankingMapper.toDto(gbsBanking);
    }

    @Override
    public Optional<GbsBankingDTO> partialUpdate(GbsBankingDTO gbsBankingDTO) {
        log.debug("Request to partially update GbsBanking : {}", gbsBankingDTO);

        return gbsBankingRepository
            .findById(gbsBankingDTO.getId())
            .map(existingGbsBanking -> {
                gbsBankingMapper.partialUpdate(existingGbsBanking, gbsBankingDTO);

                return existingGbsBanking;
            })
            .map(gbsBankingRepository::save)
            .map(gbsBankingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GbsBankingDTO> findAll() {
        log.debug("Request to get all GbsBankings");
        return gbsBankingRepository.findAll().stream().map(gbsBankingMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GbsBankingDTO> findOne(Long id) {
        log.debug("Request to get GbsBanking : {}", id);
        return gbsBankingRepository.findById(id).map(gbsBankingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GbsBanking : {}", id);
        gbsBankingRepository.deleteById(id);
    }
}
