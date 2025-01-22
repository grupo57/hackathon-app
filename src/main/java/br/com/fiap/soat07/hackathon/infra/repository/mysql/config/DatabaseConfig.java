package br.com.fiap.soat07.hackathon.infra.repository.mysql.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.datasource")
@EntityScan("br.com.fiap.soat07.hackathon.infra.repository.mysql.model")
@EnableJpaRepositories("br.com.fiap.soat07.hackathon")
@EnableTransactionManagement
public class DatabaseConfig {

	private String url;
	private String driverClassName;
	private String username;
	private String password;

	@Bean
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(getDriverClassName());
		hikariConfig.setJdbcUrl(getUrl());
		hikariConfig.setUsername(getUsername());
		hikariConfig.setPassword(getPassword());
		return new HikariDataSource(hikariConfig);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}