package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class MainController {

    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            String clientID = requestDTO.getClientId();
            char firstDigit =   clientID.charAt(0);
            BigDecimal maxLimit;
            String currensy="";
            String rqUID = requestDTO.getRqUID();

            if (firstDigit == '8'){
                maxLimit = new BigDecimal(2000);
                currensy = "US";
            } else if (firstDigit == '9'){
                maxLimit = new BigDecimal(1000);
                currensy = "EU";
            } else {
                maxLimit = new BigDecimal(10000);
                currensy = "RUB";
            }
            BigDecimal randomValue = maxLimit.multiply(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble()));
            BigDecimal roundedValue = randomValue.setScale(2, BigDecimal.ROUND_HALF_UP);
            ResponseDTO responseDTO = new ResponseDTO();

            responseDTO.setRqUID(rqUID);
            responseDTO.setClientId(clientID);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currensy);
            responseDTO.setBalance(roundedValue);
            responseDTO.setMaxLimit(maxLimit);


            log.info("********** RequestDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.info("********** ResponseDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
