package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GbsBankingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GbsBankingDTO.class);
        GbsBankingDTO gbsBankingDTO1 = new GbsBankingDTO();
        gbsBankingDTO1.setId(1L);
        GbsBankingDTO gbsBankingDTO2 = new GbsBankingDTO();
        assertThat(gbsBankingDTO1).isNotEqualTo(gbsBankingDTO2);
        gbsBankingDTO2.setId(gbsBankingDTO1.getId());
        assertThat(gbsBankingDTO1).isEqualTo(gbsBankingDTO2);
        gbsBankingDTO2.setId(2L);
        assertThat(gbsBankingDTO1).isNotEqualTo(gbsBankingDTO2);
        gbsBankingDTO1.setId(null);
        assertThat(gbsBankingDTO1).isNotEqualTo(gbsBankingDTO2);
    }
}
