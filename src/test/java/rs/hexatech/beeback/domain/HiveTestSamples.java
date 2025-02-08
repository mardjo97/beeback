package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HiveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Hive getHiveSample1() {
        return new Hive()
            .id(1L)
            .barcode("barcode1")
            .orderNumber(1)
            .description("description1")
            .archivedReason("archivedReason1")
            .externalId(1)
            .uuid("uuid1");
    }

    public static Hive getHiveSample2() {
        return new Hive()
            .id(2L)
            .barcode("barcode2")
            .orderNumber(2)
            .description("description2")
            .archivedReason("archivedReason2")
            .externalId(2)
            .uuid("uuid2");
    }

    public static Hive getHiveRandomSampleGenerator() {
        return new Hive()
            .id(longCount.incrementAndGet())
            .barcode(UUID.randomUUID().toString())
            .orderNumber(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .archivedReason(UUID.randomUUID().toString())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
