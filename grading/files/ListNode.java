/**
 * Generic singly linked list node. It serves as the basic building block for 
 * storing data in singly linked chains of nodes.
 * 
 * <b>Do not modify this file in any way!</b>
 */
class ListNode<E> {
	private E data;                // data to be stored 
	private ListNode<E> next;   // connnection to next node

	/**
	 * Constructs a new list node with no link to next node.
	 * @param data the data to be stored in this node
	 */
	ListNode(E data) {
		this(data, null);
	}
	
	/**
	 * Constructs a new list node with a link to next node.
	 * @param data the data to be stored in this node
	 * @param next the node after this one
	 */
	ListNode(E data, ListNode<E> next) {
		this.data = data;
		this.next = next;
	}

	/**
	 * Returns the current data.
	 * @return the current data
	 */
	E getData() {
		return data;
	}

	/**
	 * Returns the current next node.
	 * @return the current next node
	 */
	ListNode<E> getNext() {
		return next;
	}

	/**
	 * Sets the data to the given new value.
	 * @param data the new data
	 */
	void setData(E data) {
		this.data = data;
	}

	/**
	 * Sets the next node to the given new value.
	 * @param next the new next node
	 */
	void setNext(ListNode<E> next) {
		this.next = next;
	}
}
