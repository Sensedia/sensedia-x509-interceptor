# x509 Interceptor

This interceptor help us to extract and manipulate information about x509 certificates when it is used in a mTLS connection.

Since API Platform 4.3.4.0, in scenarios of use mTLS inbound address, we can obatain information about the x509 certificate client through x-forwarded-client-cert header in all requests (it is true if the handshake TLS is successfully completed). The following keys are supported by x-forwarded-client-cert:

* <span style="color:orange;">By</span>: The Subject Alternative Name (URI type) of the current client certificate.
* <span style="color:orange;">Hash</span>: The SHA 256 digest of the current client certificate.
* <span style="color:orange;">Cert</span>: The entire client certificate in URL encoded PEM format.
* <span style="color:orange;">Chain</span>: The entire client certificate chain (including the leaf certificate) in URL encoded PEM format.
* <span style="color:orange;">Subject</span>: The Subject field of the current client certificate. The value is always double-quoted.
* <span style="color:orange;">URI</span>: The URI type Subject Alternative Name field of the current client certificate.
* <span style="color:orange;">DNS</span>: The DNS type Subject Alternative Name field of the current client certificate. A client certificate may contain multiple DNS type Subject Alternative Names, each will be a separate key-value pair.


Here is an example of x-forwarded-client-cert in a real call:

