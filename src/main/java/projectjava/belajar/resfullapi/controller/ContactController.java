package projectjava.belajar.resfullapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import projectjava.belajar.resfullapi.entity.User;
import projectjava.belajar.resfullapi.model.ContactResponse;
import projectjava.belajar.resfullapi.model.CreateContactRequest;
import projectjava.belajar.resfullapi.model.WebResponse;
import projectjava.belajar.resfullapi.service.ContactService;

import javax.print.attribute.standard.Media;

@RestController
public class ContactController {

    @Autowired
    ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request){
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }
}
