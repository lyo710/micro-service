/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
/**
 * project : service-gateway-agent
 * user created : pippo
 * date created : 2012-6-6 - 下午7:03:19
 */
package com.baidu.ub.msoa.utils.jetty;

import org.eclipse.jetty.server.session.AbstractSession;
import org.eclipse.jetty.server.session.AbstractSessionManager;

import javax.servlet.http.HttpServletRequest;

/**
 * EmptySessionManager
 *
 * @author pippo
 * @since 2012-6-6
 */
public class EmptySessionManager extends AbstractSessionManager {

    private static EmptySession default_session;

    public EmptySessionManager() {
        _sessionIdManager = new EmptySessionIdManager();
        if (default_session == null) {
            default_session = new EmptySession(this, null);
        }
    }

    @Override
    protected void addSession(AbstractSession session) {

    }

    @Override
    public AbstractSession getSession(String idInCluster) {
        return default_session;
    }

    @Override
    protected void shutdownSessions() throws Exception {

    }

    @Override
    protected AbstractSession newSession(HttpServletRequest request) {
        return default_session;
    }

    @Override
    protected boolean removeSession(String idInCluster) {
        return true;
    }

    @Override
    public void renewSessionId(String oldClusterId, String oldNodeId, String newClusterId, String newNodeId) {
        // TODO Auto-generated method stub

    }

}
