Connect to Cassandra with DSE with SSL.
========================================================

This is a simple example of setting up cassandra (dse version) with ssl client to encryption. It uses a simple 
To setup dse to use SSL we need to follow the steps in the following url

http://www.datastax.com/docs/datastax_enterprise3.1/security/ssl_transport#ssl-transport

The steps are summarised below.

The following commands were used to create the keystore and the truststore.

    keytool -genkey -alias localhost -keyalg RSA -keystore .keystore

    keytool -export -alias localhost -file localhost.cer -keystore .keystore

    keytool -import -v -trustcacerts -alias localhost -file localhost.cer -keystore .truststore


I have included the keystore and truststore from the example. The passwords for these are both password1.

Update the client_encryption_details in the cassandra.yaml 

    client_encryption_options:
    enabled: true
    keystore: /Users/patcho/dev/datastax-ssl-example/src/main/resources/keystore
    keystore_password: password1

The client using the default Java System properties for SSL so, pass the following properties 
when running the ClusterConnect class. 
    
    -Djavax.net.ssl.trustStore=/Users/patcho/dev/datastax-ssl-example/src/main/resources/truststore -Djavax.net.ssl.trustStorePassword=password1

Troubleshooting
================

There is a bug in the Java security jars which may result in the error that needs new jars.
Cannot support TLS_RSA_WITH_AES_256_CBC_SHA with currently installed providers

Fix detailed here -  
http://www.pathin.org/tutorials/java-cassandra-cannot-support-tls_rsa_with_aes_256_cbc_sha-with-currently-installed-providers/

    e.g. For MAC
    Install jars from
    http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
    to 
    /Library/Java/JavaVirtualMachines/<JAVA_VERSION/Contents/Home/jre/lib/security
    e.g.
    /Library/Java/JavaVirtualMachines/jdk1.7.0_40.jdk/Contents/Home/jre/lib/security

If you get a NoHostAvailable Exception and you know for sure your cluster is up, 
then the problem is probably the system properties that are being sent in.

