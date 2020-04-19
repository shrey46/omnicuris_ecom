package com.ecom.model;

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


@Document(collection = "omnicomUsers")
public class EcomUserModel {

    @Id
    private String id;
    private String userName;
    private String userEmail;
    private String userContact;
    private String userAddress;
    private long userCreatedAt;

}
