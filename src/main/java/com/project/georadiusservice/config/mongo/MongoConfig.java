package com.project.georadiusservice.config.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
@RequiredArgsConstructor
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class MongoConfig extends AbstractMongoClientConfiguration {

    private static final String MONGO_TEMPLATE = "mongodb://%s:%s@%s/%s";
    private static final String OPTION_TEMPLATE = "%s=%s";
    private final List<Converter<?, ?>> converters = new ArrayList<>();

    @Value("${mongo.connection.database}")
    private String dbName;
    @Value("${mongo.connection.ssl}")
    private Boolean sslEnabled;
    @Value("${application.env}")
    private String environment;
    @Value("${mongo.connection.replica}")
    private String replicaSet;
    @Value("${mongo.connection.readPref}")
    private String readPreference;
    @Value("${mongo.connection.retryWrite}")
    private String retryWrite;

    @Value("${mongo.connection.url}")
    private String mongoUrl;

    @Value("${mongo.username}")
    private String mongoUsername;

    @Value("${mongo.password}")
    private String mongoPassword;

    @Value("${mongo.connection.ssl.path}")
    private String mongoSslCertPath;

    @Value("${mongo.connection.truststore.password}")
    private String trustStorePassword;

    private Map<String, String> getMongoUriOptions() {
        return Stream.of(new String[][]{
                {"replicaSet", String.valueOf(replicaSet)},
                {"readPreference", String.valueOf(readPreference)},
                {"retryWrites", String.valueOf(retryWrite)},
                {"tls", String.valueOf(sslEnabled)},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @SneakyThrows
    @Override
    public MongoClient mongoClient() {
        if (environment.equalsIgnoreCase("dev")) {
            return MongoClients.create(mongoUrl);
        } else {
            Map<String, String> mongoUriOptions = getMongoUriOptions();
            ConnectionString fullConnectionString = getConnectionStringWithOptions(mongoUsername, mongoPassword, mongoUriOptions);
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(new File(mongoSslCertPath), trustStorePassword.toCharArray())
                    .build();

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(fullConnectionString)
                    .applyToSslSettings(builder ->
                            builder.enabled(true)
                                    .context(sslContext)
                                    .invalidHostNameAllowed(true)
                    )
                    .build();

            return MongoClients.create(settings);
        }
    }

    private ConnectionString getConnectionStringWithOptions(String userName, String password, Map<String, String> mongoUriOptions) {
        String fullConnectionString = String.format(MONGO_TEMPLATE, userName, password, mongoUrl, getDatabaseName());
        StringBuilder options = new StringBuilder("?");
        mongoUriOptions.forEach((key, value) -> {
            if (Objects.nonNull(value)) {
                String option = String.format(OPTION_TEMPLATE, key, value);
                options.append(option).append("&");
            }
        });
        options.deleteCharAt(options.length() - 1);
        fullConnectionString = fullConnectionString + options;
        return new ConnectionString(fullConnectionString);
    }

    @Override
    public MongoCustomConversions customConversions() {
        converters.add(new ZonedDateTimeReadConverter());
        converters.add(new ZonedDateTimeWriteConverter());
        return new MongoCustomConversions(converters);
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return new ZonedDateTimeProvider();
    }

    @Bean
    public MongoEventListener mongoEventListener() {
        return new MongoEventListener();
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}