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

import org.apache.commons.lang3.StringUtils;
import org.flowable.idm.api.Tenant;
import org.flowable.idm.api.TenantQuery;
import org.flowable.idm.api.User;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.flowable.ui.common.service.exception.ConflictingRequestException;
import org.flowable.ui.common.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ruben De Swaef
 */
@Service
@Transactional
public class TenantServiceImpl extends AbstractIdmService implements TenantService {

    @Override
    public List<Tenant> getTenants(String filter) {
        TenantQuery tenantQuery = identityService.createTenantQuery();
        if (StringUtils.isNotEmpty(filter)) {
            tenantQuery.tenantNameLikeIgnoreCase("%" + (filter != null ? filter : "") + "%");
        }
        return tenantQuery.orderByTenantName().asc().list();
    }

    public Tenant getTenant(String tenantId) {
        return identityService.createTenantQuery().tenantId(tenantId).singleResult();
    }

    public Tenant updateTenantName(String tenantId, String name) {
        Tenant tenant = identityService.createTenantQuery().tenantId(tenantId).singleResult();
        if (tenant != null) {
            tenant.setName(name);
            identityService.saveTenant(tenant);
        }
        return tenant;
    }

    public void deleteTenant(String tenantId) {
        Tenant tenant = identityService.createTenantQuery().tenantId(tenantId).singleResult();
        if (tenant == null) {
            throw new NotFoundException();
        }
        identityService.deleteTenant(tenantId);
    }

    @Override
    public Tenant createNewTenant(String tenantId, String name) {
        if (StringUtils.isBlank(tenantId) ||
            StringUtils.isBlank(name)) {
            throw new BadRequestException("Id and name are required");
        }

        if (identityService.createTenantQuery().tenantId(tenantId).count() > 0) {
            throw new ConflictingRequestException("Tenant already registered", "ACCOUNT.SIGNUP.ERROR.ALREADY-REGISTERED");
        }

        Tenant tenant = identityService.newTenant(tenantId);
        tenant.setName(name);
        identityService.saveTenant(tenant);

        return tenant;
    }

    public void addTenantMember(String tenantId, String userId) {
        verifyTenantMemberExists(tenantId, userId);
        Tenant tenant = identityService.createTenantQuery().tenantId(tenantId).singleResult();
        if (tenant == null) {
            throw new NotFoundException();
        }

        User user = identityService.createUserQuery().userId(userId).singleResult();
        if (user == null) {
            throw new NotFoundException();
        }

        identityService.createTenantMembership(userId, tenantId);
    }

    public void deleteTenantMember(String tenantId, String userId) {
        verifyTenantMemberExists(tenantId, userId);
        Tenant tenant = identityService.createTenantQuery().tenantId(tenantId).singleResult();
        if (tenant == null) {
            throw new NotFoundException();
        }

        User user = identityService.createUserQuery().userId(userId).singleResult();
        if (user == null) {
            throw new NotFoundException();
        }

        identityService.deleteTenantMembership(userId, tenantId);
    }

    protected void verifyTenantMemberExists(String tenantId, String userId) {
        // Check existence
        Tenant tenant = identityService.createTenantQuery().tenantId(tenantId).singleResult();
        User user = identityService.createUserQuery().userId(userId).singleResult();
        for (User tenantMember : identityService.createUserQuery().memberOfTenant(tenantId).list()) {
            if (tenantMember.getId().equals(userId)) {
                user = tenantMember;
            }
        }

        if (tenant == null || user == null) {
            throw new NotFoundException();
        }
    }

    @Override
    public List<Tenant> getTenantsForUser(String userId) {
        return identityService.createTenantQuery().tenantMember(userId).list();
    }

}
