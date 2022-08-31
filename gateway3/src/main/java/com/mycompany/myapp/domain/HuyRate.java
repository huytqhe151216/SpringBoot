package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;

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
    @JsonIgnoreProperties("huyRates")
    private HuyMovie movie;

    @ManyToOne
    @JsonIgnoreProperties("huyRates")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStar() {
        return star;
    }

    public HuyRate star(Integer star) {
        this.star = star;
        return this;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public HuyRate content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getDateCreate() {
        return dateCreate;
    }

    public HuyRate dateCreate(Instant dateCreate) {
        this.dateCreate = dateCreate;
        return this;
    }

    public void setDateCreate(Instant dateCreate) {
        this.dateCreate = dateCreate;
    }

    public HuyMovie getMovie() {
        return movie;
    }

    public HuyRate movie(HuyMovie huyMovie) {
        this.movie = huyMovie;
        return this;
    }

    public void setMovie(HuyMovie huyMovie) {
        this.movie = huyMovie;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return 31;
    }

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
