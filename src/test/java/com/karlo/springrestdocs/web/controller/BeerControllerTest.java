package com.karlo.springrestdocs.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karlo.springrestdocs.domain.Beer;
import com.karlo.springrestdocs.repositories.BeerRepository;
import com.karlo.springrestdocs.web.model.BeerDTO;
import com.karlo.springrestdocs.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs()
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "com.karlo.springrestdocs.web.mapper")
public class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerRepository beerRepository;

    @Test
    public void getBeerById() throws Exception{
        given(beerRepository.findById(any())).willReturn(Optional.of(Beer.builder().build()));

        mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
                .param("iscold", "yes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("v1/beer",
                        pathParameters(
                            parameterWithName("beerId").description("UUID od desired ber to get.")
                        ),
                        requestParameters(
                                parameterWithName("iscold").description("Is beer cold query param")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Beer Id"),
                                fieldWithPath("version").description("Version number"),
                                fieldWithPath("createdDate").description("Date created"),
                                fieldWithPath("lastModifiedDate").description("Last modified date"),
                                fieldWithPath("beerName").description("Beer name"),
                                fieldWithPath("beerStyle").description("Beer style"),
                                fieldWithPath("upc").description("UPC of beer"),
                                fieldWithPath("price").description("Price"),
                                fieldWithPath("quantityOnHand").description("Quantity on hand")
                        )
                ));
    }

    @Test
    public void saveBeer() throws Exception{
        BeerDTO beerDTO = getValidBeerDto();
        String beerDtoToJson = objectMapper.writeValueAsString(beerDTO);

        ConstrainedFields fields = new ConstrainedFields(BeerDTO.class);

        mockMvc.perform(post("/api/v1/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoToJson))
                .andExpect(status().isCreated())
                .andDo(document("v1/beer",
                        requestFields(
                                fields.withPath("id").ignored(),
                                fields.withPath("version").ignored(),
                                fields.withPath("createdDate").ignored(),
                                fields.withPath("lastModifiedDate").ignored(),
                                fields.withPath("beerName").description("Beer name"),
                                fields.withPath("beerStyle").description("Beer style"),
                                fields.withPath("upc").description("UPC of beer"),
                                fields.withPath("price").description("Price"),
                                fields.withPath ("quantityOnHand").ignored()
                        )));

    }

    @Test
    public void updateBeerById() throws Exception{
        BeerDTO beerDTO = getValidBeerDto();
        String beerDtoToJson = objectMapper.writeValueAsString(beerDTO);

        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoToJson))
                .andExpect(status().isNoContent());

    }

    private BeerDTO getValidBeerDto() {
        return BeerDTO.builder()
                .beerName("Nice Ale")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("9.99"))
                .upc(123123009L)
                .build();
    }

    public static class ConstrainedFields {
        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input){
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path){
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                    .descriptionsForProperty(path), ". ")));
        }
    }
}
