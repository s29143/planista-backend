package edu.pjatk.planista.exception;

public class RateLimitExceededException extends RuntimeException {
    private final long retryAfterSeconds;
    private final long limit;
    private final long remaining;

    public RateLimitExceededException(long retryAfterSeconds, long limit, long remaining) {
        super("Rate limit exceeded. Try again later.");
        this.retryAfterSeconds = retryAfterSeconds;
        this.limit = limit;
        this.remaining = remaining;
    }

    public long getRetryAfterSeconds() { return retryAfterSeconds; }
    public long getLimit() { return limit; }
    public long getRemaining() { return remaining; }
}
