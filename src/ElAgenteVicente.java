import java.util.*;

import negotiator.Bid;
import negoUPV.UPVAgent;

public class ElAgenteVicente extends UPVAgent {

	Bid last_moment_offer;
	double S;
	double beta;
	double betainicial;
	double RU;
	double N = 5;
	double umbralCambio;
	
	boolean _DEBUG = true;
	int num_pasos;
	ArrayList<Bid> memoria;
	int num_delta; // Instantes de tiempo a mirar atrás.

	public void initialize() {
		last_moment_offer = null;
		/*
		Boulware:
		beta = 0.5;
		RU = 0.9;
		
		Concesor:
		beta = 5;
		RU = 0.5;
		
		Lineal:
		beta = 1;
		RU = 0.8;
		
		 */
		
		// tit for tat
		memoria = new ArrayList<Bid>();
		num_delta = 5;
		num_pasos = 0;
		//fin de tit for tat
		
		beta = 0.4;
		betainicial = 0.4;
		RU = 0.6;
		S = 0.99;
		umbralCambio = 0.9;
		update();
	}

	public boolean acceptOffer(Bid offer) {
		// Añadimos en la lista
		this.memoria.add(offer);
		
		update();

		return getUtility(offer) >= S;
	}
	
//	private void update() {
//		
//		if (getTime() > umbralCambio) {
//			beta = 3;//exponential(getTime());
//			RU = RU;//*0.95;
//		}
//		S = 1 - (1 - RU) * Math.pow(getTime(), 1.0 / beta);
//
//	}
//
//	private double exponential(double time) {
//		double res = -2*Math.log(1-getTime())-2*Math.log(umbralCambio)+betainicial;
//		return res;
//	}

	
	/**
	 * Tit for tat
	 * */
	private void update() {
		
		if (memoria.size() <= num_delta) {
			// No empezamos la estrategia tit for tat hasta que no tenemos sufucientes datos
			S = 1 - (1 - RU) * Math.pow(getTime(), 1.0 / beta);
		}else {
			// Tit for tat
			Bid delta_numerador = memoria.get(memoria.size()-num_delta+1);
			Bid delta_denominador = memoria.get(memoria.size()-num_delta);
			
			double u_delta_numerador = 1 - getUtility(delta_numerador);
			double u_delta_denominador = 1 - getUtility(delta_denominador);
			
			double max = Math.max(RU,  (u_delta_numerador/u_delta_denominador) * getUtility(getLastOpponentOffer()));
			
			S = Math.min(1, max);
			// Baixa masa rapid! de primeras baixa a 0.6 (RU)
//			print(S + "");
//			System.exit(0);
			
		}
		
		
		this.num_pasos += 1;
		
		if (this._DEBUG) {
			print("Num pasos: " + num_pasos);	
			print("Len memoria: " + memoria.size());	
		}
	}

	protected void print(String s) {
		System.out.println(s);
	}
	
	/*Fin de tit for tat*/
	
	
	
	public Bid proposeOffer() {
		
		Vector<Bid> m_bids = getOffers(S, S + 0.1);

		Bid selected = m_bids.get(rand.nextInt(m_bids.size()));

		return selected;

	}
	
	
}
