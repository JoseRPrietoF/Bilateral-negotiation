import java.util.Random;
import java.util.Vector;

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
		beta = 0.4;
		betainicial = 0.4;
		RU = 0.6;
		S = 0.99;
		umbralCambio = 0.9;
		update();
	}

	public boolean acceptOffer(Bid offer) {

		update();

		return getUtility(offer) >= S;
	}
	
	private void update() {
		
		if (getTime() > umbralCambio) {
			beta = 3;//exponential(getTime());
			RU = RU;//*0.95;
		}
		S = 1 - (1 - RU) * Math.pow(getTime(), 1.0 / beta);

	}

	private double exponential(double time) {
		double res = -2*Math.log(1-getTime())-2*Math.log(umbralCambio)+betainicial;
		return res;
	}

	public Bid proposeOffer() {

		Vector<Bid> m_bids = getOffers(S, S + 0.1);

		Bid selected = m_bids.get(rand.nextInt(m_bids.size()));

		return selected;

	}
}
