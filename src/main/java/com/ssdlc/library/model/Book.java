package com.ssdlc.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Getter @Setter @NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String author;

    @NotBlank
    @Column(nullable = false)
    private String category;

    @Column(length = 1000)
    private String description;

    @Min(0)
    @Column(nullable = false)
    private int availableCopies = 1;

    public Book(String title, String author, String category, String description, int availableCopies) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.availableCopies = availableCopies;
    }
}
