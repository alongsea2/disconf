package com.baidu.disconf.client.core.watch.inner;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.core.DisconfCoreMgr;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.zookeeper.ZookeeperMgr;

/**
 * 结点监控器
 * 
 * @author liaoqiqi
 * @version 2014-6-16
 */
public class NodeWatcher implements Watcher {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(NodeWatcher.class);

    private String monitorPath = "";
    private String keyName = "";
    private DisConfigTypeEnum disConfigTypeEnum;
    private DisconfSysUpdateCallback disconfSysUpdateCallback;

    private DisconfCoreMgr disconfCoreMgr;

    public NodeWatcher(DisconfCoreMgr disconfCoreMgr, String monitorPath,
            String keyName, DisConfigTypeEnum disConfigTypeEnum,
            DisconfSysUpdateCallback disconfSysUpdateCallback) {

        super();
        this.disconfCoreMgr = disconfCoreMgr;
        this.monitorPath = monitorPath;
        this.keyName = keyName;
        this.disConfigTypeEnum = disConfigTypeEnum;
        this.disconfSysUpdateCallback = disconfSysUpdateCallback;
    }

    /**
     * 
     * @param monitorPath
     */
    public void monitorMaster() {

        //
        // 监控
        //
        Stat stat = new Stat();
        try {

            ZookeeperMgr.getInstance().read(monitorPath, this, stat);

        } catch (InterruptedException e) {

        } catch (KeeperException e) {
            LOGGER.error("cannot monitor " + monitorPath, e);
        }

        LOGGER.info("monitor path: (" + monitorPath + "," + keyName + ","
                + disConfigTypeEnum.getModelName() + ") has been added!");
    }

    /**
     * 回调函数
     */
    @Override
    public void process(WatchedEvent event) {

        //
        // 结点更新时
        //
        if (event.getType() == EventType.NodeDataChanged) {

            try {

                LOGGER.info("============GOT UPDATE EVENT: (" + monitorPath
                        + "," + keyName + ","
                        + disConfigTypeEnum.getModelName()
                        + ")======================");

                // 调用回调函数, 回调函数里会重新进行监控
                try {
                    disconfSysUpdateCallback.reload(disconfCoreMgr,
                            disConfigTypeEnum, keyName);
                } catch (Exception e) {
                    LOGGER.error(e.toString(), e);
                }

            } catch (Exception e) {

                LOGGER.error("monitor node exception. " + monitorPath, e);
            }
        }
    }
}