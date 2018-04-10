package dao.db.units;

import java.util.Collection;

import beans.coupon.system.Coupon;

/**
 * This class describe a DAO interface of Coupon. There are C.R.U.D methods:
 * CREATE, READ, UPDATE, DELETE.
 * 
 * @author Vinogura Oren
 *
 */
interface CouponDAO {

	void create(Coupon couponName);

	void delete(Coupon couponName);

	void update(Coupon couponName);

	Coupon read(long custId);

	Collection<Coupon> readAllCoupons();

	Collection<Coupon> readCouponByType();

}
