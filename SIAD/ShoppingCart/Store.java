public class Store {
        /**
        * Returns the product list as a String. 
        *
        * @return    product list
        */
        public static String asString(){
		String res = "candies - 1\n car - 30000\n pen - 40\n book - 100";
 		return res;
        }
	/**
	* return price of the product
	*/
	public static int getPrice(String product){
		int price = 0;		
		if (product.equals("candies"))
             		price = 1;
		if (product.equals("car"))
             		price = 30000;
		if (product.equals("pen"))
             		price = 40;
		if (product.equals("book"))
             		price = 100;
     		return price;
        }
}
		
