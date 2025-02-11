package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HarvestTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HarvestType getHarvestTypeSample1() {
        return new HarvestType().id(1L).name("name1").externalId(1).uuid("uuid1");
    }

    public static HarvestType getHarvestTypeSample2() {
        return new HarvestType().id(2L).name("name2").externalId(2).uuid("uuid2");
    }

    public static HarvestType getHarvestTypeRandomSampleGenerator() {
        return new HarvestType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
