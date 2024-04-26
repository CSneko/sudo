package org.cneko.sudo.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactories{
    public static class ModrinthThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger count = new AtomicInteger(1);

        public ModrinthThreadFactory() {
            this.namePrefix = "Modrinth API Thread";
        }

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(r, namePrefix + "-pool-" + count.getAndIncrement());
            return t;
        }
    }
    public static class BashThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger count = new AtomicInteger(1);

        public BashThreadFactory() {
            this.namePrefix = "Bash Thread";
        }

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(r, namePrefix + "-pool-" + count.getAndIncrement());
            return t;
        }
    }
}
