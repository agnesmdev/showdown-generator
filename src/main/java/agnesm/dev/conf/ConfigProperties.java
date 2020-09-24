package agnesm.dev.conf;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@ConfigurationProperties
public class ConfigProperties {

    @NotNull
    private String host;

    @Primary
    @Bean
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