```sh 
x-forwarded-client-cert: Hash=0e163506d1135fab55d4006ef687827d5840d820b2b96fbb86669906db588f28;Cert="-----BEGIN%20CERTIFICATE-----%0AMIIF2jCCA8KgAwIBAgIJALa0N9eujNjtMA0GCSqGSIb3DQEBCwUAMIGmMQswCQYD%0AVQQGEwJCUjESMBAGA1UECAwJU2FvIFBhdWxvMREwDwYDVQQHDAhDYW1waW5hczER%0AMA8GA1UECgwIU2Vuc2VkaWExGTAXBgNVBAsMEFByb2R1Y3QgRW5naW5lZXIxFzAV%0ABgNVBAMMDk15IE93biBSb290IENBMSkwJwYJKoZIhvcNAQkBFhptYXJpby5tYW5j%0AdXNvQHNlbnNlZGlhLmNvbTAeFw0yMDA5MjgxMjQyNDVaFw0yMzAxMDExMjQyNDVa%0AMIGjMQswCQYDVQQGEwJCUjESMBAGA1UECAwJU2Fvfollows an example of x-bla in a real callIFBhdWxvMREwDwYDVQQHDAhD%0AYW1waW5hczERMA8GA1UECgwIU2Vuc2VkaWExCzAJBgNVBAsMAlBFMSIwIAYDVQQD%0ADBlhcGktdGVzdGluZzEuc2Vuc2VkaWEuY29tMSkwJwYJKoZIhvcNAQkBFhptYXJp%0Aby5tYW5jdXNvQHNlbnNlZGlhLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC%0AAQoCggEBAK9covQZij0Pr8QgEx%2BDXpbV%2BD4nynKJz5m9nqlpVFDqAOjJKAi4SJP%2B%0AAKGoFASMULwKCSyokrttSBlJEH1D5A5tXk6F21WMHHgocf0M1lzUyJ6SU1vyRNQU%0AEnTpWfIMVsehb6QjmHxi4mY7Eni0by474QSKvq%2F4jDkInPXk9CGrzLjJuI056Nwa%0AQs%2BXXnBWEBKvo9golQ759QCNnOA53Hx4cabU9ODQ%2BYK3TN%2FtLTQcjz6typg91GuM%0Ae0DDL%2Fg81rOZOiBt8CBpyO83FZmGPg5avHIqqjYGkU1Ng%2F2xh0pFnOlwAqvGR5Ns%0AhBUyrNeWPas%2B9igqPXPuk2LLyA2NqJ8CAwEAAaOCAQowggEGMIHFBgNVHSMEgb0w%0AgbqhgaykgakwgaYxCzAJBgNVBAYTAkJSMRIwEAYDVQQIDAlTYW8gUGF1bG8xETAP%0ABgNVBAcMCENhbXBpbmFzMREwDwYDVQQKDAhTZW5zZWRpYTEZMBcGA1UECwwQUHJv%0AZHVjdCBFbmdpbmVlcjEXMBUGA1UEAwwOTXkgT3duIFJvb3QgQ0ExKTAnBgkqhkiG%0A9w0BCQEWGm1hcmlvLm1hbmN1c29Ac2Vuc2VkaWEuY29tggkA4NsmXODel%2BAwCQYD%0AVR0TBAIwADALBgNVHQ8EBAMCBPAwJAYDVR0RBB0wG4IZYXBpLXRlc3RpbmcxLnNl%0AbnNlZGlhLmNvbTANBgkqhkiG9w0BAQsFAAOCAgEAlZDbWff2pv5RdOUsyYZUD0TO%0AJLKwVTAdZfMg6d65BnDrZl0XiestxuuxDzWelHYg0Jd3cvS2VrhlSysIhJA1NdD8%0AlXrtwoJhLS8RbEPLOcsvHkSY%2B8J7XqTMhtEMCJR3us8DaZH2XX5qpVuW3Q678q1k%0AxsfC6hI4XPpTDB%2BRE8PH%2BkZynczVHuaSUdq8weq4%2BrORJxX7Xz%2BsBsrVjRKn7J9V%0AjZxsckMFj0xtNaj6tGrPEYaL7HBJvFWMN5fXuwQBhH%2BLq4xqtlN%2FeRVQUaMmX2zy%0ALdnUUJwZolFhXJessZ%2B240RjTBjRiG9qatMnA5JBdobILwT4EDE2LekICDOhmKm0%0AaP9QCEGI%2By5dzmDWU7fkZJE06%2FJ6FSMGOTQF7ZctLpZ42q073y%2BuMpb9emQK7%2FUz%0Asdy0vqPTdZFFbNcx2jdSNh%2FyVR%2BNZS0r9PVR0TenE41R%2Blx0FT0XXMliAaNeoYVv%0AhwQUlqF9Vy2PtnHLqAQIuuP7W7SBEtAXhek9qZsl4Z3QENVhxCSylI4OoHmETxRg%0APbWN7BhAYbSWPR8IMl%2Fl7VpgooPWXCSY7q35zcovG%2BxzzZaCB%2BXGod9VvHfgkSVB%0A1KgNGZSx9yFOw%2FygKu1MCDFXWWXVnUMX2gwUWTKpTG4o4VrsmjlFyvxP13mAnqMQ%0AIenAp7fmMAxbZKWdGqQ%3D%0A-----END%20CERTIFICATE-----%0A";Chain="-----BEGIN%20CERTIFICATE-----%0AMIIF2jCCA8KgAwIBAgIJALa0N9eujNjtMA0GCSqGSIb3DQEBCwUAMIGmMQswCQYD%0AVQQGEwJCUjESMBAGA1UECAwJU2FvIFBhdWxvMREwDwYDVQQHDAhDYW1waW5hczER%0AMA8GA1UECgwIU2Vuc2VkaWExGTAXBgNVBAsMEFByb2R1Y3QgRW5naW5lZXIxFzAV%0ABgNVBAMMDk15IE93biBSb290IENBMSkwJwYJKoZIhvcNAQkBFhptYXJpby5tYW5j%0AdXNvQHNlbnNlZGlhLmNvbTAeFw0yMDA5MjgxMjQyNDVaFw0yMzAxMDExMjQyNDVa%0AMIGjMQswCQYDVQQGEwJCUjESMBAGA1UECAwJU2FvIFBhdWxvMREwDwYDVQQHDAhD%0AYW1waW5hczERMA8GA1UECgwIU2Vuc2VkaWExCzAJBgNVBAsMAlBFMSIwIAYDVQQD%0ADBlhcGktdGVzdGluZzEuc2Vuc2VkaWEuY29tMSkwJwYJKoZIhvcNAQkBFhptYXJp%0Aby5tYW5jdXNvQHNlbnNlZGlhLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC%0AAQoCggEBAK9covQZij0Pr8QgEx%2BDXpbV%2BD4nynKJz5m9nqlpVFDqAOjJKAi4SJP%2B%0AAKGoFASMULwKCSyokrttSBlJEH1D5A5tXk6F21WMHHgocf0M1lzUyJ6SU1vyRNQU%0AEnTpWfIMVsehb6QjmHxi4mY7Eni0by474QSKvq%2F4jDkInPXk9CGrzLjJuI056Nwa%0AQs%2BXXnBWEBKvo9golQ759QCNnOA53Hx4cabU9ODQ%2BYK3TN%2FtLTQcjz6typg91GuM%0Ae0DDL%2Fg81rOZOiBt8CBpyO83FZmGPg5avHIqqjYGkU1Ng%2F2xh0pFnOlwAqvGR5Ns%0AhBUyrNeWPas%2B9igqPXPuk2LLyA2NqJ8CAwEAAaOCAQowggEGMIHFBgNVHSMEgb0w%0AgbqhgaykgakwgaYxCzAJBgNVBAYTAkJSMRIwEAYDVQQIDAlTYW8gUGF1bG8xETAP%0ABgNVBAcMCENhbXBpbmFzMREwDwYDVQQKDAhTZW5zZWRpYTEZMBcGA1UECwwQUHJv%0AZHVjdCBFbmdpbmVlcjEXMBUGA1UEAwwOTXkgT3duIFJvb3QgQ0ExKTAnBgkqhkiG%0A9w0BCQEWGm1hcmlvLm1hbmN1c29Ac2Vuc2VkaWEuY29tggkA4NsmXODel%2BAwCQYD%0AVR0TBAIwADALBgNVHQ8EBAMCBPAwJAYDVR0RBB0wG4IZYXBpLXRlc3RpbmcxLnNl%0AbnNlZGlhLmNvbTANBgkqhkiG9w0BAQsFAAOCAgEAlZDbWff2pv5RdOUsyYZUD0TO%0AJLKwVTAdZfMg6d65BnDrZl0XiestxuuxDzWelHYg0Jd3cvS2VrhlSysIhJA1NdD8%0AlXrtwoJhLS8RbEPLOcsvHkSY%2B8J7XqTMhtEMCJR3us8DaZH2XX5qpVuW3Q678q1k%0AxsfC6hI4XPpTDB%2BRE8PH%2BkZynczVHuaSUdq8weq4%2BrORJxX7Xz%2BsBsrVjRKn7J9V%0AjZxsckMFj0xtNaj6tGrPEYaL7HBJvFWMN5fXuwQBhH%2BLq4xqtlN%2FeRVQUaMmX2zy%0ALdnUUJwZolFhXJessZ%2B240RjTBjRiG9qatMnA5JBdobILwT4EDE2LekICDOhmKm0%0AaP9QCEGI%2By5dzmDWU7fkZJE06%2FJ6FSMGOTQF7ZctLpZ42q073y%2BuMpb9emQK7%2FUz%0Asdy0vqPTdZFFbNcx2jdSNh%2FyVR%2BNZS0r9PVR0TenE41R%2Blx0FT0XXMliAaNeoYVv%0AhwQUlqF9Vy2PtnHLqAQIuuP7W7SBEtAXhek9qZsl4Z3QENVhxCSylI4OoHmETxRg%0APbWN7BhAYbSWPR8IMl%2Fl7VpgooPWXCSY7q35zcovG%2BxzzZaCB%2BXGod9VvHfgkSVB%0A1KgNGZSx9yFOw%2FygKu1MCDFXWWXVnUMX2gwUWTKpTG4o4VrsmjlFyvxP13mAnqMQ%0AIenAp7fmMAxbZKWdGqQ%3D%0A-----END%20CERTIFICATE-----%0A";Subject="emailAddress=mario.mancuso@sensedia.com,CN=api-testing1.sensedia.com,OU=PE,O=Sensedia,L=Campinas,ST=Sao Paulo,C=BR";URI=;DNS=api-testing1.sensedia.com
```

