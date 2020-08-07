package org.sperri.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.sperri.zookeeper.Constants;

/**
 * @author jie zhao
 * @date 2019/12/1 14:42
 */
public class CuratorUtils {

    /**
     * Can't be instantiated.
     */
    private CuratorUtils() {

    }

    public static CuratorFramework getClient(String connectionString) {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        return getClient(connectionString, retryPolicy);
    }

    public static CuratorFramework getClient(String connectionString, RetryPolicy retryPolicy) {
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
    }

}
