package com.project.georadiusservice.config.rabbit;

import com.climate.enums.ResponseCodesEnum;
import com.project.georadiusservice.exception.AdvisorServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitConnectionFactory {

    @Value("${messaging.ssl.enabled}")
    private String isSSLenabled;
    @Value("${messaging.ssl.key.p12}")
    private String sslKeyP12;
    @Value("${messaging.ssl.key.p12.path}")
    private String sslKeyP12Path;
    @Value("${messaging.ssl.certificate.utility}")
    private String sslCertificateUtility;
    @Value("${messaging.ssl.key.jks}")
    private String sslKeyJks;
    @Value("${messaging.ssl.key.jks.path}")
    private String sslKeyJksPath;
    @Value("${messaging.vhost}")
    private String virtualHost;
    @Value("${messaging.port}")
    private int sslPort;
    @Value("${messaging.requestedHeartBeat}")
    private Integer requestedHeartBeat;
    @Value("${messaging.ssl.TLS.protocol.versions}")
    private String tlsProtocolVersion;

    @Value("${messaging.password.decoded}")
    private Boolean isPasswordDecoded;

    @Value("${messaging.host}")
    private String rabbitHost;

    @Value("${messaging.ssl.keyPassphrase}")
    private String rabbitKeyPassphrase;

    @Value("${messaging.pwd}")
    private String rabbitPassword;

    @Value("${messaging.source:ec2}")
    private String rmq_source;

    @Value("${messaging.ssl.trustPassphrase}")
    private String rabbitTrustPassphrase;

    @Value("${messaging.usr}")
    private String rabbitUserName;

    private final String AMQ_RMQ_SOURCE = "amq";

    public static String base64Decode(String token) {
        byte[] decodedBytes = Base64.decodeBase64(token.getBytes());
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    @Bean
    public ConnectionFactory connectionFactoryFromSecretsManager() {
        log.info("SSL Status [Enable={}]", isSSLenabled);
        CachingConnectionFactory cachingConnectionFactory = null;
        com.rabbitmq.client.ConnectionFactory connectionFactory = null;
        if(AMQ_RMQ_SOURCE.equalsIgnoreCase(rmq_source)) {
            try {
                connectionFactory = new com.rabbitmq.client.ConnectionFactory();
                connectionFactory.useSslProtocol();
                cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
                cachingConnectionFactory.setHost(rabbitHost);
                cachingConnectionFactory.setPort(sslPort);
            } catch (GeneralSecurityException generalSecurityException) {
                throw new AdvisorServiceException(ResponseCodesEnum.EXC_RABBITMQ_CONFIGURATION.toString());
            }
        }
        else if (("true").equalsIgnoreCase(isSSLenabled)) {
            String base64DecodeKeyPassphrase = base64Decode(rabbitKeyPassphrase);
            String base64DecodeTrustPassphrase = base64Decode(rabbitTrustPassphrase);
            try {
                char[] keyPassphrase = base64DecodeKeyPassphrase.toCharArray();
                KeyStore ks = KeyStore.getInstance(sslKeyP12);
                ks.load(Files.newInputStream(Paths.get(sslKeyP12Path)), keyPassphrase);
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(sslCertificateUtility);
                kmf.init(ks, keyPassphrase);
                char[] trustPassphrase = base64DecodeTrustPassphrase.toCharArray();
                KeyStore tks = KeyStore.getInstance(sslKeyJks);
                tks.load(Files.newInputStream(Paths.get(sslKeyJksPath)), trustPassphrase);
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(sslCertificateUtility);
                tmf.init(tks);
                SSLContext sslContext = SSLContext.getInstance(tlsProtocolVersion);
                sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                connectionFactory = new com.rabbitmq.client.ConnectionFactory();
                connectionFactory.useSslProtocol(sslContext);
                cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
                cachingConnectionFactory.setHost(rabbitHost);
                cachingConnectionFactory.setPort(sslPort);
            } catch (GeneralSecurityException | IOException generalSecurityException) {
                log.error(generalSecurityException.getMessage(), generalSecurityException);
                throw new AdvisorServiceException(ResponseCodesEnum.EXC_RABBITMQ_CONFIGURATION.toString());
            }
        } else {
            cachingConnectionFactory = new CachingConnectionFactory(rabbitHost);
        }
        cachingConnectionFactory.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(true);
        cachingConnectionFactory.getRabbitConnectionFactory().setTopologyRecoveryEnabled(true);
        cachingConnectionFactory.setVirtualHost(virtualHost);
        cachingConnectionFactory.setUsername(rabbitUserName);
        final String decodedPassword = getDecodedPasswordByEnv();
        cachingConnectionFactory.setPassword(decodedPassword);
        cachingConnectionFactory.setRequestedHeartBeat(requestedHeartBeat);
        return cachingConnectionFactory;
    }

    private String getDecodedPasswordByEnv() {
        return Boolean.TRUE.equals(isPasswordDecoded) ?
                base64Decode(rabbitPassword) : rabbitPassword;
    }

}
