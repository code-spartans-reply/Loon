package com.reply.spartans.datacenter.model;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Objects;

public class RemainingSlotSpace {
	private final Slot baseSlot;

	private int remainingSpace;

	public RemainingSlotSpace(Slot baseSlot) {
		this.baseSlot = baseSlot;
		this.remainingSpace = baseSlot.getDimension();
	}

	public void allocate(int slots) {
		checkArgument(slots <= this.remainingSpace);
		this.remainingSpace -= slots;
	}

	public boolean isFull() {
		return this.remainingSpace == 0;
	}

	public int getRemainingSpace() {
		return remainingSpace;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		final RemainingSlotSpace other = (RemainingSlotSpace) obj;
		return Objects.equals(baseSlot, other.baseSlot) && this.remainingSpace == other.remainingSpace;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.baseSlot) & this.remainingSpace;
	}

	public Slot getBaseSlot() {
		return baseSlot;
	}

}