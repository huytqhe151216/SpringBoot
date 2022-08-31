package com.mycompany.myapp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

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
    private Set<HuyRate> huyRates = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "huy_movie_genre",
               joinColumns = @JoinColumn(name = "huy_movie_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<HuyGenre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "huy_movie_actor",
               joinColumns = @JoinColumn(name = "huy_movie_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"))
    private Set<HuyActor> actors = new HashSet<>();

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

    public HuyMovie name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return director;
    }

    public HuyMovie director(String director) {
        this.director = director;
        return this;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCountry() {
        return country;
    }

    public HuyMovie country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWriter() {
        return writer;
    }

    public HuyMovie writer(String writer) {
        this.writer = writer;
        return this;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Duration getDuration() {
        return duration;
    }

    public HuyMovie duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Instant getPublishDate() {
        return publishDate;
    }

    public HuyMovie publishDate(Instant publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public String getContentSummary() {
        return contentSummary;
    }

    public HuyMovie contentSummary(String contentSummary) {
        this.contentSummary = contentSummary;
        return this;
    }

    public void setContentSummary(String contentSummary) {
        this.contentSummary = contentSummary;
    }

    public Set<HuyRate> getHuyRates() {
        return huyRates;
    }

    public HuyMovie huyRates(Set<HuyRate> huyRates) {
        this.huyRates = huyRates;
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
        this.huyRates = huyRates;
    }

    public Set<HuyGenre> getGenres() {
        return genres;
    }

    public HuyMovie genres(Set<HuyGenre> huyGenres) {
        this.genres = huyGenres;
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
        return actors;
    }

    public HuyMovie actors(Set<HuyActor> huyActors) {
        this.actors = huyActors;
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return 31;
    }

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
