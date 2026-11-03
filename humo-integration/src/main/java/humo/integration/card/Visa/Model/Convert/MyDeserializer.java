package humo.integration.card.Visa.Model.Convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.var;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyDeserializer extends JsonDeserializer {

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.convertValue(
                node,
                new TypeReference<DataMob>() {
                }
        );


    }
//    @Override
//    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//
//
//         JsonNode node = jsonParser.readValueAsTree();
//        System.err.println(node);
//        ObjectReader dataMobReader = mapper.readerFor(new TypeReference<DataMob>() {
//        });
//
//        DataMob dataMob = dataMobReader.readValue(node);
//
//
//        var row = node.get("soapenv:Envelope")
//                .get("soapenv:Body")
//                .get("queryCardholderDataHistoryResponse")
//                .get("Details")
//                .get("row");
//
//
//        if (!row.isArray()) {
//            ArrayList<DataMob.Row> rows = new ArrayList<>();
//
//            ObjectReader reader = mapper.readerFor(new TypeReference<DataMob.Row>() {
//            });
//            DataMob.Row object = reader.readValue(row);
//            rows.add(object);
//            dataMob.getSoapenvEnvelope().getSoapenvBody().getQueryCardholderDataHistoryResponse().getDetails().setRow(rows);
//        }
//
//        return dataMob;
//    }
}
