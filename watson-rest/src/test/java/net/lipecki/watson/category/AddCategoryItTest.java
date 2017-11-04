package net.lipecki.watson.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.lipecki.watson.BaseItTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

public class AddCategoryItTest extends BaseItTest {

    public static final String CATEGORY = "any-category";
    public static final String TYPE = "any-type";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldAddCategory() throws Exception {
        // when
        final ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(
                                objectMapper.writeValueAsString(
                                        AddCategoryDto.builder()
                                                .type(TYPE)
                                                .name(CATEGORY)
                                                .build()
                                )
                        )
        );

        // then
        final String categoryUuid = result.andExpect(
                MockMvcResultMatchers
                        .status()
                        .isOk()
        ).andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(categoryUuid).isNotNull();
    }

}
