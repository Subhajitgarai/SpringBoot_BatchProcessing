package com.batch.processing.config;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class DelayingChunkListener implements ChunkListener {

    private final long delayInMilliseconds;

    public DelayingChunkListener(long delayInMilliseconds) {
        this.delayInMilliseconds = delayInMilliseconds;
    }

    @Override
    public void beforeChunk(ChunkContext context) {
        // No action needed before chunk
    }

    @Override
    public void afterChunk(ChunkContext context) {
        try {

            Thread.sleep(delayInMilliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterChunkError(ChunkContext context) {

    }
}
