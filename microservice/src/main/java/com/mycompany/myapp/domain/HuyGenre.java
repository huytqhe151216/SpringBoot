package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    @JsonIgnoreProperties(value = { "huyRates", "genres", "actors" }, allowSetters = true)
    private Set<HuyMovie> movies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HuyGenre id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public HuyGenre name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<HuyMovie> getMovies() {
        return this.movies;
    }

    public HuyGenre movies(Set<HuyMovie> huyMovies) {
        this.setMovies(huyMovies);
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
        if (this.movies != null) {
            this.movies.forEach(i -> i.removeGenre(this));
        }
        if (huyMovies != null) {
            huyMovies.forEach(i -> i.addGenre(this));
        }
        this.movies = huyMovies;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HuyGenre{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
