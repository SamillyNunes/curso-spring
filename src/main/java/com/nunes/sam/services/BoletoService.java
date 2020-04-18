package com.nunes.sam.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nunes.sam.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	// num cenario real trocaria isso pra uma chamada de web service pra gerar o boleto e dar a data de vencimento
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto,Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7); //adicionando 7 dias
		pagto.setDataVencimento(cal.getTime());
	}
}
