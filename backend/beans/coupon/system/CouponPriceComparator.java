package beans.coupon.system;

/**
 * This class is Comparator for coupon, ordered by price. Price: low-high
 * 
 * @author oren vinogura
 *
 */
import java.util.Comparator;

public class CouponPriceComparator implements Comparator<Coupon> {

	@Override
	public int compare(Coupon c1, Coupon c2) {
		if (c1.getPrice() < c2.getPrice())
			return -1;
		else if (c1.getPrice() > c2.getPrice())
			return 1;
		else
			return 0;
	}

}
