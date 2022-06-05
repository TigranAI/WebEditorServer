package ru.tigran.WebEditor.database.postgres.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Title;
    private int difficulty;
    private String description;

    @Type(type = "string-array")
    @Column(columnDefinition = "text[]")
    private String[] tests;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTests() {
        return tests;
    }

    public void setTests(String[] tests) {
        this.tests = tests;
    }
}
