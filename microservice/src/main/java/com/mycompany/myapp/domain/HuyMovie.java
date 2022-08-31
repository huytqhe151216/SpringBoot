package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A HuyMovie.
 */
@Entity
@Table(name = "huy_movie")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "huymovie")
public class HuyMovie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "director")
    private String director;

    @Column(name = "country")
    private String country;

    @Column(name = "writer")
    private String writer;

    @Column(name = "duration")
    private Duration duration;

    @Column(name = "publish_date")
    private Instant publishDate;

    @Column(name = "content_summary")
    private String contentSummary;

    @OneToMany(mappedBy = "movie")
    @JsonIgnoreProperties(value = { "movie" }, allowSetters = true)
    private Set<HuyRate> huyRates = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_huy_movie__genre",
        joinColumns = @JoinColumn(name = "huy_movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnoreProperties(value = { "movies" }, allowSetters = true)
    private Set<HuyGenre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_huy_movie__actor",
        joinColumns = @JoinColumn(name = "huy_movie_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @JsonIgnoreProperties(value = { "movies" }, allowSetters = true)
    private Set<HuyActor> actors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HuyMovie id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public HuyMovie name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return this.director;
    }

    public HuyMovie director(String director) {
        this.director = director;
        return this;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCountry() {
        return this.country;
    }

    public HuyMovie country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWriter() {
        return this.writer;
    }

    public HuyMovie writer(String writer) {
        this.writer = writer;
        return this;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public HuyMovie duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Instant getPublishDate() {
        return this.publishDate;
    }

    public HuyMovie publishDate(Instant publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public String getContentSummary() {
        return this.contentSummary;
    }

    public HuyMovie contentSummary(String contentSummary) {
        this.contentSummary = contentSummary;
        return this;
    }

    public void setContentSummary(String contentSummary) {
        this.contentSummary = contentSummary;
    }

    public Set<HuyRate> getHuyRates() {
        return this.huyRates;
    }

    public HuyMovie huyRates(Set<HuyRate> huyRates) {
        this.setHuyRates(huyRates);
        return this;
    }

    public HuyMovie addHuyRate(HuyRate huyRate) {
        this.huyRates.add(huyRate);
        huyRate.setMovie(this);
        return this;
    }

    public HuyMovie removeHuyRate(HuyRate huyRate) {
        this.huyRates.remove(huyRate);
        huyRate.setMovie(null);
        return this;
    }

    public void setHuyRates(Set<HuyRate> huyRates) {
        if (this.huyRates != null) {
            this.huyRates.forEach(i -> i.setMovie(null));
        }
        if (huyRates != null) {
            huyRates.forEach(i -> i.setMovie(this));
        }
        this.huyRates = huyRates;
    }

    public Set<HuyGenre> getGenres() {
        return this.genres;
    }

    public HuyMovie genres(Set<HuyGenre> huyGenres) {
        this.setGenres(huyGenres);
        return this;
    }

    public HuyMovie addGenre(HuyGenre huyGenre) {
        this.genres.add(huyGenre);
        huyGenre.getMovies().add(this);
        return this;
    }

    public HuyMovie removeGenre(HuyGenre huyGenre) {
        this.genres.remove(huyGenre);
        huyGenre.getMovies().remove(this);
        return this;
    }

    public void setGenres(Set<HuyGenre> huyGenres) {
        this.genres = huyGenres;
    }

    public Set<HuyActor> getActors() {
        return this.actors;
    }

    public HuyMovie actors(Set<HuyActor> huyActors) {
        this.setActors(huyActors);
        return this;
    }

    public HuyMovie addActor(HuyActor huyActor) {
        this.actors.add(huyActor);
        huyActor.getMovies().add(this);
        return this;
    }

    public HuyMovie removeActor(HuyActor huyActor) {
        this.actors.remove(huyActor);
        huyActor.getMovies().remove(this);
        return this;
    }

    public void setActors(Set<HuyActor> huyActors) {
        this.actors = huyActors;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HuyMovie)) {
            return false;
        }
        return id != null && id.equals(((HuyMovie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HuyMovie{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", director='" + getDirector() + "'" +
            ", country='" + getCountry() + "'" +
            ", writer='" + getWriter() + "'" +
            ", duration='" + getDuration() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", contentSummary='" + getContentSummary() + "'" +
            "}";
    }
}
