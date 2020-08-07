package org.sperri.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.sperri.zookeeper.Constants;

public class CuratorTest {

    public static void main(String[] args) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(10000, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient(Constants.ZK_CONNECTION_STRING, retryPolicy);
        client.start();


    }
}
