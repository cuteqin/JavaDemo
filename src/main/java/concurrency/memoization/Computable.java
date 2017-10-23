package concurrency.memoization;

/**
 * Created by Administrator on 2017/10/23.
 */
public interface Computable<A,R> {
    R compute(A arg) throws InterruptedException;
}
