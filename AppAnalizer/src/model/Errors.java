package model;

import java.util.ArrayList;
import java.util.List;

public class Errors {
	List<String> errorsList;

	public Errors() {
		this.errorsList = new ArrayList<>();
	}

	public void palabraNoReconocida(String aux, int noLinea) {
		errorsList.add("\nUnrecognized Word '"+aux+"' - Error in line:"+noLinea);
	}
	public void variableInvalida(String aux, int noLinea) {
		errorsList.add("\nInvalid Variable '"+aux+"' - Error in line:"+noLinea);
	}
	public void errorAlAgregarId(String aux, int noLinea) {
		errorsList.add("\nID Can't Be Added '"+aux+"' - Error in line:"+noLinea);
	}
	public void masDe2Letras(String aux, int noLinea) {
		errorsList.add("\nThe Variables Only  Contain One Lower Case '"+aux+"' - Error in line:"+noLinea);
	}	
	public void menosDe3DigitosVariables(String aux, int noLinea) {
		errorsList.add("\nVariables Have To Contain 1...3 Digits '"+aux+"' - Error in line:"+noLinea);
	}
	public void masDe3DigitosVariables(String aux, int noLinea) {
		errorsList.add("\nVariables Can't Use More Than 3 Digits '"+aux+"' - Error in line:"+noLinea);
	}	
	public void longitudDecimalesEntero(String aux, int noLinea) {
		errorsList.add("\nInt Numbers lenght Have To Be <= 5 '"+aux+"' - Error in line:"+noLinea);

	}
	public void puntoDecimalFlotantes(String aux, int noLinea){
		errorsList.add("\nFloat Numbers Only Can Contain One Decimal Dot '"+aux+"' - Error in line:"+noLinea);
	}

	public void caracterNoReconocido(String aux, int noLinea) {
		errorsList.add("\nUnrecognized Character '"+aux+"' - Error in line:"+noLinea); 

	}

	public List<String> getErrorsList() {
		return errorsList;
	}

	public void setErrorsList(List<String> errorsList) {
		this.errorsList = errorsList;
	}



}
