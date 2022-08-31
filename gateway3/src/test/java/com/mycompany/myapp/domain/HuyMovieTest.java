package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class HuyMovieTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HuyMovie.class);
        HuyMovie huyMovie1 = new HuyMovie();
        huyMovie1.setId(1L);
        HuyMovie huyMovie2 = new HuyMovie();
        huyMovie2.setId(huyMovie1.getId());
        assertThat(huyMovie1).isEqualTo(huyMovie2);
        huyMovie2.setId(2L);
        assertThat(huyMovie1).isNotEqualTo(huyMovie2);
        huyMovie1.setId(null);
        assertThat(huyMovie1).isNotEqualTo(huyMovie2);
    }
}
