package telran.currency.api;

public class ConvertData {
public String currencyFrom;
public String currencyTo;
public int amount;
public ConvertData() {
}
public ConvertData(String currencyFrom, String currencyTo, int amount) {
	super();
	this.currencyFrom = currencyFrom;
	this.currencyTo = currencyTo;
	this.amount = amount;
}
public String getCurrencyFrom() {
	return currencyFrom;
}
public String getCurrencyTo() {
	return currencyTo;
}
public int getAmount() {
	return amount;
}

}
