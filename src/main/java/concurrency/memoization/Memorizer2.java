package concurrency.memoization;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:用线程安全的concurrentHashMap来实现缓存，有两个线程同时调用可能会发生重复计算，得到相同的值
 * Created by Administrator on 2017/10/23.
 */
public class Memorizer2<A,R> implements Computable<A,R>{
    private final Map<A,R> cache=new ConcurrentHashMap<>();
    private final Computable<A,R> c;

    public Memorizer2(Computable<A, R> c) {
        this.c = c;
    }

    @Override
    public R compute(A arg) throws InterruptedException {
        R result=cache.get(arg);
        if(result==null){
            result=c.compute(arg);
            cache.put(arg,result);
        }
        return result;
    }
}
