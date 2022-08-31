package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    @JsonIgnoreProperties(value = { "huyRates", "genres", "actors" }, allowSetters = true)
    private Set<HuyMovie> movies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HuyActor id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public HuyActor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDob() {
        return this.dob;
    }

    public HuyActor dob(Instant dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return this.nationality;
    }

    public HuyActor nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Set<HuyMovie> getMovies() {
        return this.movies;
    }

    public HuyActor movies(Set<HuyMovie> huyMovies) {
        this.setMovies(huyMovies);
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
        if (this.movies != null) {
            this.movies.forEach(i -> i.removeActor(this));
        }
        if (huyMovies != null) {
            huyMovies.forEach(i -> i.addActor(this));
        }
        this.movies = huyMovies;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
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
