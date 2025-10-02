package gwan.zheng.core.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-11:06
 * @Description:
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            builder
                    .serializers(
                            new LocalDateSerializer(dateFormatter),
                            new LocalDateTimeSerializer(dateTimeFormatter)
                    )
                    .deserializers(
                            new LocalDateDeserializer(dateFormatter),
                            new LocalDateTimeDeserializer(dateTimeFormatter)
                    )
                    .simpleDateFormat("yyyy-MM-dd HH:mm:ss"); // fallback
        };
    }
}

