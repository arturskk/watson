package net.lipecki.watson.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lipecki.watson.BaseItTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class AddCategoryItTest extends BaseItTest {

    public static final String CATEGORY = "any-category";
    public static final String TYPE = "any-type";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldAddCategory() throws Exception {
        final String categoryUuid = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/category")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(
                                objectMapper.writeValueAsString(
                                        AddCategoryDto.builder()
                                                .type(TYPE)
                                                .name(CATEGORY)
                                                .build()
                                )
                        )
        ).andExpect(
                MockMvcResultMatchers
                        .status()
                        .isOk()
        ).andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(categoryUuid).isNotNull();
    }

}
