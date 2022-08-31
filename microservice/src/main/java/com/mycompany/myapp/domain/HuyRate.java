package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A HuyRate.
 */
@Entity
@Table(name = "huy_rate")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "huyrate")
public class HuyRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "star", nullable = false)
    private Integer star;

    @Column(name = "content")
    private String content;

    @Column(name = "date_create")
    private Instant dateCreate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "huyRates", "genres", "actors" }, allowSetters = true)
    private HuyMovie movie;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HuyRate id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getStar() {
        return this.star;
    }

    public HuyRate star(Integer star) {
        this.star = star;
        return this;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getContent() {
        return this.content;
    }

    public HuyRate content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getDateCreate() {
        return this.dateCreate;
    }

    public HuyRate dateCreate(Instant dateCreate) {
        this.dateCreate = dateCreate;
        return this;
    }

    public void setDateCreate(Instant dateCreate) {
        this.dateCreate = dateCreate;
    }

    public HuyMovie getMovie() {
        return this.movie;
    }

    public HuyRate movie(HuyMovie huyMovie) {
        this.setMovie(huyMovie);
        return this;
    }

    public void setMovie(HuyMovie huyMovie) {
        this.movie = huyMovie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HuyRate)) {
            return false;
        }
        return id != null && id.equals(((HuyRate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HuyRate{" +
            "id=" + getId() +
            ", star=" + getStar() +
            ", content='" + getContent() + "'" +
            ", dateCreate='" + getDateCreate() + "'" +
            "}";
    }
}
