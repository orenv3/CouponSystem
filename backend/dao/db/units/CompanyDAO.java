package dao.db.units;

import java.util.Collection;

import beans.coupon.system.Company;
import beans.coupon.system.Coupon;

/**
 * This class describe a DAO interface of Company. There are C.R.U.D methods:
 * CREATE, READ, UPDATE, DELETE.
 * 
 * @author Vinogura Oren
 *
 */
interface CompanyDAO {

	void create(Company companyName);

	void delete(Company companyName);

	void update(Company companyName);

	Company read(long compId);

	Collection<Company> readAllCompanies();

	Collection<Coupon> readAllCoupons(long id);

	boolean login(String compName, String password);

	Company readByName(String compName);

}
