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

import org.casbin.jcasbin.main.Enforcer;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

@WebFilter("/*")
public class JCasbinAuthzFilter implements Filter {
    static Enforcer enforcer;

    public void init(FilterConfig filterConfig) throws ServletException {
        enforcer = new Enforcer("examples/authz_model.conf", "examples/authz_policy.csv");
    }

    private String getUser(HttpServletRequest request) {
        String res = "";

        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            // credentials = "username:password"
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            final String[] values = credentials.split(":", 2);
            res = values[0];
        }

        return res;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String user = getUser(httpRequest);
        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        System.out.println("(" + user + ", " + path + ", " + method + ")");

        if (enforcer.enforce(user, path, method)) {
            filterChain.doFilter(request, response);
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    public void destroy() {
    }
}
