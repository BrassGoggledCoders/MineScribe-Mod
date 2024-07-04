package xyz.brassgoggledcoders.minescribe.mod.resource;

import java.util.concurrent.atomic.AtomicInteger;

public record FileCounter(
        AtomicInteger success,
        AtomicInteger failure,
        AtomicInteger total
) {
    public FileCounter() {
        this(
                new AtomicInteger(0),
                new AtomicInteger(0),
                new AtomicInteger(0)
        );
    }

    public void increaseSuccess() {
        success.incrementAndGet();
        total.incrementAndGet();
    }

    public void increaseFailure() {
        failure.incrementAndGet();
        total.incrementAndGet();
    }

    public void combine(FileCounter fileCounterOther) {
        this.success.addAndGet(fileCounterOther.success.get());
        this.failure.addAndGet(fileCounterOther.failure.get());
        this.total.addAndGet(fileCounterOther.total.get());
    }
}
