package org.sperri.zookeeper.demo.distributedLock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * @author jie zhao
 * @date 2020/3/30 19:17
 */
public class DistributedQueue {


    private static final String ROOT = "/queue";

    private static final String PREFIX_QUEUE = "qn-";

    private ZooKeeper zooKeeper;

    private List<ACL> acl = ZooDefs.Ids.OPEN_ACL_UNSAFE;


    /**
     * Insert data into queue.
     *
     * @param data
     * @return true if data was successfully added.
     */
    public boolean offer(byte[] data) throws KeeperException, InterruptedException {
        for (; ; ) {
            try {
                zooKeeper.create(ROOT + "/" + PREFIX_QUEUE, data, acl, CreateMode.PERSISTENT_SEQUENTIAL);
                return true;
            } catch (KeeperException.NoNodeException e) {
                zooKeeper.create(ROOT + "/" + PREFIX_QUEUE, new byte[0], acl, CreateMode.PERSISTENT);
            }
        }
    }

}
