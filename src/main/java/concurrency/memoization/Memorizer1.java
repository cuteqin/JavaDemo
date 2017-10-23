package concurrency.memoization;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:使用HashMap和同步机制来初始化缓存，性能低
 * Created by Administrator on 2017/10/23.
 */
public class Memorizer1<A,R> implements Computable<A,R> {
    private final Map<A,R> cache=new HashMap<>();
    private final Computable<A,R> c;

    public Memorizer1(Computable<A, R> c) {
        this.c = c;
    }
    @Override
    public synchronized R compute(A arg) throws InterruptedException {
        R result=cache.get(arg);
        if(result==null){
            result=c.compute(arg);
            cache.put(arg,result);
        }
        return result;
    }
}
