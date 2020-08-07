package org.sperri.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperTest {

    /** zookeeper集群地址 */
    private static final String ZK_CLUSTER_HOSTS = "192.168.116.129,192.168.116.130,192.168.116.131";

    /** zookeeper端口 */
    private static final String ZK_CLUSTER_PORT = "2181";

    /** 会话超时时间 */
    private static final int SESSION_TIMEOUT = 30000;

    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zk = new ZooKeeper(ZK_CLUSTER_HOSTS, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 获取时间的状态
                Event.KeeperState keeperState = event.getState();
                Event.EventType eventType = event.getType();

                // 如果建立了连接
                if (Event.KeeperState.SyncConnected == keeperState) {
                    if (Event.EventType.None == eventType && null == event.getPath()) {
                        // 如果建立连接成功，则发送信号量，让随后阻塞的线程继续运行
                        COUNT_DOWN_LATCH.countDown();
                        System.out.println("ZK建立连接");
                    }
                }
            }
        });

        System.out.println("与zk连接状态: " + zk.getState());

        // 阻塞
        COUNT_DOWN_LATCH.await();

        // 创建父节点
        String parentNode = zk.create("/test", "123456".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("parent node: " + parentNode);

        // 创建子节点
        String childNode = zk.create("/test/child", "45678".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("child node: " + childNode);

        zk.close();
    }
}
