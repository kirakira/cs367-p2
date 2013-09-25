/**
 * This is the solution class, please do not share with students.
 * @author menghui
 *
 * @param <E> Students will use Strings.
 */

public class CircularLinkedList<E> implements CircularListADT<E> {
    private ListNode<E> current;
    private int count;

    public CircularLinkedList() {
        current = null;
        count = 0;
    }

    public void add(E item) {
        if (current == null) {
            current = new ListNode<E>(item);
            current.setNext(current);
            current.setPrevious(current);
        } else {
            ListNode<E> tmp = new ListNode<E>(item, current, current.getPrevious());
            current.getPrevious().setNext(tmp);
            current.setPrevious(tmp);
            current = tmp;
        }

        ++count;
    }

    public E remove() throws ElementNotFoundException {
        if (count == 0)
            throw new ElementNotFoundException();

        ListNode<E> ret = current;
        if (count > 1) {
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
            current = current.getNext();
        } else
            current = null;

        --count;

        return ret.getData();
    }
    
    public boolean isEmpty() {
        return count == 0;
    }
    
    public int size() {
        return count;
    }

    public E get(int offset) throws ElementNotFoundException {
        if (count == 0)
            throw new ElementNotFoundException();

        ListNode<E> t = current;
        offset = offset % count;

        for (int i = 0; i < offset; ++i)
            t = t.getNext();
        for (int i = 0; i > offset; --i)
            t = t.getPrevious();

        return t.getData();
    }
    
    public void setCurrentPosition(int offset) {
        if (count == 0)
            return;

        offset = offset % count;
        for (int i = 0; i < offset; ++i)
            current = current.getNext();
        for (int i = 0; i > offset; --i)
            current = current.getPrevious();
    }

    public String print(int offset) {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < count; ++i)
                sb.append(get(offset + i).toString() + "\n");
        } catch (ElementNotFoundException e) {
        }
        return sb.toString();
    }
}
