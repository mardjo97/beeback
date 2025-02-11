package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GoodHarvestHiveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static GoodHarvestHive getGoodHarvestHiveSample1() {
        return new GoodHarvestHive().id(1L).externalId(1).uuid("uuid1");
    }

    public static GoodHarvestHive getGoodHarvestHiveSample2() {
        return new GoodHarvestHive().id(2L).externalId(2).uuid("uuid2");
    }

    public static GoodHarvestHive getGoodHarvestHiveRandomSampleGenerator() {
        return new GoodHarvestHive()
            .id(longCount.incrementAndGet())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
