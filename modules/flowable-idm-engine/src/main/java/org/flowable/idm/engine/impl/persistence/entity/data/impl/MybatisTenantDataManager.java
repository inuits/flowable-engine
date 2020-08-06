/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.idm.engine.impl.persistence.entity.data.impl;

import java.util.List;
import java.util.Map;

import org.flowable.idm.api.Tenant;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.impl.TenantQueryImpl;
import org.flowable.idm.engine.impl.persistence.entity.TenantEntity;
import org.flowable.idm.engine.impl.persistence.entity.TenantEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.data.AbstractIdmDataManager;
import org.flowable.idm.engine.impl.persistence.entity.data.TenantDataManager;

/**
 * @author Ruben De Swaef
 */
public class MybatisTenantDataManager extends AbstractIdmDataManager<TenantEntity> implements TenantDataManager {

    public MybatisTenantDataManager(IdmEngineConfiguration idmEngineConfiguration) {
        super(idmEngineConfiguration);
    }

    @Override
    public Class<? extends TenantEntity> getManagedEntityClass() {
        return TenantEntityImpl.class;
    }

    @Override
    public TenantEntity create() {
        return new TenantEntityImpl();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tenant> findTenantByQueryCriteria(TenantQueryImpl query) {
        return getDbSqlSession().selectList("selectTenantByQueryCriteria", query, getManagedEntityClass());
    }

    @Override
    public long findTenantCountByQueryCriteria(TenantQueryImpl query) {
        return (Long) getDbSqlSession().selectOne("selectTenantCountByQueryCriteria", query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tenant> findTenantsByNativeQuery(Map<String, Object> parameterMap) {
        return getDbSqlSession().selectListWithRawParameter("selectTenantByNativeQuery", parameterMap);
    }

    @Override
    public long findTenantCountByNativeQuery(Map<String, Object> parameterMap) {
        return (Long) getDbSqlSession().selectOne("selectTenantCountByNativeQuery", parameterMap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tenant> findTenantsByUser(String userId) {
        return getDbSqlSession().selectList("selectTenantsByUserId", userId);
    }
}
