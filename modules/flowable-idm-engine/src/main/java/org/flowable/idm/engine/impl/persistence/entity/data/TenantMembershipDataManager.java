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
package org.flowable.idm.engine.impl.persistence.entity.data;

import org.flowable.common.engine.impl.persistence.entity.data.DataManager;
import org.flowable.idm.engine.impl.persistence.entity.TenantMembershipEntity;

/**
 * @author Ruben De Swaef
 */
public interface TenantMembershipDataManager extends DataManager<TenantMembershipEntity> {

    void deleteTenantMembership(String userId, String tenantId);

    void deleteTenantMembershipByTenantId(String tenantId);

    void deleteTenantMembershipByUserId(String userId);

}