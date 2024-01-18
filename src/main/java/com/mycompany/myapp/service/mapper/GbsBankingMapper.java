package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.GbsBanking;
import com.mycompany.myapp.service.dto.GbsBankingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GbsBanking} and its DTO {@link GbsBankingDTO}.
 */
@Mapper(componentModel = "spring")
public interface GbsBankingMapper extends EntityMapper<GbsBankingDTO, GbsBanking> {}
