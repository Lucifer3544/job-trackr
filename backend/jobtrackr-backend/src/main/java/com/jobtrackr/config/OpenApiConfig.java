package com.jobtrackr.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobTrackrOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JobTrackr API")
                        .description("REST APIs for managing job applications, companies, HR contacts, interviews, offers, follow-ups and activity timelines.")
                        .version("1.0.0"));
    }
}
