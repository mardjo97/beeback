package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Group getGroupSample1() {
        return new Group()
            .id(1L)
            .name("name1")
            .enumValueName("enumValueName1")
            .color("color1")
            .hiveCount(1)
            .hiveCountFinished(1)
            .additionalInfo("additionalInfo1")
            .orderNumber(1)
            .externalId(1)
            .uuid("uuid1");
    }

    public static Group getGroupSample2() {
        return new Group()
            .id(2L)
            .name("name2")
            .enumValueName("enumValueName2")
            .color("color2")
            .hiveCount(2)
            .hiveCountFinished(2)
            .additionalInfo("additionalInfo2")
            .orderNumber(2)
            .externalId(2)
            .uuid("uuid2");
    }

    public static Group getGroupRandomSampleGenerator() {
        return new Group()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .enumValueName(UUID.randomUUID().toString())
            .color(UUID.randomUUID().toString())
            .hiveCount(intCount.incrementAndGet())
            .hiveCountFinished(intCount.incrementAndGet())
            .additionalInfo(UUID.randomUUID().toString())
            .orderNumber(intCount.incrementAndGet())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
