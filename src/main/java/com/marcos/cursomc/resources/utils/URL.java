package com.marcos.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {

	public static List<Integer> decodeIntList(String s) {

		List<Integer> intList = new ArrayList<Integer>();

		String[] vet = s.split(",");

		for (int i = 0; i < vet.length; i++) {
			intList.add(Integer.parseInt(vet[i]));
		}

		return intList;
	}

	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
