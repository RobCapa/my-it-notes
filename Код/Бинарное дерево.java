import java.io.Serializable;
import java.util.*;

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    private static Entry<String> root;
    private static int countEntry;

    private void countEntryIncrement() {
        countEntry++;
    }

    private void countEntryDecrement() {
        countEntry--;
    }

    @Override
    public int size() {
        return countEntry;
    }

    /**
     * Добавление нового Entry
     * */
    @Override
    public void add(int index, String element) {
        countEntryIncrement();

        if (root == null) {
            root = new Entry<>(element, index);
            return;
        }

        addEntry(new Entry<>(element, index));
    }

    /**
     * Находит подходящего родителя.
     * Добавляет его в поле parent для newEntry.
     * Назначает ссылку на newEntry родителю в поле левого или правого потомка.
     */
    public void addEntry(Entry<String> newEntry) {
        Entry<String> parent = root.searchSuitableParent(newEntry.getIndex());
        newEntry.setParent(parent);
        if (newEntry.getIndex() < parent.getIndex()) {
            parent.setLeftChild(newEntry);
        } else {
            parent.setRightChild(newEntry);
        }
    }

    @Override
    public String get(int index) {
        return root.findEntry(index).getElementName();
    }

    /**
     * Изменение поля element для определенного Entry.
     * */
    @Override
    public String set(int index, String element) {
        root.findEntry(index).setElementName(element);
        return element;
    }

    /**
     * Находит entryForDelete.
     * Если это root, то запускает удаление корня,
     * Если нет, то обычное удаление вершины.
     * */
    @Override
    public String remove(int index) {
        Entry<String> entryForDelete = root.findEntry(index);
        countEntryDecrement();

        if (entryForDelete.getIndex() == root.getIndex()) {
            removeRoot(entryForDelete);
            return "Root";
        }

        removeVertex(entryForDelete);
        return "Vertex";
    }

    /**
     * Затирает ссылку на объект entryForDelete для его родителя.
     * Если у entryForDelete присутствовали потомки, отправляет их на добавление.
     * */
    private void removeVertex(Entry<String> entryForDelete) {
        Entry<String> parent = entryForDelete.getParent();

        if (entryForDelete.getIndex() < parent.getIndex()) {
            parent.setLeftChild(null);
        } else {
            parent.setRightChild(null);
        }

        if (!entryForDelete.availableToAddLeftChildren()) {
            addEntry(entryForDelete.getLeftChild());
        }
        if (!entryForDelete.availableToAddRightChildren()) {
            addEntry(entryForDelete.getRightChild());
        }
    }

    /**
     * Если у корня было оба потомка, то leftChild становится новым корнем, а rightChild отправляется на добавление.
     * Если был только один потомок, то он становится новым корнем.
     * Если потомков не было корень == null.
     * */
    private void removeRoot(Entry<String> entryForDelete) {
        if (!root.availableToAddLeftChildren() && !root.availableToAddRightChildren()) {
            root = entryForDelete.getLeftChild();
            addEntry(entryForDelete.getRightChild());
            return;
        }

        if (!root.availableToAddLeftChildren()) {
            root = entryForDelete.getLeftChild();
            return;
        } else if (!root.availableToAddRightChildren()) {
            root = entryForDelete.getRightChild();
            return;
        }

        root = null;
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "CustomTree{" +
                "root=" + root +
                '}';
    }

    private class Entry<T> implements Serializable {
        private String elementName;
        private final int index;
        private Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName, int index, Entry<T> parent) {
            this.elementName = elementName;
            this.index = index;
            this.parent = parent;
        }

        /**
         * Конструктор для корня.
         * */
        public Entry(String elementName, int index) {
            this(elementName, index, null);
        }

        public boolean availableToAddLeftChildren() {
            return leftChild == null;
        }

        public boolean availableToAddRightChildren() {
            return rightChild == null;
        }

        /**
         * Рукурсивный поиск родителя, подходящего для добавления потомка.
         * */
        public Entry<T> searchSuitableParent(int index) {
            if (index < this.index) {
                if (this.availableToAddLeftChildren()) {
                    return this;
                }
                return leftChild.searchSuitableParent(index);
            }
            if (this.availableToAddRightChildren()) {
                return this;
            }
            return rightChild.searchSuitableParent(index);
        }

        /**
         * Рекурсивный поиск подходящего по индексу Entry.
         * */
        public Entry<T> findEntry(int index) {
            if (index == this.index) {
                return this;
            }
            if (index < this.index) {
                return leftChild.findEntry(index);
            }
            return rightChild.findEntry(index);
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "elementName='" + elementName + '\'' +
                    ", index=" + index +
                    ", leftChild=" + leftChild +
                    ", rightChild=" + rightChild +
                    '}';
        }

        public void setLeftChild(Entry<T> leftChild) {
            this.leftChild = leftChild;
        }

        public void setRightChild(Entry<T> rightChild) {
            this.rightChild = rightChild;
        }

        public void setElementName(String elementName) {
            this.elementName = elementName;
        }

        public void setParent(Entry<T> parent) {
            this.parent = parent;
        }

        public String getElementName() {
            return elementName;
        }

        public int getIndex() {
            return index;
        }

        public Entry<T> getParent() {
            return parent;
        }

        public Entry<T> getLeftChild() {
            return leftChild;
        }

        public Entry<T> getRightChild() {
            return rightChild;
        }
    }
}