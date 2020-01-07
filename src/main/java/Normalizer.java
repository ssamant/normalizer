import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

class Normalizer {
    private static final String HEADER_ADDRESS = "Address";
    private static final String HEADER_BAR_DURATION = "BarDuration";
    private static final String HEADER_FOO_DURATION = "FooDuration";
    private static final String HEADER_FULLNAME = "FullName";
    private static final String HEADER_NOTES = "Notes";
    private static final String HEADER_TOTAL_DURATION = "TotalDuration";
    private static final String HEADER_TIMESTAMP = "Timestamp";
    private static final String HEADER_ZIP = "ZIP";
    private static final String NORMALIZED_FILENAME = "normalized_entries.csv";
    private static final String TIMESTAMP_PATTERN = "M/d/yy h:m:s a";
    private static final String TIMEZONE_EASTERN = "America/New_York";
    private static final String TIMEZONE_PACIFIC = "America/Los_Angeles";
    private static final int ZIPCODE_LENGTH = 5;
    private static final String ZIP_CODE_PREFIX = "0";

    Normalizer() {}

    public void createNormalizeEntries(final String filePath) {
        final List<NormalizedEntry> normalizedEntries = new ArrayList<>();

        File file = new File(filePath);
        try (BufferedReader bufferedReader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim()))
        {
            for(CSVRecord csvRecord : csvParser) {
                final NormalizedEntry normalizedEntry = createNormalizedEntry(csvRecord);
                normalizedEntries.add(normalizedEntry);
            }

        } catch (IOException | DateTimeParseException e) {
            System.out.println("Exception parsing CSV: " + e);
        }

        writeRecordsToCsv(normalizedEntries);
    }

    private NormalizedEntry createNormalizedEntry(final CSVRecord record) throws DateTimeParseException {
        final String convertedTimestamp = formatTimestamp(record.get(HEADER_TIMESTAMP));
        final String convertedZip = formatZipCode(record.get(HEADER_ZIP));
        final String convertedFullName = formatFullName(record.get(HEADER_FULLNAME));
        final double fooDuration = formatDuration(record.get(HEADER_FOO_DURATION));
        final double barDuration = formatDuration(record.get(HEADER_BAR_DURATION));
        final double totalDuration = fooDuration + barDuration;

        return NormalizedEntry.builder()
                .address(record.get(HEADER_ADDRESS))
                .timestamp(convertedTimestamp)
                .barDuration(Double.toString(barDuration))
                .fooDuration(Double.toString(fooDuration))
                .totalDuration(Double.toString(totalDuration))
                .fullName(convertedFullName)
                .zip(convertedZip)
                .notes(record.get(HEADER_NOTES))
                .build();
    }

    private String formatTimestamp(final String unprocessedTimestamp) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);
            final ZonedDateTime oldDateTime = LocalDateTime.from(formatter.parse(unprocessedTimestamp)).atZone(ZoneId.of(TIMEZONE_PACIFIC));
            final LocalDateTime convertedDateTime = oldDateTime.withZoneSameInstant(ZoneId.of(TIMEZONE_EASTERN)).toLocalDateTime();
            return convertedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private String formatZipCode(final String zipCode) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(zipCode);
        while (stringBuilder.length() < ZIPCODE_LENGTH) {
            stringBuilder.insert(0, ZIP_CODE_PREFIX);
        }

        return stringBuilder.toString();
    }

    private String formatFullName(final String fullName) {
        return fullName.toUpperCase();
    }

    private double formatDuration(final String timestamp) {
        final String[] fields = timestamp.split(":");
        double duration = 0;
        duration = duration + Integer.parseInt(fields[0]) * 3600;
        duration = duration + Integer.parseInt(fields[1]) * 60;
        duration = duration + Double.valueOf(fields[2]);

        return duration;

    }

    private void writeRecordsToCsv(final List<NormalizedEntry> normalizedEntries) {
        try (final FileWriter out = new FileWriter(NORMALIZED_FILENAME);
             final CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADER_TIMESTAMP, HEADER_ADDRESS, HEADER_ZIP,
                     HEADER_FULLNAME, HEADER_FOO_DURATION, HEADER_BAR_DURATION, HEADER_TOTAL_DURATION, HEADER_NOTES))) {
            for (final NormalizedEntry entry : normalizedEntries) {
                printer.printRecord(entry.getTimestamp(),
                        entry.getAddress(),
                        entry.getZip(),
                        entry.getFullName(),
                        entry.getFooDuration(),
                        entry.getBarDuration(),
                        entry.getTotalDuration(),
                        entry.getNotes());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
