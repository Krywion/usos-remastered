package pl.krywion.usosremastered.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.krywion.usosremastered.dto.domain.FacultyDto;
import pl.krywion.usosremastered.entity.Faculty;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        PropertyMap<Faculty, FacultyDto> facultyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getDeanName());

                map().setId(source.getId());
                map().setName(source.getName());
                map().setPostalCode(source.getPostalCode());
                map().setEstablishmentYear(source.getEstablishmentYear());
                map(source.getDean().getId(), destination.getDeanId());
            }
        };

        modelMapper.addMappings(facultyMap);

        return modelMapper;
    }
}