// Copyright 2018 The casbin Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.jcasbin.plugins;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class SpringbootPluginTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private void testAuthzRequest(String user, String path, String method, int code) {
        try {
            // We use HTTP basic authentication for authentication in this test client.
            // Username is user.
            // Password is "123".
            // You can customize your own authentication like OAuth, Apache Shiro, Spring Security, etc.
            String plainCredentials = user + ":123";
            String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + base64Credentials);

            ResponseEntity<String> result = testRestTemplate.exchange(path, HttpMethod.resolve(method), new HttpEntity<>(headers), String.class);
            int myCode = result.getStatusCodeValue();
            if (myCode != code) {
                fail(String.format("%s, %s, %s: %d, supposed to be %d", user, path, method, myCode, code));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testbasic() {
        // Here we use HTTP basic authentication as the way to get the logged-in user name
        // For simplicity, the credential is not verified, you should implement and use your own
        // authentication before the authorization.
        // In this example, we assume "alice:123" is a legal user.

        testAuthzRequest("alice", "/dataset1/resource1", "GET", 200);
        testAuthzRequest("alice", "/dataset1/resource1", "POST", 200);
        testAuthzRequest("alice", "/dataset1/resource2", "GET", 200);
        testAuthzRequest("alice", "/dataset1/resource2", "POST", 403);
    }

    @Test
    public void testPathWildcard() {
        // Here we use HTTP basic authentication as the way to get the logged-in user name
        // For simplicity, the credential is not verified, you should implement and use your own
        // authentication before the authorization.
        // In this example, we assume "bob:123" is a legal user.

        testAuthzRequest("bob", "/dataset2/resource1", "GET", 200);
        testAuthzRequest("bob", "/dataset2/resource1", "POST", 200);
        testAuthzRequest("bob", "/dataset2/resource1", "DELETE", 200);
        testAuthzRequest("bob", "/dataset2/resource2", "GET", 200);
        testAuthzRequest("bob", "/dataset2/resource2", "POST", 403);
        testAuthzRequest("bob", "/dataset2/resource2", "DELETE", 403);

        testAuthzRequest("bob", "/dataset2/folder1/item1", "GET", 403);
        testAuthzRequest("bob", "/dataset2/folder1/item1", "POST", 200);
        testAuthzRequest("bob", "/dataset2/folder1/item1", "DELETE", 403);
        testAuthzRequest("bob", "/dataset2/folder1/item2", "GET", 403);
        testAuthzRequest("bob", "/dataset2/folder1/item2", "POST", 200);
        testAuthzRequest("bob", "/dataset2/folder1/item2", "DELETE", 403);
    }

    @Test
    public void testRBAC() {
        // Here we use HTTP basic authentication as the way to get the logged-in user name
        // For simplicity, the credential is not verified, you should implement and use your own
        // authentication before the authorization.
        // In this example, we assume "cathy:123" is a legal user.

        // cathy can access all /dataset1/* resources via all methods because it has the dataset1_admin role.
        testAuthzRequest("cathy", "/dataset1/item", "GET", 200);
        testAuthzRequest("cathy", "/dataset1/item", "POST", 200);
        testAuthzRequest("cathy", "/dataset1/item", "DELETE", 200);
        testAuthzRequest("cathy", "/dataset2/item", "GET", 403);
        testAuthzRequest("cathy", "/dataset2/item", "POST", 403);
        testAuthzRequest("cathy", "/dataset2/item", "DELETE", 403);

        // delete all roles on user cathy, so cathy cannot access any resources now.
        JCasbinAuthzFilter.enforcer.deleteRolesForUser("cathy");

        testAuthzRequest("cathy", "/dataset1/item", "GET", 403);
        testAuthzRequest("cathy", "/dataset1/item", "POST", 403);
        testAuthzRequest("cathy", "/dataset1/item", "DELETE", 403);
        testAuthzRequest("cathy", "/dataset2/item", "GET", 403);
        testAuthzRequest("cathy", "/dataset2/item", "POST", 403);
        testAuthzRequest("cathy", "/dataset2/item", "DELETE", 403);
    }
}
