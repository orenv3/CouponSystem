package connectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * This class describe pool of connection and methods to use them. Those
 * connections are designed to connect to the Database(DB_COUPON). There are
 * maximum 5 connections to use.
 * 
 * @author Oren Vinogura
 *
 */
public class ConnectionPool {

	public final static int MAX_CONNECTIONS = 100;
	private List<Connection> connectionPoolInstanc;
	private static ConnectionPool instance;

	/*
	 * private CTOR for singleton
	 */
	private ConnectionPool() {
		connectionPoolInstanc = addConnection2List();
	}

	/**
	 * This method returns one connection to the an applicant(Client ENUM, other
	 * method etc). This connection connects to the coupon database. There are
	 * maximum 5 connections in the list. If no connection is available in the
	 * list, the applicant will wait till one of the connections will return via
	 * this.returnConnection method. It is a synchronized method to prevent
	 * errors and exceptions. This method uses SoftCloseConnection class in case
	 * of system shutdown.
	 * 
	 * @return A connection to db_coupon
	 */
	public synchronized Connection getConnection() {
		if (connectionPoolInstanc.size() <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else
			return connectionPoolInstanc.remove(0);

		return null;

	}

	/**
	 * This method puts back in place a connection to the List of connection.
	 * This method uses SoftCloseConnection class in case of system shutdown.
	 */
	public synchronized void returnConnection(Connection con) {
		connectionPoolInstanc.add(0, con);
		notify();
	}

	/**
	 * This class is a singleton. This method returns the single Object of this
	 * class.
	 * 
	 * @return Object of ConnectionPool
	 */
	public static synchronized ConnectionPool getInstance() {
		if (instance == null)
			instance = new ConnectionPool();
		return instance;
	}

	public int getSize() {
		return connectionPoolInstanc.size();

	}


	//*********************//		private method 		//***********************//	

	/**
	 * This method Initializes the static connection list(connectionPoolInstanc)
	 * and addes all the connections to it.
	 */
	private LinkedList<Connection> addConnection2List() {

		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/db_coupon?autoReconnect=true&useSSL=false";
		try {
			Class.forName(driverName);
			System.out.println("driver loaded: " + driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {// creates a list of connections.				
			connectionPoolInstanc = new LinkedList<Connection>();
			for (int i = 1; i <= MAX_CONNECTIONS; i++) {
				Connection connection = DriverManager.getConnection(url, "root", "root");
				connectionPoolInstanc.add(connection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (LinkedList<Connection>) connectionPoolInstanc;
	}

}
