package model;

import java.time.LocalDateTime;

public class DictionaryRow {

    private String word;
    private String pronunciation;
    private String meaning;
    private String source;
    private LocalDateTime dateOfAdded;

    public DictionaryRow() {
    }

    public DictionaryRow(String word, String pronunciation, String meaning, String source) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.meaning = meaning;
        this.source = source;
        this.dateOfAdded = LocalDateTime.now();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getDateOfAdded() {
        return dateOfAdded;
    }

    public void setDateOfAdded(LocalDateTime dateOfAdded) {
        this.dateOfAdded = dateOfAdded;
    }
}
