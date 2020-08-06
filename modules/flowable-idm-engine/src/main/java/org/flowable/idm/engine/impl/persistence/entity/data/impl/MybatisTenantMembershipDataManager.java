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

import java.util.HashMap;
import java.util.Map;

import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.impl.persistence.entity.TenantMembershipEntity;
import org.flowable.idm.engine.impl.persistence.entity.TenantMembershipEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.data.AbstractIdmDataManager;
import org.flowable.idm.engine.impl.persistence.entity.data.TenantMembershipDataManager;

/**
 * @author Joram Barrez
 */
public class MybatisTenantMembershipDataManager extends AbstractIdmDataManager<TenantMembershipEntity> implements TenantMembershipDataManager {

    public MybatisTenantMembershipDataManager(IdmEngineConfiguration idmEngineConfiguration) {
        super(idmEngineConfiguration);
    }

    @Override
    public Class<? extends TenantMembershipEntity> getManagedEntityClass() {
        return TenantMembershipEntityImpl.class;
    }

    @Override
    public TenantMembershipEntity create() {
        return new TenantMembershipEntityImpl();
    }

    @Override
    public void deleteTenantMembership(String userId, String tenantId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("tenantId", tenantId);
        getDbSqlSession().delete("deleteTenantMembership", parameters, getManagedEntityClass());
    }

    @Override
    public void deleteTenantMembershipByTenantId(String tenantId) {
        getDbSqlSession().delete("deleteTenantMembershipsByTenantId", tenantId, getManagedEntityClass());
    }

    @Override
    public void deleteTenantMembershipByUserId(String userId) {
        getDbSqlSession().delete("deleteTenantMembershipsByUserId", userId, getManagedEntityClass());
    }

}
