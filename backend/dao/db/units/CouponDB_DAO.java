package dao.db.units;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import beans.coupon.system.Coupon;
import connectionPool.ConnectionPool;
import general.CouponType;
import general.StaticQueries;
import myExceptions.MyCouponException;

/**
 * This class represents the implementation of Coupon's DAO
 * 
 * @author Oren Vinogura
 *
 */
public class CouponDB_DAO implements CouponDAO {
	String connectionExcption = "Something went wrong with the connection.\ncontact your administrator";

	/**
	 * This method creates coupon object on the DB_Coupon system. The method
	 * operates only on coupon table. The method throws EXCEPTION if the coupon
	 * ID is alrady on the DB.
	 * 
	 * @param coupon
	 *            This is the coupon Object to create on DB.
	 */
	@Override
	public void create(Coupon coupon) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		PreparedStatement statment = null;
		String sql = "INSERT INTO Coupon VALUES(?,?,?,?,?,?,?,?,?)";
		con = pool.getConnection();

		try {
			statment = con.prepareStatement(sql);
			couponStatmant(statment, coupon, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method deletes a coupon from the DB_Coupon system. The method
	 * operates only on coupon table. The method throw EXCEPTION the coupon's id
	 * is not in the DB.
	 * 
	 * @param coupon
	 *            This is a coupon Object to delete from DB.
	 */
	@Override
	public void delete(Coupon coupon) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		PreparedStatement statment = null;
		con = pool.getConnection();

		try {
			String sql = "delete from Coupon where id = ?";
			statment = con.prepareStatement(sql);
			statment.setLong(1, coupon.getId());
			int excption = statment.executeUpdate();
			if (excption == 0) {
				throw new MyCouponException(
						"Could not delete the coupon.\nThe values you entered are invalid or the coupon number is not in the system.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				statment.close();
				pool.returnConnection(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method updates coupon's object on the DB_Coupon system. This method
	 * operates only on coupon table. This method throws EXCEPTION if the
	 * coupon's id is not on the DB system.
	 * 
	 * @param coupon
	 *            This is an updated coupon Object in order to update his
	 *            parameters on DB.
	 */
	@Override
	public void update(Coupon coupon) throws MyCouponException {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		PreparedStatement statmnt = null;
		con = pool.getConnection();

		try {
			String sql = "UPDATE Coupon SET TITLE=?, START_DATE=?, END_DATE=?, AMOUNT=?,"
					+ "TYPE=?, MESSAGE=?, PRICE=?, IMAGE=? WHERE ID=?";
			statmnt = con.prepareStatement(sql);
			couponStatmant(statmnt, coupon, false);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pool.returnConnection(con);
		}
	}

	/**
	 * Get a coupon's object via the DB_Coupon system. The
	 * method operates only on coupon table. This method throws EXCEPTION if
	 * there is no such coupon id.
	 * 
	 * @param couponId
	 *            This is a coupon's ID number.
	 * 
	 * @return coupon Object according to the given ID.
	 */
	@Override
	public Coupon read(long couponId) {
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		PreparedStatement stmnt = null;
		ResultSet rst = null;
		Coupon c = new Coupon();
		con = pool.getConnection();

		try {
			String sql = "SELECT * FROM Coupon WHERE ID = ?";
			stmnt = con.prepareStatement(sql);
			stmnt.setLong(1, couponId);
			rst = stmnt.executeQuery();
			while (rst.next()) {
				c.setId(rst.getLong("ID"));
				c.setTitle(rst.getString("TITLE"));
				c.setStartDate(StaticQueries.convert2JavaDate(rst.getDate("START_DATE")));
				c.setEndDate(StaticQueries.convert2JavaDate(rst.getDate("END_DATE")));
				c.setAmount(rst.getInt("AMOUNT"));
				c.setType(CouponType.String2EnumType(rst.getString("TYPE")));
				c.setMessage(rst.getString("MESSAGE"));
				c.setPrice(rst.getFloat("price"));
				c.setImage(rst.getString("IMAGE"));
			}
			long id = c.getId();// coupon name is a no null column on DB + in my tests I do setID().
			if (id == 0) {
				throw new MyCouponException(
						"no such coupon.\nPlease check the coupon number or contact your helpdesk.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rst.close();
				stmnt.close();
				pool.returnConnection(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return c;
	}

	/**
	 * Get a coupon's objects list. The list includes all the
	 * coupons in coupon table.
	 * 
	 * @return Collection list of all coupon's objects on DB.
	 */
	@Override
	public Collection<Coupon> readAllCoupons() {
		Collection<Coupon> list = new LinkedList<>();
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		Statement stmnt = null;
		con = pool.getConnection();

		try {
			String sql = "SELECT * FROM Coupon";
			stmnt = con.createStatement();
			ResultSet resultS = stmnt.executeQuery(sql);
			StaticQueries.couponResultSet(resultS, list);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
				pool.returnConnection(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * Get a coupons list of all the coupons in the coupon
	 * table. The list sorted by type via sql query "order by type".
	 * 
	 * @return Collection list of all coupon's objects from the coupon table
	 *         ordered by type.
	 */
	@Override
	public Collection<Coupon> readCouponByType() {
		Collection<Coupon> list = new LinkedList<>();
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection con = null;
		Statement stmnt = null;
		con = pool.getConnection();

		try {
			String sql = "SELECT * FROM Coupon ORDER BY TYPE";
			stmnt = con.createStatement();
			ResultSet resultS = stmnt.executeQuery(sql);
			StaticQueries.couponResultSet(resultS, list);
			if (list.size() == 0)
				throw new MyCouponException("No coupon was found in the database");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
				pool.returnConnection(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	private Date convert2SQL(java.util.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		java.util.Date current = cal.getTime();// convert the date to milliseconds
		java.sql.Date thisDate = new java.sql.Date(current.getTime()); // create SQL date
		return thisDate;
	}
	//************************** private methods *****************************//

	/**
	 * This method configures coupon's parameters in order to use this coupon in
	 * other methods. Other methods can be for example this.create(coupon) and
	 * this.update(coupon) to create a coupon or update a coupon. The method
	 * throws EXCEPTION if there is a ID number duplication.
	 * 
	 * @param statment
	 *            This is given java.sql.PreparedStatement. The father method
	 *            provides java.sql.PreparedStatement in order to configure the
	 *            coupon object and send sql commands to the database. This
	 *            statment closed in the end of the procedure.
	 * @param coupon
	 *            This is the given coupon object with new parameters from the
	 *            father method.
	 * @param action
	 *            boolean action. if action=true, the method setes the given
	 *            coupon's parameters in the sql command to create the object on
	 *            DB. Otherwise, setes the given coupon's parameters in the sql
	 *            command to update an existing object on DB.
	 * 
	 */
	private void couponStatmant(PreparedStatement statment, Coupon coupon, boolean action) {
		int index = -1; // just for initialize. the -1 do not mean nothing 
		try {
			if (action == true) {
				//create new coupon
				index = 1;
				statment.setLong(index, coupon.getId());
			} else {
				//updating the given coupon's parameters on the existing coupon on DB.
				index = 0;
				statment.setLong(9, coupon.getId());
			}
			statment.setString(++index, coupon.getTitle());
			statment.setDate(++index, this.convert2SQL(coupon.getStartDate()));
			statment.setDate(++index, this.convert2SQL(coupon.getEndDate()));
			statment.setInt(++index, coupon.getAmount());
			statment.setString(++index, CouponType.EnumType2String(coupon.getType()));
			statment.setString(++index, coupon.getMessage());
			statment.setDouble(++index, coupon.getPrice());
			statment.setString(++index, coupon.getImage());
			int exceptn = statment.executeUpdate();
			// if row on statmnt.executeUpdate=0 then there are 2 answers:  
			//if action=true -> id already exist on DB, 
			//if action=false -> no such id on DB
			if (exceptn == 0) {
				throw new MyCouponException("The coupon number in the system is uniq."
						+ "\nMust not be a number duplication OR update coupon that does no exist."
						+ "\nCheck your coupon number and if necessary contact the helpdesk");
			}
		} catch (SQLException e) {
			throw new MyCouponException("The coupon number in the system is uniq."
					+ "\nMust not be a number duplication OR update coupon that does no exist."
					+ "\nCheck your coupon number and if necessary contact the helpdesk");
		} finally {
			try {
				statment.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
}
