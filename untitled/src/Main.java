package list;

public interface ListInterface <E>{

    public void insertElement(int i, E x);
    public void addElement(E x);
    public E removeElement(int i);
    public E getElement(int i);
    public int getSize();
    public boolean isEmpty();
    public void removeAll();
    public void printAll();

}
