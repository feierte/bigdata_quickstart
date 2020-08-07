package org.sperri.zookeeper.curator.election;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

/**
 * @author jie zhao
 * @date 2019/12/1 20:07
 */
public class WorkServer extends LeaderSelectorListenerAdapter {


    private volatile boolean running = false;  // 服务器状态

    private CuratorFramework zkClient;
    private static final String MASTER_PATH = "/master";


    private RunningData serverData; // 服务器的信息
    private RunningData masterData; // master节点服务器的信息

    public WorkServer(RunningData runningData) {

    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {

    }

    public void start() throws Exception {

    }

    public void stop() throws Exception {

    }


    private void takeMaster() {

    }

    private void releaseMaster() {

    }

    private boolean isMaster() {
        return false;
    }
}
