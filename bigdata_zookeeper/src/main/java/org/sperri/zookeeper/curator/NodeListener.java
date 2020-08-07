package org.sperri.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.sperri.zookeeper.Constants;

/**
 * @author jie zhao
 * @date 2019/12/1 14:59
 *
 * 事件监听
 */
public class NodeListener {

    private CuratorFramework client = CuratorUtils.getClient(Constants.ZK_CONNECTION_STRING);

    /**
     * 监听节点变化
     * @throws Exception
     */
    public void nodeListen() throws Exception {
        client.start();
        final NodeCache cache = new NodeCache(client, "/test/node1");
        cache.start();
        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                byte[] data = cache.getCurrentData().getData();
            }
        });
    }

    /**
     * 监听节点的子节点变化
     * @throws Exception
     */
    public void childNodeListen() throws Exception {
        client.start();
        final PathChildrenCache cache = new PathChildrenCache(client, "/test/node1", true);
        cache.start();
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                switch (pathChildrenCacheEvent.getType()) {
                    // 根据不同的事件类型，执行不同的处理逻辑
                    case CHILD_ADDED:
                        System.out.println(pathChildrenCacheEvent.getData());
                        break;
                    case CHILD_UPDATED:
                        break;
                    case CHILD_REMOVED:
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
