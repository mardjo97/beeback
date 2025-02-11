package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReproductionHiveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReproductionHive getReproductionHiveSample1() {
        return new ReproductionHive().id(1L).note("note1").externalId(1).uuid("uuid1");
    }

    public static ReproductionHive getReproductionHiveSample2() {
        return new ReproductionHive().id(2L).note("note2").externalId(2).uuid("uuid2");
    }

    public static ReproductionHive getReproductionHiveRandomSampleGenerator() {
        return new ReproductionHive()
            .id(longCount.incrementAndGet())
            .note(UUID.randomUUID().toString())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
