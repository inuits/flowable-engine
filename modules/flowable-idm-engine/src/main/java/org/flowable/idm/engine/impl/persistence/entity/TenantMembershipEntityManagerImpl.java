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

import org.flowable.idm.api.event.FlowableIdmEventType;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.delegate.event.impl.FlowableIdmEventBuilder;
import org.flowable.idm.engine.impl.persistence.entity.data.TenantMembershipDataManager;

/**
 * @author Ruben De Swaef
 */
public class TenantMembershipEntityManagerImpl
    extends AbstractIdmEngineEntityManager<TenantMembershipEntity, TenantMembershipDataManager>
    implements TenantMembershipEntityManager {

    public TenantMembershipEntityManagerImpl(IdmEngineConfiguration idmEngineConfiguration, TenantMembershipDataManager tenantMembershipDataManager) {
        super(idmEngineConfiguration, tenantMembershipDataManager);
    }

    @Override
    public void createTenantMembership(String userId, String tenantId) {
        TenantMembershipEntity tenantMembershipEntity = create();
        tenantMembershipEntity.setUserId(userId);
        tenantMembershipEntity.setTenantId(tenantId);
        insert(tenantMembershipEntity, false);

        if (getEventDispatcher() != null && getEventDispatcher().isEnabled()) {
            getEventDispatcher().dispatchEvent(FlowableIdmEventBuilder.createTenantMembershipEvent(FlowableIdmEventType.MEMBERSHIP_CREATED, tenantId, userId));
        }
    }

    @Override
    public void deleteTenantMembership(String userId, String tenantId) {
        dataManager.deleteTenantMembership(userId, tenantId);
        if (getEventDispatcher() != null && getEventDispatcher().isEnabled()) {
            getEventDispatcher().dispatchEvent(FlowableIdmEventBuilder.createTenantMembershipEvent(FlowableIdmEventType.MEMBERSHIP_DELETED, tenantId, userId));
        }
    }

    @Override
    public void deleteTenantMembershipByTenantId(String tenantId) {
        dataManager.deleteTenantMembershipByTenantId(tenantId);
    }

    @Override
    public void deleteTenantMembershipByUserId(String userId) {
        dataManager.deleteTenantMembershipByUserId(userId);
    }

}
