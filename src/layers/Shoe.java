/**
 * 
 */
package layers;

/**
 * @author Tahmid
 *
 */
public class Shoe {
	
	public static float[] leatherPerShoe = {3.5f, 3.2f};
	public static float[] volume = {0.35f, 0.33f};
	
	public static float getVolumeFromQuantity(int k, int quantity) {
		return quantity * volume[k];
	}
	
	public static float getQuantityFromVolume(int k, float vol) {
		return vol / volume[k];
	}
	
	public static float getShoeQuantityFromLeatherArea(int k, float area) {
		return area / leatherPerShoe[k];
	}
	
	public static float getLeatherAreaFromShoeQuantity(int k, int quantity) {
		return quantity * leatherPerShoe[k];
	}
	
}
