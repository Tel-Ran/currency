package telran.currency.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import telran.currency.entities.CurrencyRates;
import telran.currency.service.interfaces.ICurrencyConvertor;
@Service
@ManagedResource
public class CurrencyConverter implements ICurrencyConvertor{
	static private long MIN_REFRESH_PERIOD=60;
	static private long MAX_REFRESH_PERIOD=691200;
	static protected String url="http://data.fixer.io/api/latest?access_key=81ebf276e1ed808b58591b5fb05c34eb";
	static protected RestTemplate restTemplate=new RestTemplate();
	static protected CurrencyRates currencyRates=
			getRates();
	
	static private LocalDateTime last=LocalDateTime.now();
	@Value("${refreshPeriod:3600}")
	long refreshPeriod;
	@ManagedAttribute
	 public long getRefreshPeriod() {
		return refreshPeriod;
	}
	@ManagedAttribute
	public void setRefreshPeriod(long refreshPeriod) {
		if(refreshPeriod<MIN_REFRESH_PERIOD)
			refreshPeriod=MIN_REFRESH_PERIOD;
		else if(refreshPeriod>MAX_REFRESH_PERIOD)
			refreshPeriod=MAX_REFRESH_PERIOD;
			
		this.refreshPeriod = refreshPeriod;
	}
	private boolean refreshRequired() {
		return ChronoUnit.SECONDS.between
				(last, LocalDateTime.now())>refreshPeriod;
	}
	protected static CurrencyRates getRates() {
		ResponseEntity<CurrencyRates> response=
		restTemplate.exchange(url,
		HttpMethod.GET, null,
		CurrencyRates.class);
		CurrencyRates rates=response.getBody();
		last=LocalDateTime.now();
		return rates;
	}


	@Override
	public String lastDateTimePresentation() {
		if(refreshRequired())
			currencyRates=getRates();
		Instant dt=Instant.ofEpochSecond(currencyRates.timestamp);
		DateTimeFormatter dtf=DateTimeFormatter.ofPattern
				("dd/MMM/yyyy HH:mm");
		LocalDateTime ldt=LocalDateTime.ofInstant(dt,ZoneId.systemDefault() );
		return ldt.format(dtf);
	}


	@Override
	public Set<String> getCodes() {
		if(refreshRequired())
			currencyRates=getRates();
		return currencyRates.rates.keySet();
	}


	@Override
	public double convert(String currencyFrom, String currencyTo, int amount) {
		if(refreshRequired())
			currencyRates=getRates();
        Map<String,Double> rates=currencyRates.rates;
		return amount/rates.get(currencyFrom)*
				rates.get(currencyTo);
	}

	
}

