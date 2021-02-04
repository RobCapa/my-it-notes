import java.io.Serializable;
import java.util.*;

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    private static com.javarush.task.task20.task2028.CustomTree.Entry<String> root;
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
            root = new com.javarush.task.task20.task2028.CustomTree.Entry<>(element, index);
            return;
        }

        addEntry(new com.javarush.task.task20.task2028.CustomTree.Entry<>(element, index));
    }

    /**
     * Находит подходящего родителя.
     * Добавляет его в поле parent для newEntry.
     * Назначает ссылку на newEntry родителю в поле левого или правого потомка.
     */
    public void addEntry(com.javarush.task.task20.task2028.CustomTree.Entry<String> newEntry) {
        com.javarush.task.task20.task2028.CustomTree.Entry<String> parent = root.searchSuitableParent(newEntry.getIndex());
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
        com.javarush.task.task20.task2028.CustomTree.Entry<String> entryForDelete = root.findEntry(index);
        countEntryDecrement();

        if (entryForDelete.getIndex() == root.getIndex()) {
            removeRoot(entryForDelete);
            return "Root";
        }

        removeVertex(entryForDelete);
        return "Vertex";
    }

    /**
     * Затирет ссылку на объект entryForDelete для его родителя.
     * Если у entryForDelete присутствовали потомки, отправляет их на добавление.
     * */
    private void removeVertex(com.javarush.task.task20.task2028.CustomTree.Entry<String> entryForDelete) {
        com.javarush.task.task20.task2028.CustomTree.Entry<String> parent = entryForDelete.getParent();

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
    private void removeRoot(com.javarush.task.task20.task2028.CustomTree.Entry<String> entryForDelete) {
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
        private com.javarush.task.task20.task2028.CustomTree.Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName, int index, com.javarush.task.task20.task2028.CustomTree.Entry<T> parent) {
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
        public com.javarush.task.task20.task2028.CustomTree.Entry<T> searchSuitableParent(int index) {
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
        public com.javarush.task.task20.task2028.CustomTree.Entry<T> findEntry(int index) {
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

        public void setLeftChild(com.javarush.task.task20.task2028.CustomTree.Entry<T> leftChild) {
            this.leftChild = leftChild;
        }

        public void setRightChild(com.javarush.task.task20.task2028.CustomTree.Entry<T> rightChild) {
            this.rightChild = rightChild;
        }

        public void setElementName(String elementName) {
            this.elementName = elementName;
        }

        public void setParent(com.javarush.task.task20.task2028.CustomTree.Entry<T> parent) {
            this.parent = parent;
        }

        public String getElementName() {
            return elementName;
        }

        public int getIndex() {
            return index;
        }

        public com.javarush.task.task20.task2028.CustomTree.Entry<T> getParent() {
            return parent;
        }

        public com.javarush.task.task20.task2028.CustomTree.Entry<T> getLeftChild() {
            return leftChild;
        }

        public com.javarush.task.task20.task2028.CustomTree.Entry<T> getRightChild() {
            return rightChild;
        }
    }
}