package beans.coupon.system;

import java.util.Comparator;

/**
 * This class is Comparator for coupon, ordered by type. Sorting is according to
 * String.compareTo().
 * 
 * @author oren vinogura
 *
 */
public class CouponTypeComparator implements Comparator<Coupon> {

	@Override
	public int compare(Coupon c1, Coupon c2) {
		return c1.getType().compareTo(c2.getType());
	}

}
