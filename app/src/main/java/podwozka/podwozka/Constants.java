package podwozka.podwozka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public final class Constants {

    public static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new StdDateFormat());

        return mapper;
    }

    public static final String MESSAGE = "MESSAGE";

    public static final String TRAVELDTO = "TRAVELDTO";
}
