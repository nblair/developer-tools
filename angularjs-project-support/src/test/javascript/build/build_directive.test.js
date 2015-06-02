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

describe("directive: build", function() {

	var element, scope, $compile_, $httpBackend;
	
	beforeEach(module('angularjs-project-support'));

	beforeEach(inject(function($rootScope, $compile,  _$httpBackend_) {
		scope = $rootScope.$new();
		
		$httpBackend = _$httpBackend_;
		$compile_ = $compile;
	}));
		
	function compileDirective() {
		element ='<build api-url="build"></build>';

		element = $compile_(element)(scope);
		$httpBackend.flush();
		scope.$digest();
    }
	
	it('should show branch when scmBranch available', function(){
		$httpBackend.when('GET', 'build').respond(
				{
					buildNumber: "f0b3539",
					scmBranch: "scmBranch-on-tag",
					projectVersion: "1.3.1-SNAPSHOT",
					timestamp: "22.04.2015 @ 13:46:15 CDT"
				});
		compileDirective();
		
		expect(element.find('span').text()).toBe(' from scmBranch-on-tag branch');
		expect(element.find('span').hasClass('ng-hide')).toBe(false);
	});
	it('should not show branch when scmBranch is null', function(){
		$httpBackend.when('GET', 'build').respond(
				{
					buildNumber: "f0b3539",
					scmBranch: null,
					projectVersion: "1.3.0",
					timestamp: "22.04.2015 @ 13:46:15 CDT"
				});
		compileDirective();
		
		expect(element.find('span').text()).toBe(' from  branch');
		expect(element.find('span').hasClass('ng-hide')).toBe(true);
	});
	it('should not show branch when scmBranch is blank', function(){
		$httpBackend.when('GET', 'build').respond(
				{
					buildNumber: "f0b3539",
					scmBranch: '',
					projectVersion: "1.3.0",
					timestamp: "22.04.2015 @ 13:46:15 CDT"
				});
		compileDirective();
		expect(element.find('span').text()).toBe(' from  branch');
		expect(element.find('span').hasClass('ng-hide')).toBe(true);
	});
});