This header can be used in several situations, especially if you need to verify any information about the client's x509 certificate at run time in an API call.

# What the interceptor do?

The x509 interceptor extracts all the information from the x-forwarded-client-cert header, decodes the client's certificate and returns both in a single json for easier use at another time.

# Prerequisites for using it in an API

For the correct operation of the interceptor it need to be used in a mTLS scenario on api request flow as image below:

<img src=”/src/main/resources/x509-interceptor-request-flow.png”>

# Prerequisites for edit it

* Java 8
* Maven 3.5.1 or latest

Steep 1: <a href="https://docs.sensedia.com/en/api-platform-guide/4.3.x.x/interceptors/_attachments/api-interceptor-java-spec-3.0.1.jar"> Downlod Sensedia dependency jar</a>

Steep 2: Install api-interceptor-java-spec-3.0.1.jar in the maven local repository (.m2)

```sh
mvn install:install-file \
   -Dfile=../api-interceptor-java-spec-3.0.1.jar \
   -DgroupId=com.sensedia\
   -DartifactId=api-interceptor-java-spec\
   -Dversion=3.0.1 \
   -Dpackaging=jar \
   -DgeneratePom=true
```

# Generate Jar

At the root of the project execute:


```sh
mvn clean package
```

The jar file will be generate into <i>"target"</i> folder.

# Register and configure it on the API Platform

Please see <a href="https://docs.sensedia.com/en/api-platform-guide/4.3.x.x/interceptors/custom-java.html">API Platform - User Guide</a></br>

# More information:
Version: 1.0 </br>
Author: Mario Mancuso (mario.mancuso@sensedia.com) </br>
Product Engineering

