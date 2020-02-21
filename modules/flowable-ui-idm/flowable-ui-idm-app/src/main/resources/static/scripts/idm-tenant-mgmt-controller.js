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
/**
 * Controller for tenant mgmt
 */
flowableApp.controller('TenantMgmtController', ['$rootScope', '$scope', '$translate', '$http', '$timeout','$location', '$modal', '$popover', 'IdmService',
    function ($rootScope, $scope, $translate, $http, $timeout, $location, $modal, $popover, IdmService) {

        $rootScope.setMainPageById('tenantMgmt');

        $scope.model = {
            loading: false,
            selectedTenants: {},
            selectedTenantCount: 0
        };

        $scope.showCreateTenantPopup = function() {
            $scope.model.editedTenant  = {};
            $scope.model.mode = 'create';
            _internalCreateModal({
                scope: $scope,
                template: 'views/popup/idm-tenant-create.html',
                show: true
            }, $modal, $scope);
        };

        $scope.createTenant = function() {
            $scope.model.loading = true;
            IdmService.createTenant($scope.model.editedTenant).then(function (data) {
                $scope.fetchTenants(data.id);
                $scope.model.loading = false;
            });
        };

        $scope.updateTenant = function() {
            $scope.model.loadingTenant = true;
            IdmService.updateTenant($scope.model.editedTenant.id, $scope.model.editedTenant).then(function (data) {
                $scope.model.selectedTenant = data;

                // Find the entry in the list on the left, and update its name
                for (var i=0; i<$scope.model.tenants.length; i++){
                    if ($scope.model.tenants[i].id === data.id) {
                        $scope.model.tenants[i].name = data.name;
                    }
                }

                $scope.model.loadingTenant = false;
            });
        };

        $scope.editTenantDetails = function() {

            $scope.model.tenant = undefined;
            $scope.model.mode = 'edit';
            var selectedTenants = $scope.getSelectedTenants();
            if (selectedTenants && selectedTenants.length == 1) {
                $scope.model.tenant = selectedTenants[0];
            }

            $scope.model.errorMessage = undefined;
            _internalCreateModal({
                scope: $scope,
                template: 'views/popup/idm-tenant-create.html?version=' + new Date().getTime(),
                show: true
            }, $modal, $scope);
        };

        $scope.deleteTenants = function() {
            $scope.model.loading = true;
            $scope.getSelectedTenants().forEach(function(selectedTenant) {
                $http({method: 'DELETE', url: FLOWABLE.CONFIG.contextRoot + '/app/rest/admin/tenants/' + selectedTenant.id}).
                    success(function (data, status, headers, config) {

                        $rootScope.addAlert('Tenant deleted', 'info');
                        $scope.loadTenants();

                        $scope.model.loading = false;
                    }).
                    error(function (data, status, headers, config) {
                        $scope.model.loading = false;
                        if (data && data.message) {
                            $rootScope.addAlert(data.message, 'error');
                        } else {
                            $rootScope.addAlert('Error while deleting tenant', 'error');
                        }
                    });
            });
        };

        $scope.clearSelectedTenants = function() {
            $scope.model.selectedTenants = {};
            $scope.model.selectedTenantCount = 0;
        };

        $scope.loadTenants = function() {
            $scope.clearSelectedTenants();
            $scope.model.loading = true;
            var params = {
                filter: $scope.model.pendingFilterText,
            };

            $http({method: 'GET', url: FLOWABLE.CONFIG.contextRoot + '/app/rest/admin/tenants', params: params}).
                success(function(data, status, headers, config) {
                    $scope.model.tenants = data;
                    $scope.model.loading = false;
                }).
                error(function(data, status, headers, config) {
                    $scope.model.loading = false;
                });
        };

        $scope.toggleTenantSelection = function(tenant) {
            if($scope.model.selectedTenants[tenant.id]) {
                delete $scope.model.selectedTenants[tenant.id];
                $scope.model.selectedTenantCount -= 1;
            }  else {
                $scope.model.selectedTenants[tenant.id] = true;
                $scope.model.selectedTenantCount +=1;
            }

        };

        $scope.getSelectedTenants = function() {
            var selected = [];
            for(var i = 0; i<$scope.model.tenants.size; i++) {
                var tenant = $scope.model.tenants.data[i];
                if(tenant) {
                    for(var prop in $scope.model.selectedTenants) {
                        if(tenant.id == prop) {
                            selected.push(tenant);
                            break;
                        }
                    }
                }
            }

            return selected;
        };

        $scope.loadTenants();

    }]);

