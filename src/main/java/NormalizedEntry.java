import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NormalizedEntry {
    private String timestamp;
    private String address;
    private String zip;
    private String fullName;
    private String fooDuration;
    private String barDuration;
    private String totalDuration;
    private String notes;

    public NormalizedEntry() {}

    public NormalizedEntry(final String timestamp,
                 final String address,
                 final String zip,
                 final String fullName,
                 final String fooDuration,
                 final String barDuration,
                 final String totalDuration,
                 final String notes) {
        this.timestamp = timestamp;
        this.address = address;
        this.zip = zip;
        this.fullName = fullName;
        this.fooDuration = fooDuration;
        this.barDuration = barDuration;
        this.totalDuration = totalDuration;
        this.notes = notes;
    }
}