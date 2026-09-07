import java.time.LocalDate;

public class Date {

    LocalDate localDate = LocalDate.now();

    public String DateToString(LocalDate date) {
        return localDate.toString();
    }
}
