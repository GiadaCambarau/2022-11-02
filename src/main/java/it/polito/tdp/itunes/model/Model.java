package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private ItunesDAO dao;
	private List<Genre> generi;
	private Graph<Track, DefaultEdge> grafo;
	private Map<Integer, Track> mappa;
	private List<Track> best;
	private int max;
	
	
	public Model() {
		super();
		this.dao = new ItunesDAO();
		this.generi = dao.getAllGenres();
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		this.mappa = new HashMap<>();
	}
	
	public List<Genre> getGeneri(){
		return this.generi;
	}
	
	public double getMax(Genre g) {
		double max = dao.getMax(g);
		return max;
	}
	public double getMin(Genre g) {
		double min = dao.getMin(g);
		return min;
	}
	
	
	public void creaGrafo(double min, double max, Genre g) {
		for (Track t : dao.getAllTracks()) {
			mappa.put(t.getTrackId(), t);
		}
		List<Track> vertici = dao.getTracks(min, max, g);
		Graphs.addAllVertices(this.grafo, vertici);
		List<TrackInPlaylist> lista = dao.getTrackInP(min, max, g);
		for (TrackInPlaylist t1: lista) {
			for (TrackInPlaylist t2: lista) {
				if (t1.getTrack() != t2.getTrack() && t1.getN()==t2.getN()) {
					Graphs.addEdgeWithVertices(this.grafo, mappa.get(t1.getTrack()), mappa.get(t2.getTrack()));
					
				}
			}
		}
	}
	
	public int getV() {
		return this.grafo.vertexSet().size();
		
	}
	public int getA() {
		return this.grafo.edgeSet().size();
	}

	
	public List<Set<Track>> getConnesse(){
		ConnectivityInspector<Track, DefaultEdge> ci = new ConnectivityInspector<>(grafo);
		return ci.connectedSets();
	}
	
	public int getP(double min, double max, Genre g,Track t) {
		return this.dao.getNP(min, max, g, t);
	}

	public List<Track> calcolaPercorso(double durata) {
		ConnectivityInspector<Track, DefaultEdge> ci = new ConnectivityInspector<>(grafo);
		int max=0;
		Set<Track> set = new HashSet<>();
		for (Set<Track> s: ci.connectedSets()) {
			if (s.size()> max) {
				max =s.size();
			}
		}
		for (Set<Track> s : ci.connectedSets()) {
			if (s.size() == max) {
				set = s;
				break;
			}
		}
		durata = durata*60000;//in millisec
		//set è il set di tracks su cui devo lavoarare
		List<Track> parziale = new ArrayList<>();
		this.best = new ArrayList<>();
		ricorsione (parziale, durata,set);
		
		return best;
	}

	private void ricorsione(List<Track> parziale, double durata, Set<Track> set) {
		//condizione di uscita
		if (parziale.size()>= max) {
			this.best = new ArrayList<>(parziale);
			this.max = parziale.size();
		}
		//condizione normale 
		for (Track traccia: set) {
			if (!parziale.contains(traccia)) {
				if ((traccia.getMilliseconds()+calcolaDurata(parziale))<=durata) {
					parziale.add(traccia);
					ricorsione(parziale, durata, set);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}

	private int calcolaDurata(List<Track> parziale) {
		int durata = 0;
		for (Track t: parziale) {
			durata+= t.getMilliseconds();
		}
		return durata;
	}

	
	
	
	
}
