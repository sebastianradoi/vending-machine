package com.seba.exceptions;

public class NotEnoughCoinsForChange extends Exception {
	public NotEnoughCoinsForChange() {
		super("Not enough coins for change.");
	}
}
