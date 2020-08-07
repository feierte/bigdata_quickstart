package org.sperri.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.sperri.zookeeper.Constants;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author jie zhao
 * @date 2019/11/27 20:19
 *
 * 同步方式创建znode并授权
 *
 * {@code Watcher}是ZooKeeper提供的公共接口，该接口监视使客户端能够接收来自ZooKeeper服务器端的通知，
 * 并在发生时处理这些事件。客户端事件处理程序类必须实现该接口才能接收有关来自ZooKeeper服务器端的事件通知。
 */
public class CreateNodeSyncAuth implements Watcher {

    /** zookeeper的连接客户端 */
    private static ZooKeeper zooKeeper;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            /**
             * ZooKeeper客户端与服务器端会话的建立是一个异步的过程，也就是说在程序中，构造方法会在处理完客户端初始化工作后立即返回。
             * 在大多数情况下，此时并没有真正建立一个可用的会话，在会话的生命周期中处于“CONNECTING”的状态。
             */
            zooKeeper = new ZooKeeper(Constants.ZK_CONNECTION_STRING, Constants.SESSION_TIMEOUT, new CreateNodeSyncAuth());
            System.out.println("与zookeeper连接状态: " + zooKeeper.getState());
            //TimeUnit.SECONDS.sleep(100);
            countDownLatch.await();
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
        Event.KeeperState keeperState = event.getState();
        // 获取ZooKeeper发生的事件类型
        Event.EventType eventType = event.getType();
        // 如果客户端与ZooKeeper服务器端建立了俩姐
        if (Event.KeeperState.SyncConnected == keeperState) {
            if (Event.EventType.None == eventType && null == event.getPath()) {
                // TODO: 当连接zookeeper成功后，执行的逻辑
                createNodeAuth();
                // 如果建立连接成功，则发送信号量，让随后阻塞的线程继续运行
                countDownLatch.countDown();
                System.out.println("ZK建立连接：" + keeperState);
            }
        }
    }

    /**
     * 权限模式（scheme）：ip，digest
     * 授权对象（id）：
     *      ip权限模式：具体的ip地址
     *      digest权限模式：username:Base64(SHA-1(username:password))
     * 权限（permission）：CREATE(C), DELETE(D), READ(R), WRITE(W), ADMIN(A)
     *      注：单个权限、复合权限、完全权限
     *
     * 权限组合：scheme + id + permission
     */
    private void createNodeAuth() {
        try {
            ACL ipAcl = new ACL(ZooDefs.Perms.READ, new Id("ip", "192.168.116.130"));
            ACL deigestAcl = new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE,
                    new Id("digest", DigestAuthenticationProvider.generateDigest("username:password")));
            List<ACL> acls = new ArrayList<>();
            acls.add(ipAcl);
            acls.add(ipAcl);
            //zooKeeper.addAuthInfo("digest", "username:password".getBytes());
            /**
             * 参数一：path the path for node.
             * 参数二：the initial data for the node.
             * 参数三：节点的访问控制权限
             * 参数四：指定创建的节点是短暂节点还是顺序节点
             */
            String path = zooKeeper.create("/test", "123456".getBytes(), acls, CreateMode.PERSISTENT);
            System.out.println("成功了创建节点：" + path);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
