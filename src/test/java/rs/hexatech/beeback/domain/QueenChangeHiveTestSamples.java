package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QueenChangeHiveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QueenChangeHive getQueenChangeHiveSample1() {
        return new QueenChangeHive().id(1L).reminderId("reminderId1").externalId(1).uuid("uuid1");
    }

    public static QueenChangeHive getQueenChangeHiveSample2() {
        return new QueenChangeHive().id(2L).reminderId("reminderId2").externalId(2).uuid("uuid2");
    }

    public static QueenChangeHive getQueenChangeHiveRandomSampleGenerator() {
        return new QueenChangeHive()
            .id(longCount.incrementAndGet())
            .reminderId(UUID.randomUUID().toString())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
