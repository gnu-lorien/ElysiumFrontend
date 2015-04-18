package com.dstevens.character;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dstevens.characters.PlayerCharacter;
import com.dstevens.characters.traits.backgrounds.CharacterBackground;
import com.dstevens.characters.traits.distinctions.flaws.CharacterFlaw;
import com.dstevens.characters.traits.distinctions.merits.CharacterMerit;
import com.dstevens.characters.traits.powers.disciplines.CharacterDiscipline;
import com.dstevens.characters.traits.powers.disciplines.ElderPower;
import com.dstevens.characters.traits.powers.disciplines.Technique;
import com.dstevens.characters.traits.powers.magic.necromancy.NecromanticRitual;
import com.dstevens.characters.traits.powers.magic.thaumaturgy.ThaumaturgicalRitual;
import com.dstevens.characters.traits.skills.CharacterSkill;
import com.google.gson.Gson;

import static com.dstevens.collections.Lists.sort;

public class DisplayablePlayerCharacter {

	public final Integer id;
	public final String name;
	public final int setting;
	public final int status;
	public final int approval;
	public final Integer clan;
	public final Integer bloodline;
	public final List<Integer> inClanDisciplines;
	public final int physicalAttribute;
	public final int socialAttribute;
	public final int mentalAttribute;
	public final List<Integer> physicalAttributeFocuses;
	public final List<Integer> socialAttributeFocuses;
	public final List<Integer> mentalAttributeFocuses;
	public final List<DisplayableTrait> skills;
	public final List<DisplayableTrait> backgrounds;
	public final List<DisplayableTrait> disciplines;
	public final List<DisplayableTrait> techniques;
	public final List<DisplayableTrait> elderPowers;
	public final List<DisplayableTrait> necromanticRituals;
	public final List<DisplayableTrait> thaumaturgicalRituals;
	public final List<DisplayableTrait> merits;
	public final List<DisplayableTrait> flaws;
	
	//Jackson only
    @Deprecated
	private DisplayablePlayerCharacter() {
		this(null, null, -1, -1, -1, null, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, null, null);
	}
    
    private DisplayablePlayerCharacter(Integer id, String name, int setting, int status, int approval, Integer clan, Integer bloodline, List<Integer> inClanDisciplines,
    		                           int physicalAttribute, int socialAttribute, int mentalAttribute,
    		                           List<Integer> physicalAttributeFocuses, List<Integer> socialAttributeFocuses, List<Integer> mentalAttributeFocuses,
    		                           List<DisplayableTrait> skills, 
    		                           List<DisplayableTrait> backgrounds, 
    		                           List<DisplayableTrait> disciplines, 
    		                           List<DisplayableTrait> techniques, 
    		                           List<DisplayableTrait> elderPowers,
    		                           List<DisplayableTrait> necromanticRituals, 
    		                           List<DisplayableTrait> thaumaturgicalRituals,
    		                           List<DisplayableTrait> merits, 
    		                           List<DisplayableTrait> flaws) {
		this.id = id;
		this.name = name;
		this.setting = setting;
		this.status = status;
		this.approval = approval;
		this.clan = clan;
		this.bloodline = bloodline;
		this.inClanDisciplines = inClanDisciplines;
		this.physicalAttribute = physicalAttribute;
		this.socialAttribute = socialAttribute;
		this.mentalAttribute = mentalAttribute;
		this.physicalAttributeFocuses = physicalAttributeFocuses;
		this.socialAttributeFocuses = socialAttributeFocuses;
		this.mentalAttributeFocuses = mentalAttributeFocuses;
		this.skills = skills;
		this.backgrounds = backgrounds;
		this.disciplines = disciplines;
		this.techniques = techniques;
		this.elderPowers = elderPowers;
		this.necromanticRituals = necromanticRituals;
		this.thaumaturgicalRituals = thaumaturgicalRituals;
		this.merits = merits;
		this.flaws = flaws;
    }
	
	public static Function<PlayerCharacter, DisplayablePlayerCharacter> fromCharacter() {
		return (PlayerCharacter t) -> new DisplayablePlayerCharacter(t.getId(), t.getName(), 
																	 t.getSetting().ordinal(), 
				                                                     t.getCurrentStatus().status().ordinal(), 
				                                                     t.getApprovalStatus().ordinal(), 
				                                                     Optional.ofNullable(t.getClan()).map((Enum<?> e) -> e.ordinal()).orElse(null),
				                                                     Optional.ofNullable(t.getBloodline()).map((Enum<?> e) -> e.ordinal()).orElse(null),
				                                                     t.getInClanDisciplines().stream().map((Enum<?> e) -> e.ordinal()).collect(Collectors.toList()),
				                                                     t.getPhysicalAttribute(),
				                                                     t.getSocialAttribute(),
				                                                     t.getMentalAttribute(),
				                                                     sort(t.getPhysicalAttributeFocuses().stream().map((Enum<?> e) -> e.ordinal()).collect(Collectors.toList())),
				                                                     sort(t.getSocialAttributeFocuses().stream().map((Enum<?> e) -> e.ordinal()).collect(Collectors.toList())),
				                                                     sort(t.getMentalAttributeFocuses().stream().map((Enum<?> e) -> e.ordinal()).collect(Collectors.toList())),
				                                                     sort(t.getSkills().stream().map((CharacterSkill m) -> new DisplayableTrait(m.trait().ordinal(), m.rating(), m.getSpecialization(), m.getFocuses())).collect(Collectors.toList())),
				                                                     sort(t.getBackgrounds().stream().map((CharacterBackground m) -> new DisplayableTrait(m.trait().ordinal(), m.rating(), m.getSpecialization(), m.getFocuses())).collect(Collectors.toList())),
				                                                     sort(t.getDisciplines().stream().map((CharacterDiscipline m) -> new DisplayableTrait(m.trait().ordinal(), m.rating())).collect(Collectors.toList())),
				                                                     sort(t.getTechniques().stream().map((Technique m) -> new DisplayableTrait(m.ordinal())).collect(Collectors.toList())),
				                                                     sort(t.getElderPowers().stream().map((ElderPower m) -> new DisplayableTrait(m.ordinal())).collect(Collectors.toList())),
				                                                     sort(t.getNecromanticRituals().stream().map((NecromanticRitual m) -> new DisplayableTrait(m.ordinal())).collect(Collectors.toList())),
				                                                     sort(t.getThaumaturgicalRituals().stream().map((ThaumaturgicalRitual m) -> new DisplayableTrait(m.ordinal())).collect(Collectors.toList())),
				                                                     sort(t.getMerits().stream().map((CharacterMerit m) -> new DisplayableTrait(m.trait().ordinal(), m.getSpecialization())).collect(Collectors.toList())),
				                                                     sort(t.getFlaws().stream().map((CharacterFlaw m) -> new DisplayableTrait(m.trait().ordinal(), m.getSpecialization())).collect(Collectors.toList()))
				                                                     );
	}
	
	public String serialized() {
		return new Gson().toJson(this);
	}
	
}