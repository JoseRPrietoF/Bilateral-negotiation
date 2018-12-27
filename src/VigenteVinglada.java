import java.util.*;

import negotiator.Bid;
import negoUPV.UPVAgent;

public class VigenteVinglada extends UPVAgent {

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
	ArrayList<Bid> mis_ofertas;
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
		mis_ofertas = new ArrayList<Bid>();
		num_delta = 3;
		num_pasos = 0;
		//fin de tit for tat
		
		beta = 0.01;
		betainicial = 0.01;
		RU = 0.9;
		S = 0.99;
		umbralCambio = 0.9;
		update();
	}

	public boolean acceptOffer(Bid offer) {
		// En els últims instants aceptem qualsevol oferta

		update();

		return getUtility(offer) >= S;
	}
	

	
	/**
	 * Tit for tat
	 * */
	private void update() {
		
		S = Math.max(0.98, S*0.999999999);
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
