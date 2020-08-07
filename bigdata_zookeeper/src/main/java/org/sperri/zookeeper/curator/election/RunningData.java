package org.sperri.zookeeper.curator.election;

import java.io.Serializable;

/**
 * @author jie zhao
 * @date 2019/12/1 20:01
 */
public class RunningData implements Serializable {

    private static final long serialVersionUID = 6514063427617837373L;

    private long cid; // 服务器id
    private String name; // 服务器名称

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
