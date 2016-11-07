package com.cat.jpa.tool.jpa;

public final class Page {
	private static final int DEFAULT_NUMBER = 1;
	private static final int DEFAULT_SIZE = 10;

	private final int number;
	private final int size;

	public Page(int number, int size) {
		this.number = number;
		this.size = size;
	}

	public static Page of(int number, int size) {
		number = number < 1 ? DEFAULT_NUMBER : number;
		size = size < 1 ? DEFAULT_SIZE : size;
		return new Page(number, size);
	}

	public int offset() {
		return (number - 1) * size;
	}

	public int size() {
		return size;
	}
}