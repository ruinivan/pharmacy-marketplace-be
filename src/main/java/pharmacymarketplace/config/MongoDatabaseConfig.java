package pharmacymarketplace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@EnableMongoRepositories(
        basePackages = "pharmacymarketplace.**.repository.mongo", // Scaneia apenas repositórios Mongo
        mongoTemplateRef = "mongoTemplate"
)
@EnableConfigurationProperties(MongoProperties.class)
public class MongoDatabaseConfig {

    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate(MongoProperties mongoProperties) {
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoProperties.getUri());
        return new MongoTemplate(factory);
    }
}