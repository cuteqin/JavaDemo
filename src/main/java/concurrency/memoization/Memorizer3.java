package concurrency.memoization;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @Description:用futureTask来检查某个线程是否已经开始相同的计算，如开始则阻塞等待，如未开始则启动一个futureTask
 * Created by Administrator on 2017/10/23.
 */
public class Memorizer3<A,R> implements Computable<A,R> {

    private final Map<A,Future<R>> cache=new ConcurrentHashMap<>();
    private final Computable<A,R> c;

    public Memorizer3(Computable<A, R> c) {
        this.c = c;
    }

    @Override
    public R compute(final A arg) throws InterruptedException {
       Future<R> future=cache.get(arg);
       //以下代码非线程安全，不是原子操作
       if(future==null){
           Callable<R> eval=new Callable<R>() {
               @Override
               public R call() throws InterruptedException {
                   return c.compute(arg);
               }
           };
           FutureTask<R> ft=new FutureTask<R>(eval);
           future=ft;
           cache.put(arg,ft);
           ft.run();//在这里调用c.compute
       }
       try {
           return future.get();
       }catch (ExecutionException e){
            throw launderThrowable(e.getCause());
       }
    }
    public static RuntimeException launderThrowable(Throwable t){
        if(t instanceof RuntimeException){
            return (RuntimeException)t;
        }else if(t instanceof Error){
            throw (Error)t;
        }else{
            throw new IllegalStateException("not checked",t);
        }
    }
}
