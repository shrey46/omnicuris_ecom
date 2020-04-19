package com.ecom.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author shrey
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class EcomUserDto {

    private String userName;
    private String userEmail;
    private String userContact;
    private String userAddress;
}
