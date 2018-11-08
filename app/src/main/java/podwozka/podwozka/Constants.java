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

    public static final String TRAVELDTOS = "TRAVELDTOS";

    public static final String DRIVER_LOGIN = "DRIVER_LOGIN";

    public static final String CAPACITY = "CAPACITY";

    public static final String START_PLACE_SAVED = "START_PLACE_SAVED";

    public static final String END_PLACE_SAVED = "END_PLACE_SAVED";

    public static final String DATE_SAVED = "DATE_SAVED";

    public static final String TIME_SAVED = "TIME_SAVED";
}
