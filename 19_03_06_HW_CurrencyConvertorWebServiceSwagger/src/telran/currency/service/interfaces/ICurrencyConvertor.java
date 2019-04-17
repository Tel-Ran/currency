package telran.currency.service.interfaces;

import java.util.Set;

public interface ICurrencyConvertor {
String lastDateTimePresentation();
Set<String> getCodes();
double convert(String from, String to, int amount);
}
