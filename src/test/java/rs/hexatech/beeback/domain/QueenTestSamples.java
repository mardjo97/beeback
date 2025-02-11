package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QueenTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Queen getQueenSample1() {
        return new Queen().id(1L).origin("origin1").year(1).externalId(1).uuid("uuid1");
    }

    public static Queen getQueenSample2() {
        return new Queen().id(2L).origin("origin2").year(2).externalId(2).uuid("uuid2");
    }

    public static Queen getQueenRandomSampleGenerator() {
        return new Queen()
            .id(longCount.incrementAndGet())
            .origin(UUID.randomUUID().toString())
            .year(intCount.incrementAndGet())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
