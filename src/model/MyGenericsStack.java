package model;

public class MyGenericsStack<T> {

	private int stackSize;
	private T[] stackArr;
	private int top;

	public MyGenericsStack(int size) {
		this.stackSize = size;
		// noinspection unchecked
		this.stackArr = (T[]) new Object[stackSize];
		this.top = -1;
	}

	public void push(T entry) {
		if (this.isStackFull()) {
			this.increaseStackCapacity();
		}
		this.stackArr[++top] = entry;
	}

	public T pop() throws Exception {
		if (this.isStackEmpty()) {
			throw new Exception("Stack is empty. Can not remove element.");
		}
		return this.stackArr[top--];
	}

	@SuppressWarnings("unused")
	public T getTop() {
		return stackArr[top];
	}

	private void increaseStackCapacity() {

		// noinspection unchecked
		T[] newStack = (T[]) new Object[this.stackSize * 2];
		if (stackSize >= 0)
			System.arraycopy(this.stackArr, 0, newStack, 0, stackSize);
		this.stackArr = newStack;
		this.stackSize = this.stackSize * 2;
	}

	public boolean isStackEmpty() {
		return (top == -1);
	}

	@SuppressWarnings("WeakerAccess")
	public boolean isStackFull() {
		return (top == stackSize - 1);
	}
}