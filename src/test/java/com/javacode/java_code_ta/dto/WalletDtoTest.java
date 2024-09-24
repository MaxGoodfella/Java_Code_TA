package com.javacode.java_code_ta.dto;

import com.javacode.java_code_ta.model.enums.OperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class WalletDtoTest {

    @Autowired
    JacksonTester<WalletDto> json;

    @Test
    void testWalletDto() throws Exception {

        WalletDto walletDto = WalletDto
                .builder()
                .walletId(UUID.fromString("b1fbbf70-0b1d-4b92-946b-3c8a93b9be5b"))
                .operationType(OperationType.valueOf("DEPOSIT"))
                .amount(1000L)
                .build();

        JsonContent<WalletDto> result = json.write(walletDto);

        assertThat(result).extractingJsonPathStringValue("$.walletId")
                .isEqualTo("b1fbbf70-0b1d-4b92-946b-3c8a93b9be5b");
        assertThat(result).extractingJsonPathStringValue("$.operationType")
                .isEqualTo("DEPOSIT");
        assertThat(result).extractingJsonPathNumberValue("$.amount")
                .isEqualTo(1000);

    }

}