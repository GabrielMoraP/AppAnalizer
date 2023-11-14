package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Analizer{
	private String cadena;
	private char simbolos[] = {',', '(', ')', '[', ']', ';', '='};
	private HashMap<String, Tokens> simbolos2;
	private ArrayList<String> tokens;
	private ArrayList<String> lexemes;
	private Map<String, String> ids;
	private Set<String> idsList;
	private Errors errors2;

	public Analizer(String cadena) {
		this.cadena = cadena;
		this.tokens = new ArrayList<>();
		this.lexemes = new ArrayList<>();
		this.idsList = new LinkedHashSet<>();;
		this.simbolos2 = new HashMap<>();
		this.ids = new LinkedHashMap<>();
		this.errors2 = new Errors();

		simbolos2.put("INICIO", Tokens.tk_inicio);
		simbolos2.put("FIN", Tokens.tk_fin);
		simbolos2.put("ENTERO", Tokens.tk_entero);
		simbolos2.put("FLOTANTE", Tokens.tk_flotante);
		simbolos2.put("LEER", Tokens.tk_leer);
		simbolos2.put("IMPRIMIR", Tokens.tk_imprimir);
		simbolos2.put("SUM", Tokens.tk_sum);
		simbolos2.put("RES", Tokens.tk_res);
		simbolos2.put("MUL", Tokens.tk_mul);
		simbolos2.put("DIV", Tokens.tk_div);
		simbolos2.put(",", Tokens.tk_coma);
		simbolos2.put("(", Tokens.tk_parentesisAbre);
		simbolos2.put(")", Tokens.tk_parentesisCierra);
		simbolos2.put("[", Tokens.tk_corcheteAbre);
		simbolos2.put("]", Tokens.tk_corcheteCierra);
		simbolos2.put(";", Tokens.tk_puntoComa);
		simbolos2.put("{", Tokens.tk_llaveAbre);
		simbolos2.put("}", Tokens.tk_llaveCierra);
		simbolos2.put("=", Tokens.tk_igual);

	}	

	int noDigitosVariable = 0; 
	boolean variable = false;
	boolean errorLetra = false;
	boolean errorNumero = false;
	int noLinea = 1;
	int noDiagonales = 0;
	int noPuntosDecimal = 0;
	int noDigitosEntero = 0;

	boolean errorNoEntero = false;
	boolean palabraReservada = false;
	boolean errorPunto = false;

	StringBuilder tokenActual = new StringBuilder();
	public void analizar() {
		char[] c = cadena.toCharArray();
		StringBuilder numero = new StringBuilder();

		for (char letra : c) {	
			if (noDiagonales == 2) {
				if (letra == '\r') {
					noDiagonales = 0;
					noLinea++;
				}
				continue;
			}
			if (letra == '/') {
				noDiagonales++;
				continue;
			}
			if ('\r' == letra) {
				if (tokenActual.length() > 0) {
					String aux = tokenActual.toString();
					if (variable && noDigitosVariable != 0) {
						if (!actualizarId(tokenActual)) {
							continue;
						}
					}else if (palabraReservada) {
						if (!actualizarTokens(tokenActual)) {
							continue;
						}
					}else if (noDigitosVariable > 3) {
						errors2.masDe3DigitosVariables(aux, noLinea);
					}else{
						errors2.menosDe3DigitosVariables(aux, noLinea);
					}
				}
				restaurarValores();
				noLinea++;
				noDiagonales = 0;
				continue;
			}
			if (letra == '\n' || letra == '\t') {
				restaurarValores();
				continue;
			}

			if (letra == '{' || letra == '}') {
				if (tokenActual.length() > 0) {
					String aux = tokenActual.toString();
					if (variable && noDigitosVariable != 0) {
						if (!actualizarId(tokenActual)) {
							continue;
						}
					}else if (palabraReservada) {
						if (!actualizarTokens(tokenActual)) {
							continue;
						}
					}else if (noDigitosVariable > 3) {
						errors2.masDe3DigitosVariables(aux, noLinea);
					}else{
						errors2.menosDe3DigitosVariables(aux, noLinea);
					}
				}
				tokenActual.setLength(0);
				tokenActual.append(letra);
				actualizarTokens(tokenActual);
				restaurarValores();
				continue;
			}

			if (Character.isLetter(letra)) {
				if (noPuntosDecimal > 0) {
					errors2.palabraNoReconocida(tokenActual.toString(), noLinea);
				}
				if (Character.isUpperCase(letra)) {
					tokenActual.append(letra);
					palabraReservada = true;
				}
				if (Character.isLowerCase(letra)) {
					tokenActual.append(letra);
					if (!variable) {
						variable = true;
						continue;
					}else {
						errorLetra = true;
					}
				}
			}	
			if (Character.isDigit(letra) || letra == '.') {
				if (variable) {
					tokenActual.append(letra);
					noDigitosVariable++;
					if (noDigitosVariable > 3) {
						errorNumero = true;
					}
				}else {
					numero.append(letra);
					if (letra == '.') {
						noPuntosDecimal++;
					}else {
						noDigitosEntero++;
					}
					if (noDigitosEntero > 5) {
						if (!errorNoEntero) {
							errors2.longitudDecimalesEntero(numero.toString(), noLinea);
						}
						errorNoEntero = true;
					}
					if (noPuntosDecimal > 1) {
						errorPunto = true;
					}
				}
			}
			if (noDigitosVariable == 3 && !Character.isDigit(letra)) {
				if (!actualizarId(tokenActual)) {
					continue;
				}
			}
			if (noDigitosEntero > 0 && !Character.isDigit(letra)  && letra != '.') {
				if (!errorNoEntero || !errorPunto) {					
					actualizarNumeros(numero);
				}
				restaurarValores();
			}
			if (esSimboloSeparador(letra)) {
				if (tokenActual.length() > 0) {
					String aux = tokenActual.toString();
					if (palabraReservada) {
						if (actualizarTokens(tokenActual)) {
							tokenActual.append(letra);
							actualizarTokens(tokenActual);
						}						
					}else {
						if (noDigitosVariable != 0) {

							actualizarId(tokenActual);

							tokenActual.append(letra);
							actualizarTokens(tokenActual);
						}else {
							errors2.menosDe3DigitosVariables(aux, noLinea);
						}
					}
				}else {
					tokenActual.append(letra);
					actualizarTokens(tokenActual);
				}
				restaurarValores();
				continue;
			}
			if (letra == ' ') {
				if (palabraReservada) {
					if (actualizarTokens(tokenActual)) {
						palabraReservada = false;
						continue;
					}
				}
				if (noDigitosVariable > 0 && variable) {
					actualizarId(tokenActual);
				}
				restaurarValores();
				continue;
			}
			if (!Character.isDigit(letra) && !Character.isLetter(letra) && letra != '.') {
				errors2.caracterNoReconocido(letra+"", noLinea);
			}

		}


		String aux = tokenActual.toString();
		if (!actualizarTokens(tokenActual) && tokenActual.length() > 0) {
			errors2.palabraNoReconocida(aux, noLinea);
		}

	}

	private void restaurarValores() {
		noPuntosDecimal = 0;
		noDigitosEntero = 0;
		tokenActual.setLength(0);

		errorNoEntero = false;
		palabraReservada = false;
		errorPunto = false;
		noDigitosVariable = 0; 
		variable = false;
		errorLetra = false;
		errorNumero = false;
	}

	private boolean actualizarId(StringBuilder tokenActual) {
		if (tokenActual.toString().equals("")) {
			return false;
		}
		if (noDigitosVariable > 0 || variable) {
			if (errorLetra) {
				errors2.masDe2Letras(tokenActual.toString(),noLinea);
				restaurarValores();
				return false;
			}
			if (errorNumero && noDigitosVariable == 0) {
				errors2.menosDe3DigitosVariables(tokenActual.toString(), noLinea);
				restaurarValores();
				return false;
			}
			if (errorNumero && noDigitosVariable > 3) {
				errors2.masDe3DigitosVariables(tokenActual.toString(), noLinea);
				restaurarValores();
				return false;
			}
			lexemes.add(String.valueOf(tokenActual));
			tokens.add(String.valueOf(Tokens.tk_identificador));
			ids.put(tokenActual.toString(), " ");

			if (!idsList.contains(tokenActual.toString())) {
				idsList.add(tokenActual.toString());
			}
			restaurarValores();
			return true;


		}
		restaurarValores();
		return false;
	}

	private void actualizarNumeros(StringBuilder tokenActual) {
		if (tokenActual.toString().equals("")) {
			return;
		}
		if (ids.get(tokenActual.toString()) == null) {
			if (noPuntosDecimal > 1) {				
				errors2.puntoDecimalFlotantes(tokenActual.toString(), noLinea);
			}else if (tokenActual.indexOf(".") == -1) {
				lexemes.add(String.valueOf(tokenActual));
				tokens.add(String.valueOf(Tokens.tk_numeroEntero));
			}else{
				lexemes.add(String.valueOf(tokenActual));
				tokens.add(String.valueOf(Tokens.tk_numeroFlotante));
			}
		}
		tokenActual.setLength(0);
	}

	private boolean actualizarTokens(StringBuilder palabra) {
		if (palabra.toString().equals("")) {
			return false;
		}

		Tokens token = simbolos2.get(palabra.toString());
		if (token != null) {	
			lexemes.add(String.valueOf(palabra.toString()));
			tokens.add(String.valueOf(token));
			palabra.setLength(0);
			return true;
		}else {
			errors2.palabraNoReconocida(palabra.toString(), noLinea);
		}
		palabra.setLength(0);
		return false;
	}

	private boolean esSimboloSeparador(char c) {
		for (char simbolo : simbolos) {
			if (simbolo == c) {
				return true;
			}
		}
		return false;
	}
	public ArrayList<String> getTokens() {
		return tokens;
	}
	
	public ArrayList<String> getLexemes() {
		return lexemes;
	}

	public ArrayList<String> getIds() {
		return new ArrayList<>(idsList);
	}

	public List<String> getErrors() {
		return errors2.getErrorsList();
	}

	public boolean isAccepted() {
		if(errors2.getErrorsList().size() == 0){
			return true;
		}
		return false;
	}

	
}
