import model.DictionaryRow;

import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvHandler {

    public static String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(CsvHandler::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    public static void exportDictionaryToCsv() {
        DbHandler dbHandler = DbHandler.getDbHandler();
        int wordCount = dbHandler.getFilteredDictionaryCount(FilterType.NONE, null);
        int limit = 50;

        LocalDateTime ldt = LocalDateTime.now();
        File csvOutputFile = new File("ExportDictionary" + ldt.getYear() + ldt.getMonthValue() + ldt.getDayOfMonth() + ".csv");

        List<String[]> dataLines = new ArrayList<>();
        try (PrintWriter pw = new PrintWriter(csvOutputFile, StandardCharsets.UTF_8)) {
            int pageCounter = 1;
            while (pageCounter <= wordCount / limit + 1) {
                List<DictionaryRow> rows = dbHandler.getDictionaryRows(limit, (pageCounter - 1) * limit, FilterType.NONE, "");
                rows.forEach(row -> dataLines.add(new String[]
                        {row.getWord(), row.getPronunciation(), row.getMeaning(), row.getSource()}));
                dataLines.stream()
                        .map(CsvHandler::convertToCSV)
                        .forEach(pw::println);
                dataLines.clear();
                pageCounter++;
            }
            System.out.println("Export done at: " + csvOutputFile.getAbsolutePath());
        } catch (FileNotFoundException | UnsupportedEncodingException e ) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
