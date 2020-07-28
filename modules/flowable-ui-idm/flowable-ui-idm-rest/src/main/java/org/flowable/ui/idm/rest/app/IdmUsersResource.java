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
package org.flowable.ui.idm.rest.app;

import org.flowable.idm.api.User;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.model.TenantRepresentation;
import org.flowable.ui.idm.model.CreateUserRepresentation;
import org.flowable.ui.idm.model.UpdateUsersRepresentation;
import org.flowable.ui.idm.service.UserService;
import org.flowable.ui.idm.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Frederik Heremans
 * @author Joram Barrez
 * @author Ruben De Swaef
 */
@RestController
@RequestMapping("/app")
public class IdmUsersResource {

    @Autowired
    protected UserService userService;

    @Autowired
    protected TenantService tenantService;

    @Value("${flowable.admin.app.security.tenant-mapping:#{null}}")
    private Boolean tenantMapping;

    @GetMapping(value = "/rest/admin/users")
    public ResultListDataRepresentation getUsers(
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) String groupId) {

        int startValue = start != null ? start.intValue() : 0;

        List<User> users = userService.getUsers(filter, sort, start);
        ResultListDataRepresentation result = new ResultListDataRepresentation();
        result.setTotal(userService.getUserCount(filter, sort, startValue, groupId));
        result.setStart(startValue);
        result.setSize(users.size());
        result.setData(convertToUserRepresentations(users));
        return result;
    }

    protected List<UserRepresentation> convertToUserRepresentations(List<User> users) {
        List<UserRepresentation> result = new ArrayList<>(users.size());
        for (User user : users) {
            UserRepresentation ur = new UserRepresentation(user);
            if(tenantMapping){
                tenantService.getTenantsForUser(user.getId()).forEach(
                    (tenant) -> {
                        ur.getTenants().add(new TenantRepresentation(tenant));
                    }
                );
            }
            result.add(ur);
        }
        return result;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = "/rest/admin/users/{userId}")
    public void updateUserDetails(@PathVariable String userId, @RequestBody UpdateUsersRepresentation updateUsersRepresentation) {
        String tenantId = null;
        if(tenantMapping){
            if(!updateUsersRepresentation.getTenants().isEmpty()){
                tenantId = updateUsersRepresentation.getTenants().get(0);
            }
        } else {
            tenantId = updateUsersRepresentation.getTenantId();
        }
        userService.updateUserDetails(userId, updateUsersRepresentation.getFirstName(),
                updateUsersRepresentation.getLastName(),
                updateUsersRepresentation.getEmail(),
                tenantId
                );

        if(tenantMapping){
            tenantService.getTenantsForUser(userId).forEach(
                (tenant) -> {
                    tenantService.deleteTenantMember(tenant.getId(), userId);
                }
            );
            updateUsersRepresentation.getTenants().forEach(
                (tenant) -> {
                    tenantService.addTenantMember(tenant, userId);
                }
            );
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = "/rest/admin/users")
    public void bulkUpdateUserDetails(@RequestBody UpdateUsersRepresentation updateUsersRepresentation) {
        userService.bulkUpdatePassword(updateUsersRepresentation.getUsers(), updateUsersRepresentation.getPassword());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = "/rest/admin/users/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }

    @PostMapping(value = "/rest/admin/users")
    public UserRepresentation createNewUser(@RequestBody CreateUserRepresentation userRepresentation) {
        String tenantId = null;
        if(tenantMapping){
            if(!userRepresentation.getTenants().isEmpty()){
                tenantId = userRepresentation.getTenantIds().get(0);
            }
        } else {
            tenantId = userRepresentation.getTenantId();
        }
        UserRepresentation ur = new UserRepresentation(userService.createNewUser(
            userRepresentation.getId(),
            userRepresentation.getFirstName(),
            userRepresentation.getLastName(),
            userRepresentation.getEmail(),
            userRepresentation.getPassword(),
            tenantId
            ));

        if(tenantMapping){
            userRepresentation.getTenantIds().forEach(
                (tenant) -> {
                    tenantService.addTenantMember(tenant, userRepresentation.getId());
                    userRepresentation.getTenants().add(new TenantRepresentation(tenantService.getTenant(tenant)));
                }
            );
        }

        return ur;
    }

}
