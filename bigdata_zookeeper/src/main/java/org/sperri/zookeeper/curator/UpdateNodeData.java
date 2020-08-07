package org.sperri.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.data.Stat;
import org.sperri.zookeeper.Constants;

/**
 * @author jie zhao
 * @date 2019/12/1 14:34
 */
public class UpdateNodeData {
    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(Constants.ZK_CONNECTION_STRING)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        Stat stat = new Stat();
        // 先做一次查询，得到znode的信息
        byte[] bytes = client.getData().storingStatIn(stat).forPath("/test/node4");

        client.setData().withVersion(stat.getVersion()).forPath("/test/node4", "12321".getBytes());

    }
}
