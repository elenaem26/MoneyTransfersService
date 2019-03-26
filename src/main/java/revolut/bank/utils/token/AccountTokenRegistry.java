package revolut.bank.utils.token;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountTokenRegistry {

    private static Map<String, LockValue> lockMap = new HashMap<>();

    public synchronized static LockValue register(String key) {
        LockValue res = lockMap.get(key);
        if (res == null) {
            LockValue token = new LockValue();
            res = lockMap.putIfAbsent(key, token);
            if (res == null) {
                res = token;
            }
        } else {
            lockMap.get(key).increment();
        }
        return res;
    }

    public synchronized static LockValue deregister(String key) {
        LockValue lockValue = lockMap.get(key);
        if (lockValue != null) {
            lockValue.decrement();
            if (lockValue.isZero()) {
                return lockMap.remove(key);
            }
        }
        return lockValue;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode(exclude = "counter")
    private static class LockValue {
        AtomicInteger counter = new AtomicInteger(1);
        String number;

        void increment() {
            counter.incrementAndGet();
        }

        void decrement() {
            counter.decrementAndGet();
        }

        boolean isZero() {
            return counter.get() == 0;
        }
    }
}
