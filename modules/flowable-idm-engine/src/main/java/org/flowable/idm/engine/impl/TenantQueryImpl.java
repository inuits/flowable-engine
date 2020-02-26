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

package org.flowable.idm.engine.impl;

import java.util.Date;
import java.util.List;

import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.query.QueryCacheValues;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.common.engine.impl.query.AbstractQuery;
import org.flowable.idm.api.Tenant;
import org.flowable.idm.api.TenantQuery;
import org.flowable.idm.api.TenantQueryProperty;
import org.flowable.idm.engine.impl.util.CommandContextUtil;

/**
 * @author Ruben De Swaef
 */
public class TenantQueryImpl extends AbstractQuery<TenantQuery, Tenant> implements TenantQuery, QueryCacheValues {

    private static final long serialVersionUID = 1L;
    protected String id;
    protected List<String> ids;
    protected String name;
    protected String nameLike;
    protected String nameLikeIgnoreCase;
    protected String userId;
    protected List<String> userIds;

    public TenantQueryImpl() {
    }

    public TenantQueryImpl(CommandContext commandContext) {
        super(commandContext);
    }

    public TenantQueryImpl(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public TenantQuery tenantId(String id) {
        if (id == null) {
            throw new FlowableIllegalArgumentException("Provided id is null");
        }
        this.id = id;
        return this;
    }

    @Override
    public TenantQuery tenantIds(List<String> ids) {
        if (ids == null) {
            throw new FlowableIllegalArgumentException("Provided ids is null");
        }
        this.ids = ids;
        return this;
    }


    @Override
    public TenantQuery tenantName(String name) {
        if (name == null) {
            throw new FlowableIllegalArgumentException("Provided name is null");
        }
        this.name = name;
        return this;
    }

    @Override
    public TenantQuery tenantNameLike(String nameLike) {
        if (nameLike == null) {
            throw new FlowableIllegalArgumentException("Provided name is null");
        }
        this.nameLike = nameLike;
        return this;
    }

    @Override
    public TenantQuery tenantNameLikeIgnoreCase(String nameLikeIgnoreCase) {
        if (nameLikeIgnoreCase == null) {
            throw new FlowableIllegalArgumentException("Provided name is null");
        }
        this.nameLikeIgnoreCase = nameLikeIgnoreCase.toLowerCase();
        return this;
    }

    @Override
    public TenantQuery tenantMember(String userId) {
        if (userId == null) {
            throw new FlowableIllegalArgumentException("Provided userId is null");
        }
        this.userId = userId;
        return this;
    }

    @Override
    public TenantQuery tenantMembers(List<String> userIds) {
        if (userIds == null) {
            throw new FlowableIllegalArgumentException("Provided userIds is null");
        }
        this.userIds = userIds;
        return this;
    }


    // sorting //////////////////////////////////////////////////////////

    @Override
    public TenantQuery orderByTenantId() {
        return orderBy(TenantQueryProperty.TENANT_ID);
    }

    @Override
    public TenantQuery orderByTenantName() {
        return orderBy(TenantQueryProperty.NAME);
    }

    // results //////////////////////////////////////////////////////////

    @Override
    public long executeCount(CommandContext commandContext) {
         return CommandContextUtil.getTenantEntityManager(commandContext).findTenantCountByQueryCriteria(this);
    }

    @Override
    public List<Tenant> executeList(CommandContext commandContext) {
        return CommandContextUtil.getTenantEntityManager(commandContext).findTenantByQueryCriteria(this);
    }

    // getters //////////////////////////////////////////////////////////

    @Override
    public String getId() {
        return id;
    }

    public List<String> getIds() {
        return ids;
    }

    public String getName() {
        return name;
    }

    public String getNameLike() {
        return nameLike;
    }

    public String getNameLikeIgnoreCase() {
        return nameLikeIgnoreCase;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getUserIds() {
        return userIds;
    }
}
