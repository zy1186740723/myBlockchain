package com.mindata.blockchain.core.service;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: zhangyan
 * @Date: 2019/4/15 14:47
 * @Version 1.0
 */
public class ThreadConstructor {
    static long tempcount = System.currentTimeMillis()/1000;// 用于计算每秒时间差
    static int prenum = 30;// 用于计算任务数量差
    static int nums = 30;// 总任务数
    static int res = 30;// 总任务数
    static int uses = 0;// 耗时 计数
    static long allstart = System.currentTimeMillis();
    static long logstime = 0;// 日志总耗时

    public static void threadBuilder(){
        int count = 2;//10万并发
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        int n=0;
        long now = System.nanoTime();

        while (!(res==0)) {
            for (int i = 0; i < count; i++) {
                executorService.execute(new ThreadConstructor().new Task(cyclicBarrier, n));
                n++;
                res--;
            }
        }

        executorService.shutdown();
        //是否有进程在执行
        while (!executorService.isTerminated()) {
            try {
                //System.out.println("!!!!!!");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.nanoTime();
        double time=(end-now)/1000000000.0;
        System.out.println("All is finished!---------"+time+"s");
    }


    public class Task extends Thread implements Runnable {
        private CyclicBarrier cyclicBarrier;
        int n;

        public Task(CyclicBarrier cyclicBarrier, int n) {
            this.cyclicBarrier = cyclicBarrier;
            this.n = n;
        }

        @Override
        public void run() {
            try {
                // 等待所有任务准备就绪

                System.out.println(Thread.currentThread().getName() + "准备就绪");
                cyclicBarrier.await();
                System.out.println(System.currentTimeMillis() + "：开始线程" + Thread.currentThread().getName());
                //
                // 测试内容
                long start = System.currentTimeMillis();
                task();
                long timeUse = (System.currentTimeMillis() - start);
                getCurrentThreads(timeUse);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //生成区块的具体任务
        public void task(){
            //String s=TestDemo.sendGet("http://localhost:6144/Home/RequestString", "key=123&v=456");
            String s= addNewBlockService.sendGet("http://119.23.11.21:8890",null);
            System.out.println("===================="+Thread.currentThread().getName()+"内容返回"+"====================");
            System.out.println(s);
            System.out.println("====================================");
            //发送 POST 请求
            String sr=addNewBlockService.sendPost("http://119.23.11.21:8888/member/detail", "name=xiaozhang&appId=wolf&ip=172.18.1.12");
            System.out.println(sr);
        }

    /**
     * 对结果的一些测试方法
     * @param use
     */
    public static void getCurrentThreads(long use){
        long start=System.currentTimeMillis();
        prenum=prenum-1;//任务数量，进度扣减
        uses+=use;


        long now = System.currentTimeMillis()/1000;
        if(now > tempcount){// 每秒输出一次
            long freeMemory=Runtime.getRuntime().freeMemory() / 1024 / 1024;//已使用内存
            long totalMemory=Runtime.getRuntime().totalMemory() / 1024 / 1024;//总共可使用内存
            int cpu = Runtime.getRuntime().availableProcessors();//可用cpu逻辑处理器

            System.out.printf("第%s秒: ", now-allstart/1000);
            int ts = (nums-prenum);//每秒事务数
            System.out.printf("每秒处理数:%s ", ts);
            System.out.printf("平均耗时:%s ", ts==0?0:uses/ts);
            System.out.printf("进度:%s ", nums);
            System.out.printf("剩余:%s毫秒 ", nums*ts);
            System.out.printf("可用内存:%sm ", freeMemory);
            System.out.printf("可用总内存:%sm \n", totalMemory);
            tempcount = now;
            nums = prenum;
            uses =0;
        }

        logstime += System.currentTimeMillis()-start;// 日志耗时累计

        // 当任务执行完了以后 计算总耗时
        if(prenum==0){
            long alluse = System.currentTimeMillis()-allstart;
            System.out.printf("总耗时%s毫秒,其中日志耗时%s毫秒\n",alluse,logstime);
            //System.exit(0);
        }
    }




}
