package org.sperri.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.data.Stat;
import org.sperri.zookeeper.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jie zhao
 * @date 2019/12/1 14:40
 */
public class CheckZnodeExists {

    private CuratorFramework client = CuratorUtils.getClient(Constants.ZK_CONNECTION_STRING);

    /**
     * 同步方式判断zookeeper节点是否存在
     * @return
     * @throws Exception
     */
    public boolean isExists() throws Exception {
        client.start();
        // 如果节点存在，则返回的Stat不为null。
        Stat stat = client.checkExists().forPath("/test/node1");
        if (null != stat) {
            return true;
        }
        return false;
    }

    /**
     * 异步方式判断zookeeper节点是否存在
     * @return
     */
    public void isExistsAsync(String path) throws Exception {

        client.start();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        /**
         * 执行异步操作时，curator会新创建一个线程来执行
         */
        client.checkExists().inBackground(new BackgroundCallback() {
            /**
             *
             * @param client curator客户端对象
             * @param event curator事件对象
             * @throws Exception
             */
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                // 通过curator事件对象可以得到如下信息
                CuratorEventType eventType = event.getType(); // 事件类型
                int resultCode = event.getResultCode();// 事件执行的返回码，成功则返回0，失败则返回非0
                event.getContext();
                event.getPath();
                event.getChildren();
                event.getData();

                Stat stat = event.getStat();
            }
        }, "context", executorService).forPath(path);
    }
}
