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
package org.flowable.ui.common.rest.idm.remote;

import java.util.ArrayList;
import java.util.List;

import org.flowable.ui.common.model.GroupRepresentation;
import org.flowable.ui.common.model.TenantRepresentation;
import org.flowable.ui.common.model.RemoteGroup;
import org.flowable.ui.common.model.RemoteTenant;
import org.flowable.ui.common.model.RemoteUser;
import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.common.service.exception.NotFoundException;
import org.flowable.ui.common.service.idm.RemoteIdmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.flowable.idm.api.User;
import org.flowable.idm.api.IdmIdentityService;

@RestController
@RequestMapping("/app")
public class RemoteAccountResource {

    @Autowired
    private RemoteIdmService remoteIdmService;

    /**
     * GET /rest/account -> get the current user.
     */
    @GetMapping(value = "/rest/account", produces = "application/json")
    public UserRepresentation getAccount() {
        UserRepresentation userRepresentation = null;
        String currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId != null) {
            RemoteUser remoteUser = remoteIdmService.getUser(currentUserId);
            if (remoteUser != null) {
                userRepresentation = new UserRepresentation(remoteUser);

                if (remoteUser.getGroups() != null && remoteUser.getGroups().size() > 0) {
                    List<GroupRepresentation> groups = new ArrayList<>();
                    for (RemoteGroup remoteGroup : remoteUser.getGroups()) {
                        groups.add(new GroupRepresentation(remoteGroup));
                    }
                    userRepresentation.setGroups(groups);
                }

                if (remoteUser.getPrivileges() != null && remoteUser.getPrivileges().size() > 0) {
                    userRepresentation.setPrivileges(remoteUser.getPrivileges());
                }

                if (remoteUser.getTenants() != null && remoteUser.getTenants().size() > 0) {
                    List<TenantRepresentation> tenants = new ArrayList<>();
                    for (RemoteTenant remoteTenant : remoteUser.getTenants()) {
                        tenants.add(new TenantRepresentation(remoteTenant));
                    }
                    userRepresentation.setTenants(tenants);
                }
            }
        }

        if (userRepresentation != null) {
            return userRepresentation;
        } else {
            throw new NotFoundException();
        }
    }

    /**
     * PUT /rest/account/tenant -> changing the currently used tenant.
     */
    @PutMapping(value = "/rest/account/tenant", produces = "application/json")
    public User putTenantAccount(@RequestBody RemoteUser user) {
        User currentUser = SecurityUtils.getCurrentUserObject();
        if (currentUser != null) {
            RemoteUser remoteUser = remoteIdmService.getUser(currentUser.getId());
            if (remoteUser.getTenants() != null && remoteUser.getTenants().size() > 0) {
                for (RemoteTenant remoteTenant : remoteUser.getTenants()) {
                    if(remoteTenant.getId().equals(user.getTenantId())) {
                        currentUser.setTenantId(user.getTenantId());
                    }
                }
            }
        } else {
            throw new NotFoundException();
        }
        return currentUser;
    }

}
