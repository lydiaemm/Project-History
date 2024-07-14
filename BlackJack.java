/*Program written by Lydia Emmons
 * 5/23/2024*/
import java.util.*;


public class BlackJack {
	
	public static DeckofCards deck;
	public static ArrayList<Card> playerHand = new ArrayList<Card>();
	public static ArrayList<Card> dealerHand = new ArrayList<Card>();
	public static Scanner inp = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		boolean play = true;
		
		
		System.out.println("Welcome to the game of all games!\n\n\nIf you don't know how to play Blackjack already...\n"
				+ "Go look it up and come back later, I'll wait :)\n\n\nAssuming you have come prepared.\n"
				+ "LET THE GAME COMMENCE!");
		
		while(play) {
			System.out.println("\n\n====================================================================================================================\n\n");
			
			int playerScore = 0;
			int dealerScore = 0;
			
			
			playerHand.clear();
			dealerHand.clear();
			
			deck = new DeckofCards();
			
			deck.shuffleDeck();
			
			System.out.println("Dealing Cards...");
			playerHand.add(deck.dealCard());
			dealerHand.add(deck.dealCard());
			playerHand.add(deck.dealCard());
			dealerHand.add(deck.dealCard());
			
			playerScore = player();
			
			dealerScore = dealer();
			
			calculateWin(playerScore, dealerScore);
			
			play = playAgain();			
		}
	}
	
	public static boolean playAgain() {
		boolean play = true;
		boolean tryAgain = false;
		
		do{
			System.out.println("\n\nWould you like to play again? (yes/no)");
			String choice = inp.next();
			if(choice.compareToIgnoreCase("yes") == 0) {
				play = true;
				tryAgain = false;
			}
			else if(choice.compareToIgnoreCase("no") == 0) {
				play = false;
				tryAgain = false;
			}
			else {
				System.out.println("Invalid Input. Please enter \"yes\" or \"no\".");
				tryAgain = true;
			}
		}while(tryAgain);
		
		return play;
	}
	
	public static void calculateWin(int playerScore, int dealerScore) {
		if(playerScore <= 21) {
			if(dealerScore > 21 || playerScore > dealerScore) {
				System.out.println("You win!");
			}
			else if(playerScore == dealerScore) {
				System.out.println("We tie.");
			}
			else if(dealerScore > playerScore && dealerScore <=21) {
				System.out.println("The house wins.");
			}
		}
		else {
			System.out.println("The house wins.");
		}
	}
	
	public static int player() {
		
		int value = 0;
		boolean ace = false;
		boolean hit = false;
		boolean tryAgain = false;
		
		
		System.out.print("Your Hand:  ");
		for(Card card : playerHand) {
			System.out.print(card.toString() + "     ");
			value += card.getValue();
			if(card.getRank().compareToIgnoreCase("ace") == 0)
				ace = true;
		}
		if(ace && value + 10 <= 21)
			value += 10;
		
		System.out.println("\n\nValue of Your Hand: " + value);
		
		
		do {
			System.out.println("\n\nHit or Stand?");
			
			do{
				String choice = inp.next();
				if(choice.compareToIgnoreCase("hit") == 0) {
					hit = true;
					tryAgain = false;
					
					playerHand.add(deck.dealCard());
					
					value = 0;
					System.out.print("Your Hand:  ");
					for(Card card : playerHand) {
						System.out.print(card.toString() + "     ");
						if(card.getRank().compareToIgnoreCase("ace") == 0)
							ace = true;
						value += card.getValue();
					}
					if(ace && value + 10 <= 21)
						value += 10;
					
					System.out.println("\n\nValue of Your Hand: " + value);
				}
				else if(choice.compareToIgnoreCase("stand") == 0) {
					hit = false;
					tryAgain = false;
				}
				else {
					System.out.println("Invalid Input. Please enter \"hit\" or \"stand\".");
					tryAgain = true;
				}
			}while(tryAgain);
			
		}while(hit && value < 21);
		
		return value;
		
	}
	
	public static int dealer() {
		
		int value = 0;
		
		for(Card card : dealerHand) {
			value += card.getValue();
		}
		while (value < 17) {
			dealerHand.add(deck.dealCard());
			System.out.println("\nThe dealer hits!");
			System.out.println(dealerHand.get(dealerHand.size() - 1));
			value = 0;
			for(Card card : dealerHand) {
				value += card.getValue();
			}
		}
		
		System.out.println("\n\nThat's the end of the game.\n\nHere were the dealer's cards: ");
		for(Card card : dealerHand) {
			System.out.print(card.toString() + "     ");
		}
		
		System.out.println("\n\n");
		
		return value;
	}
	
	public static class Card {
		String suit;
		String rank;
		int value;
		
		public Card(String rank, String suit) {
			this.suit = suit;
			this.rank = rank;
			setValue(rank);
		}
		public void setValue(String rank) {
			try {
				value = Integer.parseInt(rank);
			}
			catch(Exception e) {
				
			}
			if(rank.compareToIgnoreCase("ace") == 0) {
				value = 1;
			}
			else if(rank.compareToIgnoreCase("jack") == 0 || rank.compareToIgnoreCase("queen") == 0 || rank.compareToIgnoreCase("king") == 0) {
				value = 10;
			}
		}
		public String getSuit() {
			return suit;
		}
		public String getRank() {
			return rank;
		}
		public int getValue() {
			try {
				value = Integer.parseInt(rank);
			}
			catch(Exception e) {
				
			}
			if(rank.compareToIgnoreCase("ace") == 0) {
				value = 1;
			}
			else if(rank.compareToIgnoreCase("jack") == 0 || rank.compareToIgnoreCase("queen") == 0 || rank.compareToIgnoreCase("king") == 0) {
				value = 10;
			}
			
			return value;
		}
		public String toString() {
			return ("|" + this.rank + " of " + this.suit + "|");
		}
	}
	
	public static class DeckofCards {
		private ArrayList<Card> deck;
		private int size;
		
		public DeckofCards() {
			
			String[] suitArray = {"Hearts", "Diamonds", "Spades", "Clubs"};
			String[] rankArray = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
			
			deck = new ArrayList<Card>();
			
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 13; j++) {
					deck.add(new Card(rankArray[j], suitArray[i]));
					size++;
				}
			}
		}
		
		public void shuffleDeck() {
			
			ArrayList<Card> shuffled = new ArrayList<Card>();
			
			while (deck.size() > 0) {
				int index = (int) (Math.random() * deck.size());
				shuffled.add(deck.remove(index));
			}
			
			this.deck = shuffled;
			
		}
		
		public void printDeck() {
			for (Card card : deck) {
				System.out.println(card.toString());
			}
		}
		
		public Card dealCard() {
			size--;
			return deck.remove(size);
		}
	}

}

