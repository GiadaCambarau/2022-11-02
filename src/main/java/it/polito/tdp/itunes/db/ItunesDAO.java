package it.polito.tdp.itunes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Artist;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.MediaType;
import it.polito.tdp.itunes.model.Playlist;
import it.polito.tdp.itunes.model.Track;
import it.polito.tdp.itunes.model.TrackInPlaylist;

public class ItunesDAO {
	
	public List<Album> getAllAlbums(){
		final String sql = "SELECT * FROM Album";
		List<Album> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Album(res.getInt("AlbumId"), res.getString("Title")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Artist> getAllArtists(){
		final String sql = "SELECT * FROM Artist";
		List<Artist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Artist(res.getInt("ArtistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Playlist> getAllPlaylists(){
		final String sql = "SELECT * FROM Playlist";
		List<Playlist> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Playlist(res.getInt("PlaylistId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Track> getAllTracks(){
		final String sql = "SELECT * FROM Track";
		List<Track> result = new ArrayList<Track>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Genre> getAllGenres(){
		final String sql = "SELECT g.* "
				+ "FROM genre g "
				+ "ORDER BY g.Name asc";
		List<Genre> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Genre(res.getInt("GenreId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<MediaType> getAllMediaTypes(){
		final String sql = "SELECT * FROM MediaType";
		List<MediaType> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new MediaType(res.getInt("MediaTypeId"), res.getString("Name")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public double getMax(Genre g) {
		String sql = "SELECT MAX(t.Milliseconds/1000) as m "
				+ "FROM track t, genre g "
				+ "WHERE t.GenreId  = g.GenreId AND g.Name = ? ";
		double max = 0;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, g.getName());
			ResultSet res = st.executeQuery();
			 
			res.next();
			max = res.getDouble("m");
			
			conn.close();
			return max;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		
	}
	public double getMin(Genre g) {
		String sql = "SELECT MIN(t.Milliseconds/1000) as m "
				+ "FROM track t, genre g "
				+ "WHERE t.GenreId  = g.GenreId AND g.Name = ? ";
		double min = 0;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, g.getName());
			ResultSet res = st.executeQuery();
			 
			res.next();
			min = res.getDouble("m");
			
			conn.close();
			return min;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		
	}
	
	public List<Track> getTracks(double min, double max, Genre g){
		String sql = "SELECT t.* "
				+ "FROM track t, genre g  "
				+ "WHERE t.Milliseconds/1000 >= ? AND t.Milliseconds/1000 <= ? AND g.GenreId = t.GenreId AND g.Name = ? ";
		List<Track> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, min);
			st.setDouble(2, max);
			st.setString(3, g.getName());
			ResultSet res = st.executeQuery();
			 
			while (res.next()) {
				result.add(new Track(res.getInt("TrackId"), res.getString("Name"), 
						res.getString("Composer"), res.getInt("Milliseconds"), 
						res.getInt("Bytes"),res.getDouble("UnitPrice")));
			
				}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		
	}

	public List<TrackInPlaylist> getTrackInP(double min, double max, Genre g){
		String sql = "SELECT p.TrackId as t, COUNT(*) AS n "
				+ "FROM track t, playlisttrack p "
				+ "WHERE t.TrackId = p.TrackId AND t.Milliseconds/1000 >= ? AND t.Milliseconds/1000 <= ? AND t.GenreId = ? "
				+ "GROUP BY p.TrackId";
		List<TrackInPlaylist> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, min);
			st.setDouble(2, max);
			st.setInt(3, g.getGenreId());
			ResultSet res = st.executeQuery();
			 
			while (res.next()) {
				TrackInPlaylist t = new TrackInPlaylist(res.getInt("t"), res.getInt("n"));
				result.add(t);
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	public int getNP(double min, double max, Genre g, Track t) {
		String sql = "SELECT p.TrackId, COUNT(*) AS n "
				+ "FROM track t, playlisttrack p "
				+ "WHERE t.TrackId = p.TrackId AND t.Milliseconds/1000 >= ? AND t.Milliseconds/1000 <= ? AND t.GenreId = ? AND t.TrackId = ? "
				+ "GROUP BY p.TrackId";
		
		int playlist =0;
		List<TrackInPlaylist> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, min);
			st.setDouble(2, max);
			st.setInt(3, g.getGenreId());
			st.setInt(4, t.getTrackId());
			ResultSet res = st.executeQuery();
			 
			res.next();
			playlist = res.getInt("n");
			
			conn.close();
			return playlist;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
}
