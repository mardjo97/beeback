package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MovedHiveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MovedHive getMovedHiveSample1() {
        return new MovedHive().id(1L).location("location1").externalId(1).uuid("uuid1");
    }

    public static MovedHive getMovedHiveSample2() {
        return new MovedHive().id(2L).location("location2").externalId(2).uuid("uuid2");
    }

    public static MovedHive getMovedHiveRandomSampleGenerator() {
        return new MovedHive()
            .id(longCount.incrementAndGet())
            .location(UUID.randomUUID().toString())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
