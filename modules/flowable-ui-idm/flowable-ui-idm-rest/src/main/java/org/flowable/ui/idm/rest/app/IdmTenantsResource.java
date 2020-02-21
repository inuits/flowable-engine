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

import org.flowable.idm.api.Tenant;
import org.flowable.ui.common.model.TenantRepresentation;
import org.flowable.ui.idm.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author Ruben De Swaef
 */
@RestController
@RequestMapping(value = "/app/rest/admin/tenants")
public class IdmTenantsResource {

    @Autowired
    private TenantService tenantService;

    @GetMapping()
    public List<TenantRepresentation> getTenants(@RequestParam(required = false) String filter) {
        List<TenantRepresentation> result = new ArrayList<>();
        for (Tenant tenant : tenantService.getTenants(filter)) {
            result.add(new TenantRepresentation(tenant));
        }
        return result;
    }

    @GetMapping(value = "/{tenantId}")
    public TenantRepresentation getTenant(@PathVariable String tenantId) {
        return new TenantRepresentation(tenantService.getTenant(tenantId));
    }

    @PostMapping()
    public TenantRepresentation createNewTenant(@RequestBody TenantRepresentation tenantRepresentation) {
        return new TenantRepresentation(tenantService.createNewTenant(tenantRepresentation.getId(), tenantRepresentation.getName()));
    }

    @PutMapping(value = "/{tenantId}")
    public TenantRepresentation updateTenant(@PathVariable String tenantId, @RequestBody TenantRepresentation tenantRepresentation) {
        return new TenantRepresentation(tenantService.updateTenantName(tenantId, tenantRepresentation.getName()));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = "/{tenantId}")
    public void deleteTenant(@PathVariable String tenantId) {
        tenantService.deleteTenant(tenantId);
    }
}
