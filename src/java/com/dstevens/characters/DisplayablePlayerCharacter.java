package com.dstevens.characters;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class DisplayablePlayerCharacter {

	public final String id;
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
	public final Set<Integer> physicalAttributeFocuses;
	public final Set<Integer> socialAttributeFocuses;
	public final Set<Integer> mentalAttributeFocuses;
	public final Set<DisplayableCharacterSkill> skills;
	public final Set<DisplayableCharacterBackground> backgrounds;
	public final Set<DisplayableCharacterDiscipline> disciplines;
	public final Set<DisplayableTechnique> techniques;
	public final Set<DisplayableElderPower> elderPowers;
	public final Set<DisplayableNecromanticRitual> necromanticRituals;
	public final Set<DisplayableThaumaturgicalRitual> thaumaturgicalRituals;
	
	//Jackson only
    @Deprecated
	private DisplayablePlayerCharacter() {
		this(null, null, -1, -1, -1, null, null, null, 0, 0, 0, null, null, null, null, null, null, null, null, null, null);
	}
    
    private DisplayablePlayerCharacter(String id, String name, int setting, int status, int approval, Integer clan, Integer bloodline, List<Integer> inClanDisciplines,
    		                           int physicalAttribute, int socialAttribute, int mentalAttribute,
    		                           Set<Integer> physicalAttributeFocuses, Set<Integer> socialAttributeFocuses, Set<Integer> mentalAttributeFocuses,
    		                           Set<DisplayableCharacterSkill> skills, Set<DisplayableCharacterBackground> backgrounds, 
    		                           Set<DisplayableCharacterDiscipline> disciplines, 
    		                           Set<DisplayableTechnique> techniques, Set<DisplayableElderPower> elderPowers,
    		                           Set<DisplayableNecromanticRitual> necromanticRituals, Set<DisplayableThaumaturgicalRitual> thaumaturgicalRituals) {
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
				                                                     t.getPhysicalAttributeFocuses().stream().map((Enum<?> e) -> e.ordinal()).collect(Collectors.toSet()),
				                                                     t.getSocialAttributeFocuses().stream().map((Enum<?> e) -> e.ordinal()).collect(Collectors.toSet()),
				                                                     t.getMentalAttributeFocuses().stream().map((Enum<?> e) -> e.ordinal()).collect(Collectors.toSet()),
				                                                     t.getSkills().stream().map(DisplayableCharacterSkill.fromSkill()).collect(Collectors.toSet()),
				                                                     t.getBackgrounds().stream().map(DisplayableCharacterBackground.fromBackground()).collect(Collectors.toSet()),
				                                                     t.getDisciplines().stream().map(DisplayableCharacterDiscipline.fromDiscipline()).collect(Collectors.toSet()),
				                                                     t.getTechniques().stream().map(DisplayableTechnique.fromTechnique()).collect(Collectors.toSet()),
				                                                     t.getElderPowers().stream().map(DisplayableElderPower.fromElderPower()).collect(Collectors.toSet()),
				                                                     t.getNecromanticRituals().stream().map(DisplayableNecromanticRitual.fromNecromanticRitual()).collect(Collectors.toSet()),
				                                                     t.getThaumaturgicalRituals().stream().map(DisplayableThaumaturgicalRitual.fromThaumaturgicalRitual()).collect(Collectors.toSet())
				                                                     );
	}
	
	public String serialized() {
		return new Gson().toJson(this);
	}
	
}
