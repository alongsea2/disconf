package com.baidu.disconf.web.service.app.dao.impl;

import java.util.*;

import com.baidu.unbiz.common.genericdao.operator.Order;
import com.baidu.unbiz.common.genericdao.param.LikeParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.dao.AppDao;
import com.baidu.dsp.common.dao.AbstractDao;
import com.baidu.dsp.common.dao.Columns;
import com.baidu.unbiz.common.genericdao.operator.Match;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class AppDaoImpl extends AbstractDao<Long, App> implements AppDao {

    @Override
    public App getByName(String name) {

        return findOne(new Match(Columns.NAME, name));
    }

    @Override
    public List<App> getByIds(Set<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return findAll();
        }

        return find(match(Columns.APP_ID, ids));
    }

    @Override
    public List<App> getByIds(Set<Long> ids,Integer pageNo,Integer pageSize,String appName) {
        List<Match> list = new ArrayList<Match>();
        if(ids != null && ids.size() > 0){
            list.add(match(Columns.APP_ID, ids));
        }
        if(StringUtils.isNotBlank(appName)){
            list.add(match(Columns.NAME,new LikeParam(appName)));
        }
        return find(list,new ArrayList<Order>(0),pageNo,pageSize);
    }

}
