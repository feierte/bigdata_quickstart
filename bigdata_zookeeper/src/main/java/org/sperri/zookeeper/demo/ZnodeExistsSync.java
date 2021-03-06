package org.sperri.zookeeper.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.sperri.zookeeper.Constants;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZnodeExistsSync implements Watcher {

    /** zookeeper的连接客户端 */
    private static ZooKeeper zooKeeper;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            /**
             * ZooKeeper客户端与服务器端会话的建立是一个异步的过程，也就是说在程序中，构造方法会在处理完客户端初始化工作后立即返回。
             * 在大多数情况下，此时并没有真正建立一个可用的会话，在会话的生命周期中处于“CONNECTING”的状态。
             */
            zooKeeper = new ZooKeeper(Constants.ZK_CONNECTION_STRING, Constants.SESSION_TIMEOUT, new ZnodeExistsSync());
            System.out.println("与zookeeper连接状态: " + zooKeeper.getState());
            TimeUnit.SECONDS.sleep(1000);
            //countDownLatch.await();
            zooKeeper.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听ZooKeeper有什么变化，如果ZooKeeper服务器端有变化，将会在这里接收
     * @param event A WatchedEvent represents a change on the ZooKeeper that a Watcher is able to respond to.
     */
    @Override
    public void process(WatchedEvent event) {
        // 获取与ZooKeeper的连接状态
        Watcher.Event.KeeperState keeperState = event.getState();
        // 获取ZooKeeper发生的事件类型
        Watcher.Event.EventType eventType = event.getType();
        // 如果客户端与ZooKeeper服务器端建立了俩姐
        if (Watcher.Event.KeeperState.SyncConnected == keeperState) {
            if (Watcher.Event.EventType.None == eventType && null == event.getPath()) {
                // TODO: 当连接zookeeper成功后，执行的逻辑
                isExists();
                // 如果建立连接成功，则发送信号量，让随后阻塞的线程继续运行
                //countDownLatch.countDown();
                System.out.println("ZK建立连接：" + keeperState);
            } else {
                try {
                    // 创建新节点
                    if (eventType == Event.EventType.NodeCreated) {
                        System.out.println(event.getPath() + " created.");
                        System.out.println(zooKeeper.exists(event.getPath(), true));
                    }

                    // 改变节点数据
                    if (eventType == Event.EventType.NodeDataChanged) {
                        System.out.println(event.getPath() + " updated.");
                        System.out.println(zooKeeper.exists(event.getPath(), true));
                    }

                    // 删除节点
                    if (eventType == Event.EventType.NodeDeleted) {
                        System.out.println(event.getPath() + " deleted.");
                        System.out.println(zooKeeper.exists(event.getPath(), true));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void isExists() {
        try {
            Stat stat = zooKeeper.exists("/test", true);
            System.out.println(stat);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
