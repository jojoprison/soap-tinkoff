package com.squalorDf.soaptinkoff.database;

import javax.persistence.*;

/**
 * Class for saving result of searching in DataBase.
 */
@Entity
@Table(name = "numbers")
public class Number {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private int number;
    private String filenames;
    private String error;

    public Number() { }

    public Number(String code, int number, String filenames, String error) {
        this.code = code;
        this.number = number;
        this.filenames = filenames;
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFilenames() {
        return filenames;
    }

    public void setFilenames(String filenames) {
        this.filenames = filenames;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Number{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", number=" + number +
                ", filenames='" + filenames + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
