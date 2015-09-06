/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : service-gateway-agent
 * user created : pippo
 * date created : 2012-6-6 - 下午7:04:28
 */
package com.baidu.ub.msoa.utils.jetty;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jetty.server.session.AbstractSession;
import org.eclipse.jetty.server.session.AbstractSessionManager;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 * EmptySession
 *
 * @author pippo
 * @since 2012-6-6
 */
public class EmptySession extends AbstractSession {

    EmptySession(AbstractSessionManager abstractSessionManager, HttpServletRequest request) {
        super(abstractSessionManager, request);
    }

    EmptySession(AbstractSessionManager abstractSessionManager, long created, long accessed, String clusterId) {
        super(abstractSessionManager, created, accessed, clusterId);
    }

    @Override
    protected void checkValid() throws IllegalStateException {

    }

    @Override
    public long getAccessed() {
        return 1L;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public int getAttributes() {
        return 0;
    }

    private static Enumeration<String> emptyEnumeration = new Enumeration<String>() {

        @Override
        public boolean hasMoreElements() {
            return false;
        }

        @Override
        public String nextElement() {
            return null;
        }

    };

    @Override
    public Enumeration<String> getAttributeNames() {
        return emptyEnumeration;
    }

    @Override
    public Set<String> getNames() {
        return Collections.emptySet();
    }

    @Override
    public long getCookieSetTime() {
        return 1L;
    }

    @Override
    public long getCreationTime() throws IllegalStateException {
        return 1L;
    }

    @Override
    public String getId() throws IllegalStateException {
        return EmptySessionIdManager.defaultId;
    }

    @Override
    public long getLastAccessedTime() throws IllegalStateException {
        return 1;
    }

    @Override
    public int getMaxInactiveInterval() {
        return 1;
    }

    @Override
    public Object getValue(String name) throws IllegalStateException {
        return null;
    }

    @Override
    public String[] getValueNames() throws IllegalStateException {
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }

    @Override
    protected boolean access(long time) {
        return true;
    }

    @Override
    protected void complete() {

    }

    @Override
    protected void timeout() throws IllegalStateException {

    }

    @Override
    public void invalidate() throws IllegalStateException {

    }

    @Override
    protected void doInvalidate() throws IllegalStateException {

    }

    @Override
    public void clearAttributes() {

    }

    @Override
    public boolean isIdChanged() {
        return false;
    }

    @Override
    public boolean isNew() throws IllegalStateException {
        return false;
    }

    @Override
    public void putValue(String name, Object value) throws IllegalStateException {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public void removeValue(String name) throws IllegalStateException {

    }

    @Override
    public Object doPutOrRemove(String name, Object value) {
        return null;
    }

    @Override
    public Object doGet(String name) {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {

    }

    @Override
    public void setIdChanged(boolean changed) {

    }

    @Override
    public void setMaxInactiveInterval(int secs) {

    }

    @Override
    public void bindValue(String name, Object value) {

    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    protected void cookieSet() {

    }

    @Override
    public int getRequests() {
        return 1;
    }

    @Override
    public void setRequests(int requests) {

    }

    @Override
    public void unbindValue(String name, Object value) {

    }

    @Override
    public void willPassivate() {

    }

    @Override
    public void didActivate() {

    }

    @Override
    public Map<String, Object> getAttributeMap() {
        return Collections.emptyMap();
    }

    @Override
    public Enumeration<String> doGetAttributeNames() {
        return Collections.emptyEnumeration();
    }

}
