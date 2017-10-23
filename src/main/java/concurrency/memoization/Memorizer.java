package concurrency.memoization;
import java.util.concurrent.*;

import static concurrency.memoization.Memorizer3.launderThrowable;

/**
 * @Description:使用putIfAbsent原子方法，如果检查到future计算任务取消或者遇到异常，则从缓存中移除。此外，并没有解决缓存逾期与缓存清理的问题
 *
 * Created by Administrator on 2017/10/23.
 */
public class Memorizer<A,R> implements Computable<A,R> {
    private final ConcurrentHashMap<A,Future<R>> cache=new ConcurrentHashMap<>();
    private final Computable<A,R> c;

    public Memorizer(Computable<A, R> c) {
        this.c = c;
    }

    @Override
    public R compute(final A arg) throws InterruptedException {
        while (true) {
            Future<R> future = cache.get(arg);
            //以下代码非线程安全，不是原子操作
            if (future == null) {
                Callable<R> eval = new Callable<R>() {
                    @Override
                    public R call() throws InterruptedException {
                        return c.compute(arg);
                    }
                };
                FutureTask<R> ft = new FutureTask<R>(eval);
                future=cache.putIfAbsent(arg,ft);
                if(future==null){
                    future = ft;
                    ft.run();//在这里调用c.compute
                }

            }
            try {
                return future.get();
            }catch (CancellationException e){
                cache.remove(arg,future);
            }
            catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }
    }
}
