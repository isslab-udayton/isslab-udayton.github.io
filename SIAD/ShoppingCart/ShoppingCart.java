import java.util.Scanner;
import java.util.Date;

public class ShoppingCart {
  public static void main(String[] args) throws Exception{
	Wallet wallet = new Wallet();
	int balance = wallet.getBalance();
	//Replace <YOUR NAME> below with your name!!! 
	System.out.println("Welcome to <YOUR NAME>'s ShoppingCart. The time now is " + (new Date()).toString());
	System.out.println("Your balance: " + balance+ " credits\n");
	System.out.println("Please select your product: \n" + Store.asString());
	Scanner input = new Scanner(System.in);
	System.out.println("What do you want to buy, type e.g., pen ");
	String product = input.next();
	int price = Store.getPrice(product);
	if(balance>=price){
		wallet.setBalance(balance-price);
		Pocket pocket = new Pocket();
		pocket.addProduct(product);
		System.out.println("Your new balance: " + wallet.getBalance()+ " credits");
	}else{
		System.out.println("Your balance is less than the price");
	}
  }
}
