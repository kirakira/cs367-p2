import java.lang.reflect.*;
import java.util.*;

public class CircularLinkedListTester {
    public static void main(String... args) {
        new CircularLinkedListTester();
    }

    void print(String s) {
        System.out.println(s);
    }

    @SuppressWarnings("rawtypes")
    CircularLinkedListTester() {
        try {
            Class<?> c = Class.forName("CircularLinkedList");

            TypeVariable[] tv = c.getTypeParameters();
            if (tv.length == 0)
                print("CircularLinkedList is not generic");

            Type[] intfs = c.getGenericInterfaces();
            boolean imp = false;
                for (Type intf : intfs)
                    if (intf.toString().matches("^CircularListADT<.>$"))
                        imp = true;
            if (!imp)
                print("CircularLinkedList does not implement CircularListADT<>, or implemented a non-generic CircularListADT");

            Method p = checkMethodSignature(c, "print", String.class, new Class<?>[] {int.class});
            if (p == null)
                print("Skipping all of of the tests for CircurlarLinkedListTester");
            else {
                testAdd(c, p);
                testRemove(c, p);
                testIsEmpty(c);
                testSize(c);
                testGet(c, p);
                testSetCurrentPosition(c, p);
                testPrint(c);
            }
        } catch (ClassNotFoundException x) {
            print("Class CircularLinkedList not found");
        }
    }

    Method checkMethodSignature(Class<?> c, String name, Class<?> returnType, Class<?>[] params) {
        Class<?>[] p = new Class<?>[params.length];
        try {
            for (int i = 0; i < params.length; ++i) {
                if (params[i] == null)
                    p[i] = Class.forName("java.lang.Object");
                else
                    p[i] = params[i];
            }

            boolean fail = false;

            Method m = c.getDeclaredMethod(name, p);
            int mod = m.getModifiers();
            Class<?>[] pt = m.getParameterTypes();
            Type[] gpt = m.getGenericParameterTypes();

            if (gpt.length != pt.length || params.length != pt.length)
                print("Internal error");
            else {
                boolean nonGeneric = false;
                for (int i = 0; !fail && i < pt.length; ++i)
                    if (params[i] == null && gpt[i].toString().equals(pt[i].toString()))
                        nonGeneric = true;
                if (nonGeneric)
                    print("Method " + name + "() is not generic");
            }

            if (returnType != null) {
                if (!returnType.equals(m.getReturnType())) {
                    fail = true;
                    print("Method " + name + "() has mismatched return type");
                }
            } else {
                if (!(m.getReturnType().equals(Object.class) &&
                        !m.getReturnType().toString().equals(m.getGenericReturnType().toString())))
                    print("Method " + name + "() has non-generic return type");
            }

            if (!Modifier.isPublic(mod))
                print("Method " + name + "() is not public");

            if (!fail)
                return m;
            else
                return null;
        } catch (ClassNotFoundException x) {
            print(x.getMessage());
            return null;
        } catch (NoSuchMethodException e) {
            print("Method " + name + "() does not exist or signature does not match");
            return null;
        } catch (SecurityException e) {
            print("Method " + name + "() may not have public access");
            print(e.getMessage());
            return null;
        }
    }

    String getOutput(Object list, Method p) throws IllegalAccessException, InvocationTargetException {
        Object ret = p.invoke(list, 0);
        if (ret == null) {
            print("Method print(0) returns null");
            return "";
        } else {
            return (String) ret;
        }
    }

    Object getInstance(Class<?> c) {
        Object list = null;
        try {
            list = c.newInstance();
        } catch (InstantiationException e) {
            print("Unable to construct CircularLinkedList; check its definition of the Constructor");
        } catch (IllegalAccessException e) {
            print("Unable to access constructor");
        }
        return list;
    }

