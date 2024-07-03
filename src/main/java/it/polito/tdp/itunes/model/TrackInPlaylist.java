package it.polito.tdp.itunes.model;

import java.util.Objects;

public class TrackInPlaylist {
	private int track;
	private int n;
	public TrackInPlaylist(int track, int n) {
		super();
		this.track = track;
		this.n = n;
	}
	public int getTrack() {
		return track;
	}
	public void setTrack(int track) {
		this.track = track;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	@Override
	public int hashCode() {
		return Objects.hash(n, track);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrackInPlaylist other = (TrackInPlaylist) obj;
		return n == other.n && track == other.track;
	}
	
	
}

