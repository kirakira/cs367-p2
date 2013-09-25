/**
 * A ListADT is a sequence of items. A ListADT can be modified by removing
 * an item at a given position and adding items at the end or at a given 
 * position.
 */
public interface ListADT<E> {
    
    /**
     * Adds the given item to the very end of the list.
     * 
     * @param item the item to add
     */
    void add(E item);
    
    /**
     * Adds the given item to the given position in the list. The ListNode
     * currently in this position should be shifted to be after this node.
     * If the position is larger than the size of the list, or less than zero,
     * throws an <tt>InvalidListPositionException</tt>.
     * 
     * @param item the item to add
     * @param pos the position at which to add the item
     * @throws an InvalidListPositionException if the position is invalid
     */
    void add(int pos, E item) throws InvalidListPositionException;
    
    /**
     * Removes the item at the given position. If the position is larger than 
     * or equals to the size of the list, or less than zero, throws an 
     * <tt>InvalidListPositionException</tt>.
     * 
     * @param pos the position at which to remove a node
     * @throws InvalidListPositionException if the position is invalid
     */
    E remove(int pos) throws InvalidListPositionException;
    
    /**
     * Returns the item at the given position in the list. If the position is
     * invalid, throws an <tt>InvalidListPositionException</tt>.
     * @param pos The position of the item to return
     * @return The item at the given position
     * @throws InvalidListPositionException if the position is invalid.
     */
    E get(int pos) throws InvalidListPositionException;
    
    /**
     * Determines if this List is empty, i.e., contains no items.
     * @return true if the List is empty; false otherwise
     */
    boolean isEmpty();
    
    /**
     * Returns the number of items in this List.
     * @return the number of items in this List
     */
    int size();
}
