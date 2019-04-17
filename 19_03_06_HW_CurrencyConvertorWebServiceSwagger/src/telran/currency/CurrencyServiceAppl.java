package telran.currency;

import java.util.Scanner;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;

import telran.currency.api.ConvertData;
import telran.currency.service.impl.CurrencyConverter;
import telran.currency.service.interfaces.ICurrencyConvertor;
import static telran.currency.api.CurrencyConstantsApi.*;
@SpringBootApplication
@RestController

public class CurrencyServiceAppl {
	@Autowired
ICurrencyConvertor convertor;
@GetMapping (value=CURRENCIES)
Set<String> getCurrencyCodes(){
	return convertor.getCodes();
}
@GetMapping(value=TIMESTAMP)
String latestDateTimePresentation() {
	return convertor.lastDateTimePresentation();
}
@PostMapping(value=CONVERT)
double convert(@RequestBody ConvertData data) {
	return convertor.convert(data.currencyFrom, data.currencyTo,
			data.amount);
}
@PostConstruct
public void refreshPeriodCheck() {
	if(convertor instanceof CurrencyConverter) {
		CurrencyConverter convertorImpl=(CurrencyConverter)convertor;
		long refreshPeriod=convertorImpl.getRefreshPeriod();
		//application.properties configuration file may contain wrong value
		//setter checks the value and in a case of need updates it
		convertorImpl.setRefreshPeriod(refreshPeriod);
	}
}
public static void main(String[] args) {
	ConfigurableApplicationContext ctx=SpringApplication.run(CurrencyServiceAppl.class, args);
	Scanner scanner=new Scanner(System.in);
	String line=null;
	System.out.println("Enter 'quit' for server shutdown");
	while(true) {
		line=scanner.nextLine();
		if(line.equals("quit"))
			break;
	}
	scanner.close();
	ctx.close();
}

}
