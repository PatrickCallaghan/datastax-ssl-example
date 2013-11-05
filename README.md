Connect to Cassandra with DSE with SSL.
========================================================

This is a simple example of setting up cassandra (dse version) with ssl client to encryption. It uses a simple 
To setup dse to use SSL we need to follow the steps in the following url

http://www.datastax.com/docs/datastax_enterprise3.1/security/ssl_transport#ssl-transport

The steps are summarised below.

The following commands were used to create the keystore and the truststore (use password1 for all passwords)

    //Create Keys
    keytool -genkey -alias localhost -keyalg RSA -keysize 1024 -dname "CN=localhost, OU=TEST, O=TEST, C=UK"  -keystore .keystore -storepass cassandra -keypass cassandra
    
    keytool -export -alias localhost -file localhost.cer -keystore .keystore -storepass cassandra -keypass cassandra
    
    keytool -import -v -trustcacerts -alias localhost -file localhost.cer -keystore .truststore -storepass cassandra -keypass cassandra


To connect over cqlsh, you will need to create a pem key which will be used in the .cqlshrc file

    //Create PEM for client
    keytool -importkeystore -srckeystore .keystore -destkeystore localhost_user1.p12 -deststoretype PKCS12
    
    openssl pkcs12 -in localhost_user1.p12 -out localhost_user1.pem -nodes

I have included the keystore and truststore from the example. The passwords for these are both password1.

Update the client_encryption_details in the cassandra.yaml 

    client_encryption_options:
    enabled: true
    keystore: <install-loc>/.keystore
    keystore_password: cassandra

The client using the default Java System properties for SSL so, pass the following properties 
when running the ClusterConnect class. 
    
    -Djavax.net.ssl.trustStore=<install-loc>/.truststore -Djavax.net.ssl.trustStorePassword=cassandra

To connect through cqlsh, you will need to create a file called '.cqlshrc' in your home (~/) dir. You will need 
to source this file or restart your terminal to make the changes visible.

    The .cqlshrc file 
    [authentication]
    username = 
    password = 

    [connection]
    hostname = localhost
    port = 9160
    factory = cqlshlib.ssl.ssl_transport_factory

    [ssl]
    certfile = <install-loc>/localhost_user1.pem
    validate = true ## Optional, true by default.


Troubleshooting
================

There is a bug in the Java security jars which may result in an error that needs new jars.
Cannot support TLS_RSA_WITH_AES_256_CBC_SHA with currently installed providers

Fix detailed here -  
http://www.pathin.org/tutorials/java-cassandra-cannot-support-tls_rsa_with_aes_256_cbc_sha-with-currently-installed-providers/

    e.g. For MAC
    Install jars from
    http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
    to 
    /Library/Java/JavaVirtualMachines/<JAVA_VERSION>/Contents/Home/jre/lib/security
    e.g.
    /Library/Java/JavaVirtualMachines/jdk1.7.0_40.jdk/Contents/Home/jre/lib/security

If you get a NoHostAvailable Exception and you know for sure your cluster is up, 
then the problem is probably the system properties that are being sent in.

