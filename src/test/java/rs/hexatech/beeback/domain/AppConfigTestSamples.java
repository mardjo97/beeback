package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AppConfig getAppConfigSample1() {
        return new AppConfig().id(1L).key("key1").type("type1").value("value1").externalId(1).uuid("uuid1");
    }

    public static AppConfig getAppConfigSample2() {
        return new AppConfig().id(2L).key("key2").type("type2").value("value2").externalId(2).uuid("uuid2");
    }

    public static AppConfig getAppConfigRandomSampleGenerator() {
        return new AppConfig()
            .id(longCount.incrementAndGet())
            .key(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
