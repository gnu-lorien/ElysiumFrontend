package com.dstevens.web.user.controllers;

import java.util.function.Function;

import com.dstevens.character.trait.DisplayableTraitType;
import com.dstevens.character.trait.change.TraitChange;
import com.dstevens.character.trait.change.TraitChangeFactory;

public class RawTraitChange {

	public int traitChange;
	public DisplayableTraitType traitType;
	public int trait;
	public Integer rating;
	public String specialization;
	
	public static Function<RawTraitChange, TraitChange> toTraitChangeUsing(final TraitChangeFactory traitChangeFactory) {
		return (RawTraitChange t) -> TraitChanges.values()[t.traitChange].using(traitChangeFactory, t);
	}
	
}
