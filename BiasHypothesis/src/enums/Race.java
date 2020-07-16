package enums;

/**
 * Additional races were not expressed in the data.
 * Will add more once I can find better data and more robust methodology.
 */
public enum Race {
    WHITE("WHITE"),
    BLACK("BLACK");

    private String value;

    private Race(String value) {
        this.value = value;
    };

    public String toString() {
        return this.value;
    }


}
