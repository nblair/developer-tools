/*
 * Board of Regents of the University of Wisconsin System
 * licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
'use strict';

(function() {
	var app = angular.module('angularjs-project-support.build', []);

	app.directive('build', function() {
		return {
			restrict: 'E',
			scope: {
    			apiUrl: '@apiUrl'
    		},
			template: '<p class="text-muted pull-right">Revision {{buildNumber}} (Version {{projectVersion}}<span ng-show="scmBranch"> from {{scmBranch}} branch</span>)</p>',
			controller: ['$scope', '$http', function($scope, $http) {
				$http
					.get($scope.apiUrl)
					.success(function(data) {
						$scope.buildNumber = data.buildNumber;
						$scope.projectVersion= data.projectVersion;
						$scope.scmBranch = data.scmBranch;
					});
			}],
		}
	});
})();