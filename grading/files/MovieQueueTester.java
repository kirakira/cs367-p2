import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MovieQueueTester {

	public static void main(String[] args) {
		
		try {
			testEmpty();
			testOne();
			testAddRemove();
		} catch (IOException e) {
			System.out.println("Student's code threw unexpected exception. " +
					"Fix if possible and re-run tests.");
			e.printStackTrace();
		}
		
		try {
			System.out.println("============= Running tests on MovieQueueMain" +
					"=============\n\n");
			System.setIn(new FileInputStream("MainInput.txt"));

			String[] mainArgs = {};

			MovieQueueMain.main(mainArgs);
		} catch (FileNotFoundException e) {
			System.out.println("Could not write to file");
		} catch (Exception e) {
			System.out.println("Student's code threw unexpected exception. Fix" +
					" if possible and re-run tests.");
			e.printStackTrace();
		}
	}

	public static void testEmpty() {
		LinkedList<String> movieQueue = new LinkedList<String>();

		if (!(movieQueue instanceof ListADT))
			System.out.println("LinkedList does not implement ListADT");

		if (movieQueue.size() != 0)			
			System.out.println("size incorrect for empty list");
		try {
			movieQueue.remove(0);
			System.out.println("remove did not throw exception " +
			"for empty list");
		}
		catch(InvalidListPositionException e) { /*correct*/ }
		catch (Exception e) {
			System.out.println("remove method threw wrong exception for empty" +
			" list");
		}

		try {
			movieQueue.get(0);
			System.out.println("get did not throw exception " +
			"for empty list");
		} catch(InvalidListPositionException e) { /*correct*/ }
		catch (Exception e) {
			System.out.println("get method threw wrong exception for empty" +
			" list");
		}

		if (!movieQueue.isEmpty()) System.out.println("isEmpty incorrect for " +
		"empty list");

		try {
			movieQueue.add(1, "ABC");
			System.out.println("add(pos, item) did not throw exception for " +
			"invalid position in empty list");
		} catch(InvalidListPositionException e) { /* correct*/ } 
		catch (Exception e) {
			System.out.println("add(pos, item) threw incorrect exception for " +
			" invalid position in empty list");
		}

		try {
			movieQueue.add(0, "ABC");
		}
		catch (Exception e) {
			System.out.println("add(pos, item) threw exception for " +
			" position 0 in empty list");
		}
	}

	public static void testOne() {
		LinkedList<String> movieQueue = new LinkedList<String>();

		try {
			movieQueue.add("ABC");
		} catch (Exception e) {
			System.out.println("add(item) method incorrect when list empty");
			return;
		}

		try {
			movieQueue.get(0);
		}
		catch (InvalidListPositionException e) {
			System.out.println("get method threw InvalidListPositionException" +
			" for list with one item");
		}

		if (movieQueue.isEmpty()) System.out.println("isEmpty method incorrect " +
		"for nonempty list");

		if (movieQueue.size()!=1)
			System.out.println("size method incorrect for list with 1 item");

		try {
			movieQueue.add(0,"DEF");
			if (!movieQueue.get(0).equals("DEF")) System.out.println("add(pos, item) " +
			"method adds in incorrect order for list with one item");
		}catch(InvalidListPositionException e) {
			System.out.println("add(pos, item) method threw " +
			"exception at valid position for list with one item");
		}

		try {
			String rem = movieQueue.remove(0);
			if (!rem.equals("DEF"))
				System.out.println("remove(0) returns incorrect item for " +
						"nonempty list");
		}catch(InvalidListPositionException e) {
			System.out.println("remove method threw " +
			"exception at valid position for list with 1 item");
		}
	}
	
	public static void testAddRemove() throws IOException {
		LinkedList<String> movieQueue = new LinkedList<String>();
		
		for (int i=1; i<=20; i++) {
			movieQueue.add("Item" + i);
		}
		
		for (int i=1; i<=20; i++) {
			String item = "Item" + i;
			try {
				if (!movieQueue.get(i-1).equals(item)) {
					System.out.println("add method adds items in wrong order");
				}
			} catch (InvalidListPositionException e) {
				System.out.println("get threw exception for a valid position " +
				"in a list with many items");
			}
		}
		
		if (movieQueue.size() != 20)
			System.out.println("size incorrect for list with many items");
		
		if (movieQueue.isEmpty()) System.out.println("isEmpty incorrect for " +
				"list with many items");
		
		movieQueue = new LinkedList<String>();
		
		for (int i=1; i<=20; i++) {
			try {
				movieQueue.add(0, "Item" + i);
			} catch (InvalidListPositionException e) {
				System.out.println("add(pos, item) method threw " +
					"exception at valid position for list with many items");
			}
		}
		
		if (movieQueue.size() != 20)
			System.out.println("size incorrect for list with many items");
		
		for (int i=1; i<=20; i++) {
			String item = "Item" + i;
			try {
				if (movieQueue.get(i-1).equals(item)) {
					System.out.println("add(pos, item) method adds items in wrong order");
				}
			} catch (InvalidListPositionException e) {
				System.out.println("get threw exception for a valid position " +
						"in a list with many items");
			}
		}
		
		try {
			movieQueue.remove(19);
			movieQueue.remove(12);
			movieQueue.remove(4);
			if (movieQueue.size() != 17) System.out.println("size incorrect " +
					"for list with many items");
		} catch (InvalidListPositionException e) {
			System.out.println("remove method threw exception for list with" +
					" many items");
		}
		
		try {
			movieQueue.remove(21);
			System.out.println("remove method did not throw exception for" +
					" too large position");
		} catch (InvalidListPositionException e) { /*correct*/ }
		catch (Exception e) {
			System.out.println("remove method threw wrong exception for " +
					"too large position");
		}
		
		try {
			movieQueue.remove(-1);
			System.out.println("remove method did not throw exception for" +
					" negative position");
		} catch (InvalidListPositionException e) { /*correct*/ }
		catch (Exception e) {
			System.out.println("remove method threw wrong exception for " +
					"negative position");
		}
		
		try {
			movieQueue.add(21, "new");
			System.out.println("add(pos, item) method did not throw exception for" +
					" too large position");
		} catch (InvalidListPositionException e) { /*correct*/ }
		catch (Exception e) {
			System.out.println("add(pos, item) method threw wrong exception for " +
					"too large position");
		}
		
		try {
			movieQueue.add(-1, "item");
			System.out.println("add(pos, item) method did not throw exception for" +
					" negative position");
		} catch (InvalidListPositionException e) { /*correct*/ }
		catch (Exception e) {
			System.out.println("add(pos, item) method threw wrong exception for " +
					"negative position");
		}
		
		try {
			movieQueue.get(21);
			System.out.println("get method did not throw exception for" +
					" too large position");
		} catch (InvalidListPositionException e) { /*correct*/ }
		catch (Exception e) {
			System.out.println("get method threw wrong exception for " +
					"too large position");
		}
		
		try {
			movieQueue.get(-1);
			System.out.println("get method did not throw exception for" +
					" negative position");
		} catch (InvalidListPositionException e) { /*correct*/ }
		catch (Exception e) {
			System.out.println("get method threw wrong exception for " +
					"negative position");
		}
	}
}
