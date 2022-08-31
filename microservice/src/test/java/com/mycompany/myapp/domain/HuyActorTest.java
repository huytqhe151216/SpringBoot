package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HuyActorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HuyActor.class);
        HuyActor huyActor1 = new HuyActor();
        huyActor1.setId(1L);
        HuyActor huyActor2 = new HuyActor();
        huyActor2.setId(huyActor1.getId());
        assertThat(huyActor1).isEqualTo(huyActor2);
        huyActor2.setId(2L);
        assertThat(huyActor1).isNotEqualTo(huyActor2);
        huyActor1.setId(null);
        assertThat(huyActor1).isNotEqualTo(huyActor2);
    }
}
