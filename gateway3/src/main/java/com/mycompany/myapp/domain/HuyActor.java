package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A HuyActor.
 */
@Entity
@Table(name = "huy_actor")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "huyactor")
public class HuyActor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "dob")
    private Instant dob;

    @Column(name = "nationality")
    private String nationality;

    @ManyToMany(mappedBy = "actors")
    @JsonIgnore
    private Set<HuyMovie> movies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public HuyActor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDob() {
        return dob;
    }

    public HuyActor dob(Instant dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public HuyActor nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Set<HuyMovie> getMovies() {
        return movies;
    }

    public HuyActor movies(Set<HuyMovie> huyMovies) {
        this.movies = huyMovies;
        return this;
    }

    public HuyActor addMovie(HuyMovie huyMovie) {
        this.movies.add(huyMovie);
        huyMovie.getActors().add(this);
        return this;
    }

    public HuyActor removeMovie(HuyMovie huyMovie) {
        this.movies.remove(huyMovie);
        huyMovie.getActors().remove(this);
        return this;
    }

    public void setMovies(Set<HuyMovie> huyMovies) {
        this.movies = huyMovies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HuyActor)) {
            return false;
        }
        return id != null && id.equals(((HuyActor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "HuyActor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", dob='" + getDob() + "'" +
            ", nationality='" + getNationality() + "'" +
            "}";
    }
}
