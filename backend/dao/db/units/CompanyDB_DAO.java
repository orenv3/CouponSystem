package dao.db.units;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

import beans.coupon.system.Company;
import beans.coupon.system.Coupon;
import connectionPool.ConnectionPool;
import general.StaticQueries;
import myExceptions.MyCompanyExceptions;

/**
 * This class represents the implementation of Company's DAO
 * 
 * @author Oren Vinogura
 *
 */
public class CompanyDB_DAO implements CompanyDAO {

	/**
	 * This method creates company object on the DB_Coupon system. The method
	 * operates only on company table. The method throws EXCEPTION if the
	 * company ID is alrady on the DB.
	 * 
	 * @param company
	 *            This is the company Object to create on DB.
	 */
	@Override
	public void create(Company company) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "INSERT INTO Company VALUES(?,?,?,?)";

		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			companyStatmant(statmnt, company, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

	}

	/**
	 * This method deletes a company from the DB_Coupon system. The method
	 * operates only on company table. The method throw EXCEPTION the company's
	 * id is not in the DB.
	 * 
	 * @param company
	 *            This is a company Object to delete from DB.
	 */
	@Override
	public void delete(Company company) {
		Connection con = null;
		ConnectionPool pool = ConnectionPool.getInstance();
		con = pool.getConnection();
		String sql = "DELETE FROM Company WHERE ID = ?";
		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			statmnt.setLong(1, company.getId());
			int excpton = statmnt.executeUpdate();
			// if row on statmnt.executeUpdate=0 then there is no such id on DB
			if (excpton == 0) {
				statmnt.close();
				throw new MyCompanyExceptions(
						"There is no such company.\n" + "Pleae check the copmany number or contact the helpdesk."
								+ "\n\t\tThe deletion did not execute.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method updates company's object on the DB_Coupon system. This method
	 * operates only on company table. The method throws EXCEPTION if the
	 * company's id is not on the DB system.
	 * 
	 * @param company
	 *            This is an updated company Object in order to update his
	 *            parameters on DB.
	 */
	@Override
	public void update(Company company) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "UPDATE Company SET  COMP_NAME=?, PASSWORD=?, EMAIL =? WHERE ID=?";
		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			companyStatmant(statmnt, company, false);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * Get a company's object via the DB_Coupon system. The
	 * method operates only on company table. This method throws EXCEPTION if
	 * there is no such company id.
	 * 
	 * @param compId
	 *            This is a company's ID number.
	 * 
	 * @return company Object according to the given ID.
	 */
	@Override
	public Company read(long compId) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		ResultSet rst = null;
		con = pool.getConnection();
		Company comp = new Company();
		String sql = "SELECT * FROM Company WHERE ID=?";
		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			statmnt.setLong(1, compId);
			rst = statmnt.executeQuery();
			while (rst.next()) {
				comp.setId(rst.getLong("ID"));
				comp.setName(rst.getString("COMP_NAME"));
				comp.setPassword(rst.getString("PASSWORD"));
				comp.setEmail(rst.getString("EMAIL"));
			}
			long id = comp.getId();
			//if there is no such customer
			if (id == 0) {
				throw new MyCompanyExceptions(
						"There is no such company.\nPlease check the company number or contact the helpdek.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rst.close();
				pool.returnConnection(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return comp;
	}

	/**
	 * Get a company's object via the DB_Coupon system. The
	 * method operates only on company table. This method throws EXCEPTION if
	 * there is no such company id.
	 * 
	 * @param compName
	 *            This is a company's name.
	 * 
	 * @return company Object according to the given name.
	 */
	@Override
	public Company readByName(String compName) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		ResultSet rst = null;
		con = pool.getConnection();
		Company comp = new Company();
		String sql = "SELECT * FROM Company WHERE COMP_NAME=?";

		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			statmnt.setString(1, compName);
			rst = statmnt.executeQuery();
			while (rst.next()) {
				comp.setId(rst.getLong("ID"));
				comp.setName(rst.getString("COMP_NAME"));
				comp.setPassword(rst.getString("PASSWORD"));
				comp.setEmail(rst.getString("EMAIL"));
			}

			// if there is no such customer
			if (comp.getName() == null || comp.getName().equals(null)) {
				throw new MyCompanyExceptions(
						"There is no such company.\nPlease check the company number or contact the helpdek.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rst.close();
				pool.returnConnection(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return comp;
	}

	/**
	 * Get a company's objects list. The list includes all the
	 * companies in company table.
	 * 
	 * @return Collection list of all Company's objects on DB.
	 */
	@Override
	public Collection<Company> readAllCompanies() {
		Collection<Company> list = new LinkedList<>();
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "SELECT * FROM Company";
		try (Statement statmnt = con.createStatement();) {
			ResultSet rst = statmnt.executeQuery(sql);
			companyResultSet(rst, list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		return list;
	}

	/**
	 * Get a coupon objects list. All those coupons belong to
	 * specific company(according to company's id) . The method asking the query
	 * is from the JOIN table(company_coupon table).
	 * 
	 * @param compID
	 *            This is the Company id.
	 * 
	 * @return Collection<coupon> coupon's objects list of a specific company,
	 *         the query is according to id.
	 */
	@Override
	public Collection<Coupon> readAllCoupons(long compID) {
		Collection<Coupon> list = new LinkedList<>();
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		con = pool.getConnection();
		String sql = "SELECT ID,Title,START_DATE,END_DATE,AMOUNT,TYPE,MESSAGE,PRICE,IMAGE FROM Company_Coupon RIGHT JOIN Coupon "
				+ "ON Company_Coupon.COUPON_ID = Coupon.ID WHERE COMP_ID=" + compID;
		try (Statement statmnt = con.createStatement();) {
			ResultSet rst = statmnt.executeQuery(sql);
			StaticQueries.couponResultSet(rst, list);
		} catch (

		SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
		return list;
	}

	/**
	 * This method indicates whether the Login Information that given is valid
	 * or not. In order to do that the method compares every name and password
	 * on DB's company table to the given information. The given information is
	 * accordance with the information on the DB.
	 * 
	 * @param compName
	 *            company login name.
	 * @param password
	 *            company login password.
	 * @return returns true if there is a match between the given login
	 *         information to the login information on the DB. If there is no
	 *         match the method returns false.
	 */
	@Override
	public boolean login(String compName, String password) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		String sql = "SELECT COMP_NAME, PASSWORD FROM Company WHERE COMP_NAME=?";
		con = pool.getConnection();
		try (PreparedStatement statmnt = con.prepareStatement(sql);) {
			statmnt.setString(1, compName);
			ResultSet rst = statmnt.executeQuery();
			while (rst.next()) {
				String name = rst.getString("COMP_NAME");
				String pass = rst.getString("PASSWORD");
				if (name.equals(compName) && pass.equals(password)) {
					rst.close();
					return true;
				}
			}
			rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}

		return false;
	}

	/**
	 * The method receives an empty list and adds in it objects of companies.
	 * This method is only intended to serve other methods that uses ResultSet.
	 * This in case the other methods need to execute query commands in the
	 * database to creates list.
	 * 
	 * @param rst
	 *            This is a given java.sql.ResultSet. The father method provides
	 *            a java.sql.ResultSet, in order to implement query commands on
	 *            DB server.
	 * @param list
	 *            This is empty Collection list. The method provides a result of
	 *            a list containing company objects according to the requirement
	 *            in the SQL command.
	 */
	public void companyResultSet(ResultSet rst, Collection<Company> list) {
		try

		{
			while (rst.next()) {
				Company c = new Company();
				c.setId(rst.getLong("ID"));
				c.setName(rst.getString("COMP_NAME"));
				c.setPassword(rst.getString("PASSWORD"));
				c.setEmail(rst.getString("EMAIL"));
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

	//*********************** private methods ************************//
	/**
	 * This method configures company's parameters in order to use this company
	 * in other methods. Other methods can be for example this.create(company)
	 * and this.update(company) to create a company or update a company. The
	 * method throws EXCEPTION if there is a ID number duplication.
	 * 
	 * @param statment
	 *            This is given java.sql.PreparedStatement. The father method
	 *            provides java.sql.PreparedStatement in order to configure the
	 *            company object and send sql commands to the database. This
	 *            statment closed in the end of the procedure.
	 * @param company
	 *            This is the given company object with new parameters from the
	 *            father method.
	 * @param action
	 *            boolean action. if action=true, the method setes the given
	 *            company's parameters in the sql command to create the object
	 *            on DB. Otherwise, setes the given company's parameters in the
	 *            sql command to update an existing object on DB.
	 * 
	 */
	private void companyStatmant(PreparedStatement statment, Company company, boolean action) {
		int index = -1; // just pure initialize. do not mean nothing 
		try {
			if (action == true) {
				//create new company
				index = 1;
				statment.setLong(index, company.getId());
			} else {
				//update company's parameters on the existing company on DB.
				index = 0;
				statment.setLong(4, company.getId());
			}
			statment.setString(++index, company.getName());
			statment.setString(++index, company.getPassword());
			statment.setString(++index, company.getEmail());
			int excepton = statment.executeUpdate();
			// if row on statmnt.executeUpdate=0 then there are 2 answers:  
			//if action=true -> id already exist on DB, 
			//if action=false -> no such id on DB
			if (excepton == 0) {
				throw new MyCompanyExceptions("The company number in the system is uniq."
						+ "\nMust not be a number duplication OR update company that does no exist."
						+ "\nCheck your company number and if necessary contact the helpdesk");
			}
		} catch (SQLException e) {
			throw new MyCompanyExceptions("The company number in the system is uniq."
					+ "\nMust not be a number duplication OR update company that does no exist."
					+ "\nCheck your company number and if necessary contact the helpdesk");
		} finally {
			try {
				statment.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

}
