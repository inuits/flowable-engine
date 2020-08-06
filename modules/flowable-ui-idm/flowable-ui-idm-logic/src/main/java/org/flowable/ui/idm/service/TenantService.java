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
package org.flowable.ui.idm.service;

import java.util.List;

import org.flowable.idm.api.Tenant;

/**
 * @author Ruben De Swaef
 */
public interface TenantService {

    List<Tenant> getTenants(String filter);

    Tenant getTenant(String tenantId);

    Tenant createNewTenant(String tenantId, String name);

    Tenant updateTenantName(String tenantId, String name);

    void deleteTenant(String tenantId);

    void addTenantMember(String tenantId, String userId);

    void deleteTenantMember(String tenantId, String userId);

    List<Tenant> getTenantsForUser(String userId);
}
