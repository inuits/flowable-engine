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

package org.flowable.rest.service.api.management;

import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.ManagementService;
import org.flowable.job.api.DeadLetterJobQuery;
import org.flowable.job.api.Job;
import org.flowable.job.api.JobQuery;
import org.flowable.job.api.SuspendedJobQuery;
import org.flowable.job.api.TimerJobQuery;
import org.flowable.rest.service.api.BpmnRestApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Frederik Heremans
 * @author Joram Barrez
 * @author Tijs Rademakers
 */
public class JobBaseResource {

    @Autowired
    protected ManagementService managementService;
    
    @Autowired(required=false)
    protected BpmnRestApiInterceptor restApiInterceptor;

    protected Job getJobById(String jobId) {
        return getJobById(jobId, null);
    }
    
    protected Job getJobById(String jobId, String tenantId) {
        final JobQuery jobQuery = managementService.createJobQuery();
        if (tenantId != null) {
            jobQuery.jobTenantId(tenantId);
        }
        Job job = jobQuery.jobId(jobId).singleResult();
        validateJob(job, jobId);
        return job;
    }
    
    protected Job getTimerJobById(String jobId) {
        return getTimerJobById(jobId, null);
    }
    
    protected Job getTimerJobById(String jobId, String tenantId) {
        final TimerJobQuery timerJobQuery = managementService.createTimerJobQuery();
        if (tenantId != null) {
            timerJobQuery.jobTenantId(tenantId);
        }
        Job job = timerJobQuery.jobId(jobId).singleResult();
        validateJob(job, jobId);
        return job;
    }
    
    protected Job getSuspendedJobById(String jobId) {
        return getSuspendedJobById(jobId, null);
    }
    
    protected Job getSuspendedJobById(String jobId, String tenantId) {
        final SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();
        if (tenantId != null) {
            suspendedJobQuery.jobTenantId(tenantId);
        }
        Job job = suspendedJobQuery.jobId(jobId).singleResult();
        validateJob(job, jobId);
        return job;
    }

    protected Job getDeadLetterJobById(String jobId) {
        return getDeadLetterJobById(jobId, null);
    }
    
    protected Job getDeadLetterJobById(String jobId, String tenantId) {
        final DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();
        if (tenantId != null) {
            deadLetterJobQuery.jobTenantId(tenantId);
        }
        Job job = deadLetterJobQuery.jobId(jobId).singleResult();
        validateJob(job, jobId);
        return job;
    }
    
    protected void validateJob(Job job, String jobId) {
        if (job == null) {
            throw new FlowableObjectNotFoundException("Could not find a deadletter job with id '" + jobId + "'.", Job.class);
        }
        
        if (restApiInterceptor != null) {
            restApiInterceptor.accessJobInfoById(job);
        }
    }
}
