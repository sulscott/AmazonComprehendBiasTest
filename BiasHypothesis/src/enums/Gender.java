package enums;

/**
 * Nonbinary gender identities and pronouns were not expressed in the data.
 */
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    private String value;

    private Gender(String value) {
        this.value = value;
    };

    public String toString() {
        return this.value;
    }


}
