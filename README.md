jcasbin-springboot-plugin [![Build Status](https://travis-ci.org/jcasbin/jcasbin-springboot-plugin.svg?branch=master)](https://travis-ci.org/jcasbin/jcasbin-springboot-plugin) [![Coverage Status](https://coveralls.io/repos/github/jcasbin/jcasbin-springboot-plugin/badge.svg?branch=master)](https://coveralls.io/github/jcasbin/jcasbin-springboot-plugin?branch=master)
======

jcasbin-springboot-plugin is an authorization middleware for [Spring Boot](https://projects.spring.io/spring-boot/), it's based on [https://github.com/casbin/jcasbin](https://github.com/casbin/jcasbin). It is developed under the latest Spring Boot ``2.0.1`` and Java ``8``.

## Installation

    git clone https://github.com/jcasbin/jcasbin-springboot-plugin

## Simple Example

This project itself is a working SpringBoot project that integrates with jCasbin. The steps to use jCasbin in your own SpringBoot project are:

1. Copy the [JCasbinAuthzFilter](https://github.com/jcasbin/jcasbin-springboot-plugin/blob/master/src/main/java/org/jcasbin/plugins/JCasbinAuthzFilter.java) class to your own project.
2. Copy [authz_model.conf](https://github.com/jcasbin/jcasbin-springboot-plugin/blob/master/examples/authz_model.conf) and [authz_policy.csv](https://github.com/jcasbin/jcasbin-springboot-plugin/blob/master/examples/authz_policy.csv) to your project. You can modify them to your own jCasbin model and policy (or loading policy from DB), see [Model persistence](https://github.com/casbin/casbin/wiki/Model-persistence) and [Policy persistence](https://github.com/casbin/casbin/wiki/Policy-persistence).
2. Replace the [HttpBasicAuthnFilter](https://github.com/jcasbin/jcasbin-springboot-plugin/blob/master/src/main/java/org/jcasbin/plugins/HttpBasicAuthnFilter.java) class (which provides [HTTP basic authentication](https://en.wikipedia.org/wiki/Basic_access_authentication)) with your own authentication like OAuth, Apache Shiro, Spring Security, etc. Rewrite ``JCasbinAuthzFilter``'s [String getUser(HttpServletRequest request)](https://github.com/jcasbin/jcasbin-springboot-plugin/blob/master/src/main/java/org/jcasbin/plugins/JCasbinAuthzFilter.java#L35-L49) method to make sure jCasbin can get the authenticated user name.
3. Make sure the ``JCasbinAuthzFilter`` filter is loaded, so it can filter all your requests. To do this, you can copy the [WebComponentConfig](https://github.com/jcasbin/jcasbin-springboot-plugin/blob/master/src/main/java/org/jcasbin/plugins/WebComponentConfig.java) class into the same folder with ``JCasbinAuthzFilter``.

## Getting Help

- [jCasbin](https://github.com/casbin/jcasbin)

## License

This project is under Apache 2.0 License. See the [LICENSE](LICENSE) file for the full license text.
