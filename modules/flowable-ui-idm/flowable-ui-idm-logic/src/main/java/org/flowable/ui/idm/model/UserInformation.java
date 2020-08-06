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
package org.flowable.ui.idm.model;

import java.util.List;

import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.idm.api.Tenant;

public class UserInformation {

    protected User user;
    protected List<Group> groups;
    protected List<String> privileges;
    protected List<Tenant> tenants;

    public UserInformation() {

    }

    public UserInformation(User user, List<Group> groups, List<String> privileges, List<Tenant> tenants) {
        this.user = user;
        this.groups = groups;
        this.privileges = privileges;
        this.tenants = tenants;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }

    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

}
