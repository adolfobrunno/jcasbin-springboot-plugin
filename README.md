jcasbin-springboot-plugin [![Build Status](https://travis-ci.org/jcasbin/jcasbin-springboot-plugin.svg?branch=master)](https://travis-ci.org/jcasbin/jcasbin-springboot-plugin) [![Coverage Status](https://coveralls.io/repos/github/jcasbin/jcasbin-springboot-plugin/badge.svg?branch=master)](https://coveralls.io/github/jcasbin/jcasbin-springboot-plugin?branch=master)
======

jcasbin-springboot-plugin is an authorization middleware for [Spring Boot](https://projects.spring.io/spring-boot/), it's based on [https://github.com/casbin/jcasbin](https://github.com/casbin/jcasbin).

## Installation

    git clone https://github.com/jcasbin/jcasbin-springboot-plugin

## Simple Example

This project itself is a working SpringBoot project that integrates with jCasbin. The steps to use jCasbin in your own SpringBoot project are:

1. Copy the ``JCasbinAuthzFilter`` class to your own project, customize your own jCasbin model file and policy file (or DB).
2. Replace the ``HttpBasicAuthnFilter`` class (provides HTTP basic authentication) with your own authentication like OAuth, Apache Shiro, Spring Security, etc. Rewrite ``JCasbinAuthzFilter``'s ``String getUser(HttpServletRequest request)`` method to make sure jCasbin can get the authenticated user name.
3. Make sure the ``JCasbinAuthzFilter`` filter is loaded. For example, you can copy the ``WebComponentConfig`` class into the same folder with ``JCasbinAuthzFilter``.

## Getting Help

- [jCasbin](https://github.com/casbin/jcasbin)

## License

This project is under Apache 2.0 License. See the [LICENSE](LICENSE) file for the full license text.
