package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GbsBankingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static GbsBanking getGbsBankingSample1() {
        return new GbsBanking()
            .id(1L)
            .accountId(1L)
            .creditorName("creditorName1")
            .creditorAccountCode("creditorAccountCode1")
            .description("description1")
            .currency("currency1");
    }

    public static GbsBanking getGbsBankingSample2() {
        return new GbsBanking()
            .id(2L)
            .accountId(2L)
            .creditorName("creditorName2")
            .creditorAccountCode("creditorAccountCode2")
            .description("description2")
            .currency("currency2");
    }

    public static GbsBanking getGbsBankingRandomSampleGenerator() {
        return new GbsBanking()
            .id(longCount.incrementAndGet())
            .accountId(longCount.incrementAndGet())
            .creditorName(UUID.randomUUID().toString())
            .creditorAccountCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString());
    }
}
