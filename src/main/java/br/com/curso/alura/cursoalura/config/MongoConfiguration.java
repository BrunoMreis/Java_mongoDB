package br.com.curso.alura.cursoalura.config;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import br.com.curso.alura.cursoalura.codec.AlunoCodec;

@Configuration
public class MongoConfiguration {

	@Bean
	MongoClient mongoClient(@Value("${spring.data.mongodb.uri:mongodb://localhost:27017}") String mongoUri) {
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromCodecs(
						new AlunoCodec(MongoClientSettings.getDefaultCodecRegistry().get(Document.class))));
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(mongoUri))
				.codecRegistry(codecRegistry)
				.build();
		return MongoClients.create(settings);
	}
}
