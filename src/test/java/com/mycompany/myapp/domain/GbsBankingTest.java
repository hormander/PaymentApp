package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.GbsBankingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GbsBankingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GbsBanking.class);
        GbsBanking gbsBanking1 = getGbsBankingSample1();
        GbsBanking gbsBanking2 = new GbsBanking();
        assertThat(gbsBanking1).isNotEqualTo(gbsBanking2);

        gbsBanking2.setId(gbsBanking1.getId());
        assertThat(gbsBanking1).isEqualTo(gbsBanking2);

        gbsBanking2 = getGbsBankingSample2();
        assertThat(gbsBanking1).isNotEqualTo(gbsBanking2);
    }
}
