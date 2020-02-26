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

package org.flowable.idm.engine.impl.persistence.entity;

import java.util.List;
import java.util.Map;

import org.flowable.idm.api.Tenant;
import org.flowable.idm.api.TenantQuery;
import org.flowable.idm.api.event.FlowableIdmEventType;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.delegate.event.impl.FlowableIdmEventBuilder;
import org.flowable.idm.engine.impl.TenantQueryImpl;
import org.flowable.idm.engine.impl.persistence.entity.data.TenantDataManager;

/**
 * @author Ruben De Swaef
 */
public class TenantEntityManagerImpl
    extends AbstractIdmEngineEntityManager<TenantEntity, TenantDataManager>
    implements TenantEntityManager {

    public TenantEntityManagerImpl(IdmEngineConfiguration idmEngineConfiguration, TenantDataManager tenantDataManager) {
        super(idmEngineConfiguration, tenantDataManager);
    }

    @Override
    public Tenant createNewTenant(String tenantId) {
        TenantEntity tenantEntity = dataManager.create();
        tenantEntity.setId(tenantId);
        tenantEntity.setRevision(0);
        return tenantEntity;
    }

    @Override
    public boolean isNewTenant(Tenant tenant) {
        return ((TenantEntity) tenant).getRevision() == 0;
    }

    @Override
    public TenantQuery createNewTenantQuery() {
        return new TenantQueryImpl(getCommandExecutor());
    }

    @Override
    public List<Tenant> findTenantByQueryCriteria(TenantQueryImpl query) {
        return dataManager.findTenantByQueryCriteria(query);
    }

    @Override
    public List<Tenant> findTenantsByNativeQuery(Map<String, Object> parameterMap) {
        return dataManager.findTenantsByNativeQuery(parameterMap);
    }

    @Override
    public long findTenantCountByQueryCriteria(TenantQueryImpl query) {
        return dataManager.findTenantCountByQueryCriteria(query);
    }

    @Override
    public long findTenantCountByNativeQuery(Map<String, Object> parameterMap) {
        return dataManager.findTenantCountByNativeQuery(parameterMap);
    }

    @Override
    public List<Tenant> findTenantsByUser(String userId) {
        return dataManager.findTenantsByUser(userId);
    }
}
