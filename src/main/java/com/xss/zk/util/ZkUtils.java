package com.xss.zk.util;

import com.xss.zk.watch.DefaultWatch;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkUtils {

    private static final String zk_addr = "192.168.150.131,192.168.150.129,192.168.150.130/testZk";

    private static DefaultWatch defaultWatch = new DefaultWatch();

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static final ZooKeeper getZk(){
        ZooKeeper zooKeeper;
        try {
            defaultWatch.setCountDownLatch(countDownLatch);
            zooKeeper = new ZooKeeper(zk_addr,3000,defaultWatch);
            countDownLatch.await();
        } catch (Exception e) {
            return null;
        }
        return zooKeeper;
    }

}
