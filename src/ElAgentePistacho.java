import java.util.*;

import negotiator.Bid;
import negoUPV.UPVAgent;

public class ElAgentePistacho extends UPVAgent {

	Bid last_moment_offer;
	double S;
	double beta;
	double betainicial;
	double RU;
	double N = 5;

	boolean _DEBUG = true;
	int num_pasos;
	ArrayList<Bid> memoria;
	ArrayList<Bid> mis_ofertas;
	int num_delta; // Instantes de tiempo a mirar atrás.

	double max_incr = 0.07; // incremento maximo de la variable
	
	double CHANGE_RU = 1;
	double CHANGE_MAX_INCR = 1.1;
	ArrayList<Boolean> cambios;
	int max_cambios = 5;
	double tiempo_camb = 0.60; // a partir de este iempo se hacen cambios

	public void initialize() {
		last_moment_offer = null;
		/*
		 * Boulware: beta = 0.5; RU = 0.9;
		 * 
		 * Concesor: beta = 5; RU = 0.5;
		 * 
		 * Lineal: beta = 1; RU = 0.8;
		 * 
		 */

		// tit for tat
		memoria = new ArrayList<Bid>();
		mis_ofertas = new ArrayList<Bid>();
		num_delta = 3;
		num_pasos = 0;
		// fin de tit for tat

		beta = 0.4;
		betainicial = 0.4;
		
		// Variables para cambiar RU y max_incr
		CHANGE_RU = 0.97; // no s'empra encara
		CHANGE_MAX_INCR = 1.10;
		cambios = new ArrayList<Boolean>();
		for (int i=0; i<max_cambios; i++) {
			cambios.add(true);
		}
		
		RU = 0.6;
		S = 0.99;
		update();
	}

	public boolean acceptOffer(Bid offer) {
		// En els últims instants aceptem qualsevol oferta
//		if (getTime() > 0.90) {
//			return true;
//		}
		// Añadimos en la lista
		this.memoria.add(offer);

		update();

		return getUtility(offer) >= S;
	}

	/**
	 * Tit for tat
	 */
	private void update() {
		
		// Cambios en el incrmento
		if (cambios.size() > 0 && cambios.remove(0) && 
				tiempo_camb >= getTime()
				) {
			tiempo_camb += 0.05;
			max_incr *= CHANGE_MAX_INCR;
			if (this._DEBUG) {
				print("Cambiando incremento a : " + max_incr);
			}
		}

		if (memoria.size() <= num_delta) {
			// No empezamos la estrategia tit for tat hasta que no tenemos sufucientes datos
			S = 1 - (1 - RU) * Math.pow(getTime(), 1.0 / beta);
		} else {
			// Tit for tat
			double u_delta_numerador = 1 - getUtility(memoria.get(memoria.size() - num_delta + 1));
			double u_delta_denominador = 1 - getUtility(memoria.get(memoria.size() - num_delta));

			double anterior = getUtility(mis_ofertas.get(mis_ofertas.size() - 1));
			double max = Math.max(RU, (u_delta_numerador / u_delta_denominador) * anterior);
			
			double S_ant = S;
			
			print("S anterior "+S_ant);
			print("u_delta_numerador " + u_delta_numerador);
			print("u_delta_denominador " + u_delta_denominador);
			print("anterior " + anterior);
			
			S = Math.min(1, max);
			print("S, S_ant*(1-max_incr) " + S + " : " + S_ant*(1-max_incr));
			// No dejamos que S avance demasiado rapido usando el maximo incremento
			S = Math.max(S, S_ant*(1-max_incr));
			
			// Baixa masa rapid! de primeras baixa a 0.6 (RU)
			print(S + "");
//			System.exit(0);

		}

		this.num_pasos += 1;

		if (this._DEBUG) {
			print("Num pasos: " + num_pasos);
			print("Len memoria: " + memoria.size());
		}
	}

	private double getPromedio(int ini, int fin) {
		double res = 0.0;
		for (int i = ini; i < fin; i++) {
			res += getUtility(memoria.get(i));
		}
		return res / (fin - ini);
	}

	protected void print(String s) {
		System.out.println(s);
	}

	/* Fin de tit for tat */

	public Bid proposeOffer() {

		Vector<Bid> m_bids = getOffers(S, S + 0.1);

		Bid selected = m_bids.get(rand.nextInt(m_bids.size()));
		// Añadimos en la lista
		this.mis_ofertas.add(selected);
		return selected;

	}

}
