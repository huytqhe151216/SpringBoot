package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A HuyGenre.
 */
@Entity
@Table(name = "huy_genre")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "huygenre")
public class HuyGenre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres")
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

    public HuyGenre name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<HuyMovie> getMovies() {
        return movies;
    }

    public HuyGenre movies(Set<HuyMovie> huyMovies) {
        this.movies = huyMovies;
        return this;
    }

    public HuyGenre addMovie(HuyMovie huyMovie) {
        this.movies.add(huyMovie);
        huyMovie.getGenres().add(this);
        return this;
    }

    public HuyGenre removeMovie(HuyMovie huyMovie) {
        this.movies.remove(huyMovie);
        huyMovie.getGenres().remove(this);
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
        if (!(o instanceof HuyGenre)) {
            return false;
        }
        return id != null && id.equals(((HuyGenre) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "HuyGenre{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