    boolean checkResult(String s1, String s2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length(); ++i)
            if (s1.charAt(i) != '\n')
                sb.append(s1.charAt(i));
        s1 = sb.toString();
        sb = new StringBuilder();
        for (int i = 0; i < s2.length(); ++i)
            if (s2.charAt(i) != '\n')
                sb.append(s2.charAt(i));
        s2 = sb.toString();
        return s1.equals(s2);
    }

    void testAdd(Class<?> c, Method p) {
        Method m = checkMethodSignature(c, "add", void.class, new Class<?>[] {null});
        if (m == null) {
            print("Method add() does not have correct signature; skipping the rest of the tests for this method");
            return;
        }

        Object list;
        list = getInstance(c);
        if (list == null)
            return;

        try {
            m.invoke(list, "A");

            String res = getOutput(list, p);
            if (!checkResult(res, "A"))
                throw new WrongAnswerException("incorrect when list empty");
        } catch (IllegalAccessException e)  {
            print("Method add() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method add() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method add() " + e.getMessage());
        }
        try {
            m.invoke(list, "B");
            m.invoke(list, "C");

            String res = getOutput(list, p);
            if (!checkResult(res, "C\nB\nA"))
                throw new WrongAnswerException("adds item in wrong order");
        } catch (IllegalAccessException e)  {
            print("Method add() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method add() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method add() " + e.getMessage());
        }
    }

    void testRemove(Class<?> c, Method p) {
        Method mAdd = checkMethodSignature(c, "add", void.class, new Class<?>[] {null});
        if (mAdd == null) {
            print("Skipping tests for remove()");
            return;
        }
        Method m = checkMethodSignature(c, "remove", null, new Class<?>[] {});
        if (m == null) {
            print("Method remove() does not have correct signature; skipping the rest of the tests for this method");
            return;
        }

        Object list;
        list = getInstance(c);
        if (list == null)
            return;

        try {
            mAdd.invoke(list, "A");
            mAdd.invoke(list, "B");
        } catch (IllegalAccessException|InvocationTargetException e)  {
            print("Due to erros in add(), skipping tests for remove()");
            return;
        }

        try {
            Object ret = m.invoke(list);

            String res = getOutput(list, p);
            if (!checkResult(res, "A"))
                throw new WrongAnswerException("removes wrong item");

            if (ret == null)
                throw new WrongAnswerException("returns null");
            else if (ret.getClass().getName().equals("ListNode"))
                throw new WrongAnswerException("returns ListNode instead of data");
            else if (!(ret instanceof String) || !(((String) ret).equals("B")))
                throw new WrongAnswerException("returns wrong item");

        } catch (IllegalAccessException e)  {
            print("Method remove() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method remove() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method remove() " + e.getMessage());
        }

        list = getInstance(c);
        if (list == null)
            return;
        try {
            mAdd.invoke(list, "A");
            mAdd.invoke(list, "B");
            mAdd.invoke(list, "C");
        } catch (IllegalAccessException|InvocationTargetException e)  {
            print("Due to erros in add(), skipping the rest of the tests for remove()");
            return;
        }

        try {
            m.invoke(list);
            String res =  getOutput(list, p);
            if (!checkResult(res, "B\nA"))
                throw new WrongAnswerException("doesn't set the currect position correctly after removal");
        } catch (IllegalAccessException|InvocationTargetException e) {
        } catch (WrongAnswerException e) {
            print("Method remove() " + e.getMessage());
        }

        list = getInstance(c);
        if (list == null)
            return;
        try  {
            m.invoke(list);
            throw new WrongAnswerException("doesn't throw exception when list is empty");
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
            if (!e.getCause().getClass().getName().equals("ElementNotFoundException"))
                print("Method remove() throws an exception other than ElementNotFoundException when list is empty");
        } catch (WrongAnswerException e) {
            print("Method remove() " + e.getMessage());
        }
    }

    void testIsEmpty(Class<?> c) {
        Method mAdd = checkMethodSignature(c, "add", void.class, new Class<?>[] {null});
        Method mRemove = checkMethodSignature(c, "remove", null, new Class<?>[] {});
        if (mAdd == null || mRemove == null) {
            print("Skipping tests for isEmpty()");
            return;
        }
        Method m = checkMethodSignature(c, "isEmpty", boolean.class, new Class<?>[] {});
        if (m == null) {
            print("Method isEmpty() does not have correct signature; skipping the rest of the tests for this method");
            return;
        }

        Object list1, list2, list3;
        list1 = getInstance(c);
        list2 = getInstance(c);
        list3 = getInstance(c);
        if (list1 == null || list2 == null || list3 == null)
            return;

        try {
            mAdd.invoke(list2, "A");
            mRemove.invoke(list2);
            mAdd.invoke(list3, "C");
        } catch (IllegalAccessException|InvocationTargetException e)  {
            print("Due to erros in add() or remove(), skipping tests for isEmpty()");
            return;
        }

        try {
            Object ret1 = m.invoke(list1);
            Object ret2 = m.invoke(list2);
            if ((Boolean) ret1 == false || (Boolean) ret2 == false)
                throw new WrongAnswerException("doesn't return true when list is empty");
        } catch (IllegalAccessException e)  {
            print("Method isEmpty() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method isEmpty() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method isEmpty() " + e.getMessage());
        }

        try {
            Object ret = m.invoke(list3);
            if ((Boolean) ret == true)
                throw new WrongAnswerException("doesn't return false when list is not empty");
        } catch (IllegalAccessException e)  {
            print("Method isEmpty() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method isEmpty() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method isEmpty() " + e.getMessage());
        }
    }

    void testSize(Class<?> c) {
        Method mAdd = checkMethodSignature(c, "add", void.class, new Class<?>[] {null});
        Method mRemove = checkMethodSignature(c, "remove", null, new Class<?>[] {});
        if (mAdd == null || mRemove == null) {
            print("Skipping tests for size()");
            return;
        }
        Method m = checkMethodSignature(c, "size", int.class, new Class<?>[] {});
        if (m == null) {
            print("Method size() does not have correct signature; skipping the rest of the tests for this method");
            return;
        }

        Object list1, list2, list3;
        list1 = getInstance(c);
        list2 = getInstance(c);
        list3 = getInstance(c);
        if (list1 == null || list2 == null || list3 == null)
            return;

        try {
            mAdd.invoke(list2, "A");
            mAdd.invoke(list2, "A");
            mRemove.invoke(list2);
            mRemove.invoke(list2);
            mAdd.invoke(list3, "C");
            mAdd.invoke(list3, "C");
            mAdd.invoke(list3, "C");
        } catch (IllegalAccessException|InvocationTargetException e)  {
            print("Due to erros in add() or remove(), skipping tests for size()");
            return;
        }

        try {
            Object ret1 = m.invoke(list1);
            Object ret2 = m.invoke(list2);
            if ((Integer) ret1 != 0 || (Integer) ret2 != 0)
                throw new WrongAnswerException("doesn't return 0 when list is empty");
        } catch (IllegalAccessException e)  {
            print("Method size() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method size() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method size() " + e.getMessage());
        }

        try {
            Object ret = m.invoke(list3);
            if (ret == null || (Integer) ret != 3)
                throw new WrongAnswerException("doesn't update correctly when list is non empty");
        } catch (IllegalAccessException e)  {
            print("Method size() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method size() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method size() " + e.getMessage());
        }
    }

    void testGet(Class<?> c, Method p) {
        Method mAdd = checkMethodSignature(c, "add", void.class, new Class<?>[] {null});
        if (mAdd == null) {
            print("Skipping tests for get()");
            return;
        }
        Method m = checkMethodSignature(c, "get", null, new Class<?>[] {int.class});
        if (m == null) {
            print("Method get() does not have correct signature; skipping the rest of the tests for this method");
            return;
        }

        Object list1, list2;
        list1 = getInstance(c);
        list2 = getInstance(c);
        if (list1 == null || list2 == null)
            return;

        String orig;
        try {
            mAdd.invoke(list2, "A");
            mAdd.invoke(list2, "B");
            mAdd.invoke(list2, "C");
            orig = getOutput(list2, p);
        } catch (IllegalAccessException|InvocationTargetException e)  {
            print("Due to erros in add() or print(0), skipping tests for get()");
            return;
        }

        try {
            m.invoke(list1, 0);
        } catch (IllegalAccessException e) {
            print("Method get() is not invocable");
        } catch (InvocationTargetException e) {
            if (!e.getCause().getClass().getName().equals("ElementNotFoundException"))
                print("Method get() throws an exception other than ElementNotFoundException when list is empty");
        }

        try {
            Object ret1 = m.invoke(list2, 0);
            Object ret2 = m.invoke(list2, 2);
            if (ret1 == null || ret2 == null)
                throw new WrongAnswerException("returns null");
            else if (ret1.getClass().getName().equals("ListNode")
                    || ret2.getClass().getName().equals("ListNode"))
                throw new WrongAnswerException("returns ListNode instead of data");
            else if (!(ret1 instanceof String) || !(ret2 instanceof String)
                    || !((String) ret1).equals("C") || !((String) ret2).equals("A"))
                throw new WrongAnswerException("doesn't return the correct element");
        } catch (IllegalAccessException e)  {
            print("Method get() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method get() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method get() " + e.getMessage());
        }

        try {
            if (!orig.equals(getOutput(list2, p)))
                throw new WrongAnswerException("changes the list");
        } catch (IllegalAccessException e)  {
            print("Method get() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method get() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method get() " + e.getMessage());
        }

        try {
            Object ret = m.invoke(list2, -2);
            if (ret == null)
                throw new WrongAnswerException("returns null");
            else if (ret.getClass().getName().equals("ListNode"))
                throw new WrongAnswerException("returns ListNode instead of data");
            else if (!(ret instanceof String) || !((String) ret).equals("B"))
                throw new WrongAnswerException("doesn't handle negative offsets properly");
        } catch (IllegalAccessException e)  {
            print("Method get() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method get() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method get() " + e.getMessage());
        }
    }

    void testSetCurrentPosition(Class<?> c, Method p) {
        Method mAdd = checkMethodSignature(c, "add", void.class, new Class<?>[] {null});
        if (mAdd == null) {
            print("Skipping tests for setCurrentPosition()");
            return;
        }
        Method m = checkMethodSignature(c, "setCurrentPosition", void.class, new Class<?>[] {int.class});
        if (m == null) {
            print("Method setCurrentPosition() does not have correct signature; skipping the rest of the tests for this method");
            return;
        }

        Object list;
        list = getInstance(c);
        if (list == null)
            return;
        try {
            mAdd.invoke(list, "A");
            mAdd.invoke(list, "B");
            mAdd.invoke(list, "C");
        } catch (IllegalAccessException|InvocationTargetException e)  {
            print("Due to erros in add(), skipping tests for setCurrentPosition()");
            return;
        }

        try {
            m.invoke(list, 2);
            String s = getOutput(list, p);
            if (!checkResult(s, "A\nC\nB"))
                throw new WrongAnswerException("doesn't move the current position to the correct offset");
        } catch (IllegalAccessException e)  {
            print("Method setCurrentPosition() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method setCurrentPosition() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method setCurrentPosition() " + e.getMessage());
        }

        try {
            m.invoke(list, -2);
            String s = getOutput(list, p);
            if (!checkResult(s, "C\nB\nA"))
                throw new WrongAnswerException("doesn't handle negative offsets");
        } catch (IllegalAccessException e)  {
            print("Method setCurrentPosition() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method setCurrentPosition() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method setCurrentPosition() " + e.getMessage());
        }
    }

    void testPrint(Class<?> c) {
        Method mAdd = checkMethodSignature(c, "add", void.class, new Class<?>[] {null});
        if (mAdd == null) {
            print("Skipping tests for print()");
            return;
        }
        Method m = checkMethodSignature(c, "print", String.class, new Class<?>[] {int.class});
        if (m == null) {
            print("Method print() does not have correct signature; skipping the rest of the tests for this method");
            return;
        }

        Object list;
        list = getInstance(c);
        if (list == null)
            return;

        String orig;
        try {
            mAdd.invoke(list, "A");
            mAdd.invoke(list, "B");
            mAdd.invoke(list, "C");
            orig = getOutput(list, m);
        } catch (IllegalAccessException|InvocationTargetException e)  {
            print("Due to erros in add(), skipping tests for print()");
            return;
        }

        try {
            String s = (String) m.invoke(list, 1);
            String s2 = (String) m.invoke(list, -1);
            if (orig.equals(s))
                throw new WrongAnswerException("doesn't use offset");
            else if (s == null || s2 == null)
                throw new WrongAnswerException("returns null");
            else if (!checkResult(s, "B\nA\nC") || !checkResult(s2, "A\nC\nB"))
                throw new WrongAnswerException("incorrectly offsets or doesn't handle negative offsets");
        } catch (IllegalAccessException e)  {
            print("Method print() is not invocable");
        } catch (InvocationTargetException e) {
            print("Method print() threw an expected exception");
        } catch (WrongAnswerException e) {
            print("Method print() " + e.getMessage());
        }
    }
}

@SuppressWarnings("serial")
class WrongAnswerException extends Exception {
    public WrongAnswerException(String s) {
        super(s);
    }
}
