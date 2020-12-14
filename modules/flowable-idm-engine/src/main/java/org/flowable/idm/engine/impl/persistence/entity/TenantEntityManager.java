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

import org.flowable.common.engine.impl.persistence.entity.EntityManager;
import org.flowable.idm.api.Tenant;
import org.flowable.idm.api.TenantQuery;
import org.flowable.idm.engine.impl.TenantQueryImpl;

/**
 * @author Ruben De Swaef
 */
public interface TenantEntityManager extends EntityManager<TenantEntity> {

    Tenant createNewTenant(String tenantId);

    TenantQuery createNewTenantQuery();

    List<Tenant> findTenantByQueryCriteria(TenantQueryImpl query);

    long findTenantCountByQueryCriteria(TenantQueryImpl query);

    List<Tenant> findTenantsByNativeQuery(Map<String, Object> parameterMap);

    long findTenantCountByNativeQuery(Map<String, Object> parameterMap);

    boolean isNewTenant(Tenant tenant);

    List<Tenant> findTenantsByUser(String userId);
}