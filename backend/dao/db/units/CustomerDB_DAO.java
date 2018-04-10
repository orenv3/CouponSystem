package dao.db.units;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

import beans.coupon.system.Coupon;
import beans.coupon.system.Customer;
import connectionPool.ConnectionPool;
import general.StaticQueries;
import myExceptions.MyCustomerException;

/**
 * This class represents the implementation of Customer's DAO
 * 
 * @author Oren Vinogura
 *
 */
public class CustomerDB_DAO implements CustomerDAO {

	/**
	 * This method creates customer object on the DB_Coupon system. The method
	 * operates only on customer table. This method throws EXCEPTION if the
	 * customer ID is alrady on the DB.
	 * 
	 * @param customer
	 *            This is the customer Object to create on DB.
	 */
	@Override
	public void create(Customer customer) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "INSERT INTO customer VALUES(?,?,?)";

		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			customerStatmant(statmnt, customer, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * This method deletes a customer from the DB_Coupon system. The method
	 * operates only on customer table. The method throws EXCEPTION if the
	 * customer's id is not in the DB.
	 * 
	 * @param customer
	 *            This is a customer Object to delete from DB.
	 */
	@Override
	public void delete(Customer customer) {
		Connection con = null;
		ConnectionPool pool = ConnectionPool.getInstance();
		con = pool.getConnection();
		String sql = "DELETE FROM Customer WHERE ID = ?";

		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			statmnt.setLong(1, customer.getId());
			int excption = statmnt.executeUpdate();
			if (excption == 0) {
				statmnt.close();
				throw new MyCustomerException("There is no such customer number.\nThe deletion did not execute ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method updates customer's object on the DB_Coupon system. This
	 * method operates only on customer table. This method throws EXCEPTION if
	 * the customer's id is not on the DB system.
	 * 
	 * @param customer
	 *            This is an updated customer Object in order to update his
	 *            parameters on DB.
	 */
	@Override
	public void update(Customer customer) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "UPDATE Customer SET CUST_NAME=?, PASSWORD=? WHERE ID=?";

		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			customerStatmant(statmnt, customer, false);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * Get a customer's object via the DB_Coupon system. The
	 * method operates only on customer table. This method throws EXCEPTION if
	 * there is no such customer id.
	 * 
	 * @param custId
	 *            This is a customer's ID number.
	 * 
	 * @return customer Object according to the given ID.
	 */
	@Override
	public Customer read(long custId) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		Customer cust = new Customer();
		String sql = "SELECT * FROM Customer WHERE ID=?";

		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			statmnt.setLong(1, custId);
			ResultSet rst = statmnt.executeQuery();
			while (rst.next()) {
				cust.setId(rst.getLong("ID"));
				cust.setName(rst.getString("CUST_NAME"));
				cust.setPassword(rst.getString("PASSWORD"));
			}
			long id = cust.getId();// customer name is a no null column on DB.
			if (id == 0) {
				rst.close();
				throw new MyCustomerException(
						"no such customer.\nPlease check the customer number or contact your helpdesk.");
			} else
				rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

		return cust;
	}

	/**
	 * Get a customer's object via the DB_Coupon system. The
	 * method operates only on customer table. This method throws EXCEPTION if
	 * there is no such customer id.
	 * 
	 * @param custName
	 *            This is a customer's name.
	 * 
	 * @return customer Object according to the given name.
	 */
	@Override
	public Customer readByName(String custName) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		Customer cust = new Customer();
		String sql = "SELECT * FROM Customer WHERE CUST_NAME=?";

		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			statmnt.setString(1, custName);
			ResultSet rst = statmnt.executeQuery();
			while (rst.next()) {
				cust.setId(rst.getLong("ID"));
				cust.setName(rst.getString("CUST_NAME"));
				cust.setPassword(rst.getString("PASSWORD"));
			}
			if (cust.getName() == null || cust.getName().equals(null)) {
				// simple check for the exception
				rst.close();
				throw new MyCustomerException(
						"no such customer.\nPlease check the customer number or contact your helpdesk.");
			} else
				rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

		return cust;
	}

	/**
	 * Get a customer's objects list. The list includes all the
	 * customers in customer table.
	 * 
	 * @return Collection list of all customer's objects on DB.
	 */
	@Override
	public Collection<Customer> readAllCustomers() {
		Collection<Customer> list = new LinkedList<>();
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "SELECT * FROM customer";

		try (Statement statmnt = con.createStatement();) {
			ResultSet rst = statmnt.executeQuery(sql);
			customerResultSet(rst, list);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

		return list;
	}

	/**
	 * Get a coupon objects list. All those coupons belong to
	 * specific customer(according to customer's id) . The method asking the
	 * query is from the JOIN table(customer_coupon table).
	 * 
	 * @param custID
	 *            This is the customer id number.
	 * 
	 * @return Collection<coupon> coupon's objects list of a specific customer,
	 *         the query is according to id.
	 */
	public Collection<Coupon> readAllCoupons(long custID) {
		Collection<Coupon> list = new LinkedList<>();
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "SELECT ID,Title,START_DATE,END_DATE,AMOUNT,TYPE,MESSAGE,PRICE,IMAGE FROM Customer_Coupon RIGHT JOIN Coupon "
				+ "ON Customer_Coupon.COUPON_ID = Coupon.ID WHERE CUST_ID=" + custID;

		try (Statement statmnt = con.createStatement();) {
			ResultSet rst = statmnt.executeQuery(sql);
			StaticQueries.couponResultSet(rst, list);
		} catch (SQLException e) {
			throw new MyCustomerException("Could not read the customer fron server.\nContact your administrator");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		return list;
	}

	public Collection<Coupon> readAllCouponsForPresentasion() {
		Collection<Coupon> list = new LinkedList<>();
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "SELECT * FROM Coupon";

		try (Statement statmnt = con.createStatement();) {
			ResultSet rst = statmnt.executeQuery(sql);
			StaticQueries.couponResultSet(rst, list);
		} catch (SQLException e) {
			throw new MyCustomerException("Could not read the customer fron server.\nContact your administrator");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		return list;
	}

	/**
	 * This method indicates whether the Login Information that given is valid
	 * or not. In order to do that the method compares every name and password
	 * on DB's customer table to the given information. The given information is
	 * accordance with the information on the DB.
	 * 
	 * @param custName
	 *            customer login name.
	 * @param password
	 *            customer login password.
	 * @return returns true if there is a match between the given login
	 *         information to the login information on the DB. If there is no
	 *         match the method returns false.
	 */
	@Override
	public boolean login(String custName, String password) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		String sql = "SELECT CUST_NAME, PASSWORD FROM Customer WHERE CUST_NAME=?";
		con = pool.getConnection();
		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			statmnt.setString(1, custName);
			ResultSet rst = statmnt.executeQuery();
			while (rst.next()) {
				String name = rst.getString("CUST_NAME");
				String pass = rst.getString("PASSWORD");
				if (name.equals(custName) && pass.equals(password)) {
					rst.close();
					return true;
				}
			}
			rst.close();
		} catch (SQLException e) {

		} finally {
			pool.returnConnection(con);
		}

		return false;
	}

	/**
	 * The method receives an empty list and adds in it objects of customers.
	 * This method is only intended to serve other methods that uses ResultSet.
	 * This in case the other methods need to execute query commands in the
	 * database to creates list.
	 * 
	 * @param rst
	 *            This is a given java.sql.ResultSet. The father method provides
	 *            a java.sql.ResultSet in order to implement query commands on
	 *            DB server.
	 * @param list
	 *            This is empty Collection list. The method provides a result of
	 *            a list containing customer objects according to the
	 *            requirement in the SQL command from the father method.
	 */
	public void customerResultSet(ResultSet rst, Collection<Customer> list) {
		try {
			while (rst.next()) {
				Customer c = new Customer();
				c.setId(rst.getLong("ID"));
				c.setName(rst.getString("CUST_NAME"));
				c.setPassword(rst.getString("PASSWORD"));
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//*************************// private method //************************************//
	/**
	 * This method configures customer's parameters in order to use this
	 * customer in other methods. Other methods can be for example
	 * this.create(customer) and this.update(customer) to create a customer or
	 * update a customer. The method throws EXCEPTION if there is a ID number
	 * duplication.
	 * 
	 * @param statment
	 *            This is given java.sql.PreparedStatement. The father method
	 *            provides java.sql.PreparedStatement in order to configure the
	 *            customer object and send sql commands to the database. This
	 *            statment closed in the end of the procedure.
	 * @param customer
	 *            This is the given customer object with new parameters from the
	 *            father method.
	 * @param action
	 *            boolean action. if action=true, the method setes the given
	 *            customer's parameters in the sql command to create the object
	 *            on DB. Otherwise, setes the given customer's parameters in the
	 *            sql command in order to update an existing object on DB.
	 */
	private void customerStatmant(PreparedStatement statmnt, Customer customerName, boolean action) {

		int index = -1; // just pure initialize. do not mean nothing 
		try {
			if (action == true) {
				//create new customer
				index = 1;
				statmnt.setLong(index, customerName.getId());
			} else {
				//update customer's parameters on the existing customer on DB.
				index = 0;
				statmnt.setLong(3, customerName.getId());
			}
			statmnt.setString(++index, customerName.getName());
			statmnt.setString(++index, customerName.getPassword());
			int excption = statmnt.executeUpdate();
			// if row on statmnt.executeUpdate=0 then there are 2 answers:  
			//if action=true -> id already exist on DB, 
			//if action=false -> no such id on DB
			if (excption == 0) {
				throw new MyCustomerException("The customer number in the system is uniq."
						+ "\nMust not be a number duplication OR update customer that does no exist."
						+ "\nCheck your customer number and if necessary contact the helpdesk");
			}
		} catch (SQLException e) {
			throw new MyCustomerException("The customer number in the system is uniq."
					+ "\nMust not be a number duplication OR update customer that does no exist."
					+ "\nCheck your customer number and if necessary contact the helpdesk");
		} finally {
			try {
				statmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
