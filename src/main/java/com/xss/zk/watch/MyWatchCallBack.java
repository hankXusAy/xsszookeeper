package com.xss.zk.watch;

import com.xss.zk.MyConf;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class MyWatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    private CountDownLatch countDownLatch;
    private ZooKeeper zooKeeper;
    private MyConf myConf;

    public void setMyConf(MyConf myConf) {
        this.myConf = myConf;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        switch (watchedEvent.getType()){

            case None:
                break;
            case NodeCreated:
                System.out.println("节点<" + watchedEvent.getPath() + ">被创建了(#^.^#))");
                zooKeeper.getData(watchedEvent.getPath(),this,this,"ctx");
                break;
            case NodeDeleted:
                System.out.println("节点<" + watchedEvent.getPath() + ">被删除了 (⊙o⊙)…");
                myConf.setData("");
                countDownLatch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                System.out.println("节点<" + watchedEvent.getPath() + ">发生修改操作 (*^▽^*)");
                zooKeeper.getData(watchedEvent.getPath(),this,this,"ctx");
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }


    @Override
    public void processResult(int i, String path, Object o, Stat stat) {
        if(stat != null){
            zooKeeper.getData(path,this,this,"ctx");
        }else {
            System.out.println("state callback ：啥也没有...");
        }
    }
    @Override
    public void processResult(int i, String path, Object ctx, byte[] bytes, Stat stat) {
        if(bytes != null){
            myConf.setData(new String(bytes));
            countDownLatch.countDown();
        }else {
            System.out.println("data callback : 没有数据。。。");
        }
    }

    public void aWait() {
        zooKeeper.exists("/zk01",this,this,"ctx");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
