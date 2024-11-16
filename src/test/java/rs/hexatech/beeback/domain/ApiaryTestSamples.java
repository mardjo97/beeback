package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApiaryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Apiary getApiarySample1() {
        return new Apiary()
            .id(1L)
            .name("name1")
            .idNumber("idNumber1")
            .color("color1")
            .location("location1")
            .orderNumber(1)
            .hiveCount(1)
            .externalId(1)
            .uuid("uuid1");
    }

    public static Apiary getApiarySample2() {
        return new Apiary()
            .id(2L)
            .name("name2")
            .idNumber("idNumber2")
            .color("color2")
            .location("location2")
            .orderNumber(2)
            .hiveCount(2)
            .externalId(2)
            .uuid("uuid2");
    }

    public static Apiary getApiaryRandomSampleGenerator() {
        return new Apiary()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .idNumber(UUID.randomUUID().toString())
            .color(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .orderNumber(intCount.incrementAndGet())
            .hiveCount(intCount.incrementAndGet())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
