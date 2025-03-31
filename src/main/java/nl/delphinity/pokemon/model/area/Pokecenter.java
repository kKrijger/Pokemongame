package nl.delphinity.pokemon.model.area;

import nl.delphinity.pokemon.model.general.Pokemon;

import java.util.List;

public class Pokecenter {

    private final String name;

    public Pokecenter(String name) {
        this.name = name;
    }

	// TODO: US-PKM-O-12
	public void healPokemon(List<Pokemon> pokemonToHeal) {
	       for(Pokemon p : pokemonToHeal) {
	            p.setCurrentHp(p.getMaxHp());
	            System.out.println(p.getPokedata().name() + " " + p.getCurrentHp() + "/" + p.getMaxHp() + "HP");
	        }
	}
}