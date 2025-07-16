package com.example.osm.configs;

import com.example.osm.service.converter.ConverterFactory;
import com.example.osm.service.converter.EntityConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Configuration
public class ConverterConfig {

    @Bean
    public ConverterFactory converterFactory(List<EntityConverter<?, ?>> converters) {
        ConverterFactory factory = new ConverterFactory();

        converters.forEach(converter -> {
            ResolvableType resolvableType = ResolvableType.forInstance(converter)
                    .as(EntityConverter.class);

            Class<?> entityClass = resolvableType.getGeneric(0).resolve();
            Class<?> dtoClass = resolvableType.getGeneric(1).resolve();

            if (entityClass != null && dtoClass != null) {
                registerConverter(factory, entityClass, dtoClass, converter);
            }
        });

        return factory;
    }

    @SuppressWarnings("unchecked")
    private <E, D> void registerConverter(
            ConverterFactory factory,
            Class<?> entityClass,
            Class<?> dtoClass,
            EntityConverter<?, ?> converter) {
        factory.registerConverters(
                (Class<E>) entityClass,
                (Class<D>) dtoClass,
                (EntityConverter<E, D>) converter
        );
    }
}
