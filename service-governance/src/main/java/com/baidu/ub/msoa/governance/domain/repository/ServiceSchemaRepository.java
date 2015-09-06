package com.baidu.ub.msoa.governance.domain.repository;

/**
 * Created by pippo on 15/8/27.
 */
public interface ServiceSchemaRepository {

    /**
     * save schema
     *
     * @param provider
     * @param service
     * @param version
     * @param schema
     */
    void save(int provider, String service, int version, String schema);

    /**
     * @param provider
     * @param service
     * @param version
     * @return schema
     */
    String get(int provider, String service, int version);

}
