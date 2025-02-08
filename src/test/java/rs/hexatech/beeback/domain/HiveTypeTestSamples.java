package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HiveTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HiveType getHiveTypeSample1() {
        return new HiveType().id(1L).Name("Name1").externalId(1).uuid("uuid1");
    }

    public static HiveType getHiveTypeSample2() {
        return new HiveType().id(2L).Name("Name2").externalId(2).uuid("uuid2");
    }

    public static HiveType getHiveTypeRandomSampleGenerator() {
        return new HiveType()
            .id(longCount.incrementAndGet())
            .Name(UUID.randomUUID().toString())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
