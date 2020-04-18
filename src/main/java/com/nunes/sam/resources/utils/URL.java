package com.nunes.sam.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	//metodo para decodificar o nome, caso o user tenha colocado espaco em branco ele vai vir com %20% no lugar do espaco
	// e isso eh oq queremos tirar
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	//eh estatico pq vai ser chamado sem precisar instanciar
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(",");
		
		List<Integer> list = new ArrayList<>();
		
		for (int i=0; i<vet.length;i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		
		return list;
		
		//tambem poderia ser por lambda:
//		return Arrays.asList(s.split(",")).stream().map(x-> Integer.parseInt(x)).collect(Collectors.toList());
	}

}
