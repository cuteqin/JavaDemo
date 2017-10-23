package concurrency.memoization;

import java.math.BigInteger;

/**
 * Created by Administrator on 2017/10/23.
 */
public class ExpensiveFunction implements Computable<String,BigInteger> {
    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        return new BigInteger(arg);
    }
}
