package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HarvestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Harvest getHarvestSample1() {
        return new Harvest().id(1L).hiveFrames(1).group("group1").groupRecordId(1).externalId(1).uuid("uuid1");
    }

    public static Harvest getHarvestSample2() {
        return new Harvest().id(2L).hiveFrames(2).group("group2").groupRecordId(2).externalId(2).uuid("uuid2");
    }

    public static Harvest getHarvestRandomSampleGenerator() {
        return new Harvest()
            .id(longCount.incrementAndGet())
            .hiveFrames(intCount.incrementAndGet())
            .group(UUID.randomUUID().toString())
            .groupRecordId(intCount.incrementAndGet())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
