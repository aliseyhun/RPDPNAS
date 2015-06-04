package nl.uva.creed.datastructures.binarytree;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree<T> {

	protected T value;

	protected BinaryTree<T> leftChild;

	protected BinaryTree<T> rightChild;

	protected BinaryTree<T> parent;

	public BinaryTree(T value, BinaryTree<T> leftChild, BinaryTree<T> rightChild) {
		super();
		this.value = value;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		if (leftChild != null)
			leftChild.parent = this;
		if (rightChild != null)
			rightChild.parent = this;
	}

	public BinaryTree(T value) {
		super();
		this.value = value;
	}

	public BinaryTree<T> getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(BinaryTree<T> leftChild) {
		this.leftChild = leftChild;
		if (leftChild != null)
			leftChild.parent = this;
		if (!isValid()) {
			throw new IllegalArgumentException("Loop in the tree!");
		}
	}

	public BinaryTree<T> getRightChild() {
		return rightChild;
	}

	public void setRightChild(BinaryTree<T> rightChild) {
		this.rightChild = rightChild;
		if (rightChild != null)
			rightChild.parent = this;
		if (!isValid()) {
			throw new IllegalArgumentException("Loop in the tree!");
		}
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public boolean isLeaf() {
		if (this.leftChild == null && this.rightChild == null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BinaryTree) {
			@SuppressWarnings("rawtypes")
			BinaryTree tree = (BinaryTree) obj;
			if (this.getValue() != null && tree.getValue() != null
					&& this.getValue().equals(tree.getValue())) {
				if (this.getRightChild().equals(tree.getRightChild())
						&& this.getLeftChild().equals(tree.getLeftChild())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getValue().hashCode() + this.getRightChild().hashCode()
				+ this.getLeftChild().hashCode();
	}

	public boolean isValid() {
		if (this.isLeaf()) {
			return true;
		} else if (this.leftChild != null && this.rightChild != null) {
			return !this.leftChild.contains(this)
					&& !this.rightChild.contains(this);
		} else if (this.leftChild == null) {
			return !this.rightChild.contains(this);
		} else {
			return !this.leftChild.contains(this);
		}

	}

	private boolean contains(BinaryTree piece) {
		if (this.leftChild != null) {
			if (leftChild == piece) {
				return true;
			}
			return this.leftChild.contains(piece);
		} else if (this.rightChild != null) {
			if (rightChild == piece) {
				return true;
			}
			return this.rightChild.contains(piece);
		} else {
			return false;
		}

	}

	public String read() {
		if (isLeaf()) {
			return value.toString();
		} else {
			if (this.leftChild != null & this.rightChild != null) {
				return this.leftChild.read() + this.value
						+ this.rightChild.read();

			} else if (this.leftChild != null & this.rightChild == null) {
				return this.leftChild.read() + this.value;

			} else {
				return this.value + this.rightChild.read();
			}
		}

	}

	public List<T> readToList() {
		if (isLeaf()) {
			ArrayList<T> list = new ArrayList<T>();
			list.add(value);
			return list;
		} else {
			if (this.leftChild != null & this.rightChild != null) {
				ArrayList<T> list = new ArrayList<T>();
				list.addAll(leftChild.readToList());
				list.add(this.value);
				list.addAll(this.rightChild.readToList());
				return list;

			} else if (this.leftChild != null & this.rightChild == null) {
				ArrayList<T> list = new ArrayList<T>();
				list.addAll(this.leftChild.readToList());
				list.add(this.value);
				return list;

			} else {
				ArrayList<T> list = new ArrayList<T>();
				list.add(this.value);
				list.addAll(this.rightChild.readToList());
				return list;
			}
		}

	}

	public int numberOfNodes() {
		return this.read().length();
	}

	public boolean isRoot() {
		return this.parent == null;
	}

	public BinaryTree<T> getParent() {
		return parent;
	}

}

// http://www.cis.upenn.edu/~matuszek/cit594-2005/Assignments/3-binary-tree-adt.html
