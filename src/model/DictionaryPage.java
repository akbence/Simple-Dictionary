package model;

import java.util.List;

public class DictionaryPage {
    private List<DictionaryRow> rows;
    private int rowNum;
    private int pageNum;
    private int maxResults;

    public DictionaryPage(List<DictionaryRow> rows) {
        this.rows = rows;
    }

    public List<DictionaryRow> getRows() {
        return rows;
    }

    public void setRows(List<DictionaryRow> rows) {
        this.rows = rows;
    }
}
