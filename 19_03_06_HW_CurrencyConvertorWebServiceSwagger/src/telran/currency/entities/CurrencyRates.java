package telran.currency.entities;

import java.util.Map;

public class CurrencyRates {
public String date;
public long timestamp;
public Map<String,Double> rates;
public String getDate() {
	return date;
}
public Map<String, Double> getRates() {
	return rates;
}

}
