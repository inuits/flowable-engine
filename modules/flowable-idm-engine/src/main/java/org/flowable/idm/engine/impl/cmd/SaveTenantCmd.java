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
package org.flowable.idm.engine.impl.cmd;

import java.io.Serializable;

import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.common.engine.impl.persistence.entity.Entity;
import org.flowable.idm.api.Tenant;
import org.flowable.idm.engine.impl.persistence.entity.TenantEntity;
import org.flowable.idm.engine.impl.util.CommandContextUtil;

/**
 * @author Ruben De Swaef
 */
public class SaveTenantCmd implements Command<Void>, Serializable {

    private static final long serialVersionUID = 1L;
    protected Tenant tenant;

    public SaveTenantCmd(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        if (tenant == null) {
            throw new FlowableIllegalArgumentException("tenant is null");
        }

        if (CommandContextUtil.getTenantEntityManager(commandContext).isNewTenant(tenant)) {
            if (tenant instanceof TenantEntity) {
                CommandContextUtil.getTenantEntityManager(commandContext).insert((TenantEntity) tenant);
            } else {
                CommandContextUtil.getDbSqlSession(commandContext).insert((Entity) tenant);
            }
        } else {
            if (tenant instanceof TenantEntity) {
                CommandContextUtil.getTenantEntityManager(commandContext).update((TenantEntity) tenant);
            } else {
                CommandContextUtil.getDbSqlSession(commandContext).update((Entity) tenant);
            }

        }
        return null;
    }

}
