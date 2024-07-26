package projectjava.belajar.resfullapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import projectjava.belajar.resfullapi.entity.Contact;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressRequest {

    @JsonIgnore
    @NotBlank
    private String contactId;

    @Size(max = 100)
    private String street;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String province;

    @NotBlank
    @Size(max = 100)
    private String country;

    @Size(max = 10)
    private String postalCode;
}
