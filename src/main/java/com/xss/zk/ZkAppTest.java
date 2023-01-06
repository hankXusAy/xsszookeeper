package com.xss.zk;


import com.xss.zk.util.ZkUtils;
import com.xss.zk.watch.MyWatchCallBack;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class ZkAppTest {

    ZooKeeper zooKeeper;

    @Before
    public void initZk(){
        zooKeeper = ZkUtils.getZk();
    }

    @After
    public void colstZk(){
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void startZk() throws InterruptedException {
        MyWatchCallBack myWatchCallBack = new MyWatchCallBack();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MyConf myConf = new MyConf();

        myWatchCallBack.setCountDownLatch(countDownLatch);
        myWatchCallBack.setMyConf(myConf);
        myWatchCallBack.setZooKeeper(zooKeeper);

        myWatchCallBack.aWait();



        while (true){
            if(myConf.getData().equals("")){
                System.out.println("conf diu le ......");
                myWatchCallBack.aWait();
            }else{
                System.out.println("conf：" + myConf.getData());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
