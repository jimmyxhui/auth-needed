This repo tests basic authenticaiton and authorization with keycloak

Needs a truststore to trust keycloaks certificate, currently just using run args:

```
-Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=<pass>
```