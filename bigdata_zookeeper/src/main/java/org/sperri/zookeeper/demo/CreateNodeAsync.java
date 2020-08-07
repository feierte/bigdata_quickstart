package org.sperri.zookeeper.demo;

import org.apache.zookeeper.*;
import org.sperri.zookeeper.Constants;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 异步方式创建znode
 */
public class CreateNodeAsync implements Watcher {

    /**
     * zookeeper的连接客户端
     */
    private static ZooKeeper zooKeeper;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            zooKeeper = new ZooKeeper(Constants.ZK_CONNECTION_STRING, Constants.SESSION_TIMEOUT, new CreateNodeAsync());
            countDownLatch.await();
            zooKeeper.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        Event.KeeperState keeperState = event.getState();
        Event.EventType eventType = event.getType();
        if (keeperState == Event.KeeperState.SyncConnected) {
            if (eventType == Event.EventType.None && null == event.getPath()) {
                // TODO: 当连接zookeeper成功后，执行的逻辑
                createNodeAsync();
                //countDownLatch.countDown();
                System.out.println("ZK建立连接：" + keeperState);
            }
        }
    }


    private void createNodeAsync() {
        /**
         * 异步方式创建节点。
         * 参数一：path the path for node.
         * 参数二：the initial data for the node.
         * 参数三：节点的访问控制权限
         * 参数四：指定创建的节点是短暂节点还是顺序节点
         * 参数五：{@code StringCallback}用于异步创建的回调函数
         * 参数六：参数类型为Object，异步创建时的上下文。
         */
        zooKeeper.create("/test/node2", "123456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
                    /**
                     * rc：返回的结果状态码
                     * path：要创建的节点路径
                     * ctx：异步创建的上下文，即为上面传入的参数六
                     * name：创建成功时返回的节点路径
                     */
                    @Override
                    public void processResult(int rc, String path, Object ctx, String name) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("rc = " + rc).append("\n");
                        sb.append("path = " + path).append("\n");
                        sb.append("ctx = " + ctx).append("\n");
                        sb.append("name = " + name);
                        System.out.println(sb.toString());
                    }
                }, "创建");
    }
}
