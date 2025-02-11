package rs.hexatech.beeback.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Note getNoteSample1() {
        return new Note()
            .id(1L)
            .title("title1")
            .content("content1")
            .group("group1")
            .groupRecordId(1)
            .reminderId(1)
            .externalId(1)
            .uuid("uuid1");
    }

    public static Note getNoteSample2() {
        return new Note()
            .id(2L)
            .title("title2")
            .content("content2")
            .group("group2")
            .groupRecordId(2)
            .reminderId(2)
            .externalId(2)
            .uuid("uuid2");
    }

    public static Note getNoteRandomSampleGenerator() {
        return new Note()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .content(UUID.randomUUID().toString())
            .group(UUID.randomUUID().toString())
            .groupRecordId(intCount.incrementAndGet())
            .reminderId(intCount.incrementAndGet())
            .externalId(intCount.incrementAndGet())
            .uuid(UUID.randomUUID().toString());
    }
}
