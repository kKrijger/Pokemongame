package nl.delphinity.pokemon.model.general;

import nl.delphinity.pokemon.model.area.Area;
import nl.delphinity.pokemon.model.area.Pokecenter;
import nl.delphinity.pokemon.model.battle.Battle;
import nl.delphinity.pokemon.model.item.ItemType;
import nl.delphinity.pokemon.model.trainer.Badge;
import nl.delphinity.pokemon.model.trainer.GymLeader;
import nl.delphinity.pokemon.model.trainer.Trainer;

import java.util.*;

public class Game {

    private static final ArrayList<Area> areas = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);
    private static Trainer trainer = null;

    // set up the game in this static block
    static {

        // PEWTER City
        Pokecenter pewterCenter = new Pokecenter("Pewter City's Pokecenter");
        Area pewterCity = new Area("Pewter city", null, true, null, pewterCenter);
        pewterCity.setContainsPokemon(
                Arrays.asList(PokemonType.GRASS, PokemonType.FLYING, PokemonType.BUG, PokemonType.GROUND));

        // VIRIDIAN City
        Pokecenter viridianCenter = new Pokecenter("Viridian City's Pokecenter");
        Area viridianCity = new Area("Viridian city", null, true, pewterCity, viridianCenter);
        viridianCity.setContainsPokemon(
                Arrays.asList(PokemonType.GRASS, PokemonType.FLYING, PokemonType.BUG, PokemonType.GROUND));

        // PALLET Town
        Pokecenter palletCenter = new Pokecenter("Pallet Town's Pokecenter");
        Area palletTown = new Area("Pallet town", null, true, viridianCity, palletCenter);
        palletTown.setContainsPokemon(
                Arrays.asList(PokemonType.GRASS, PokemonType.FLYING, PokemonType.BUG, PokemonType.GROUND));

        areas.add(palletTown);
        areas.add(viridianCity);
        areas.add(pewterCity);

        // SETUP gym leaders
        GymLeader pewterLeader = new GymLeader("Bram", new Badge("Boulder Badge"), pewterCity);
        Pokemon b = new Pokemon(PokemonData.ONIX);
        b.setLevel(5);
        b.setOwner(pewterLeader);
        pewterLeader.setActivePokemon(b);
        pewterLeader.getPokemonCollection().add(b);
        pewterCity.setGymLeader(pewterLeader);
    

    GymLeader viridianLeader = new GymLeader("Rens", new Badge("Scrum Badge"), viridianCity);
    Pokemon r = new Pokemon(PokemonData.PERSIAN);
    r.setLevel(5);
    r.setOwner(viridianLeader);
    viridianLeader.setActivePokemon(r);
    viridianLeader.getPokemonCollection().add(r);
    viridianCity.setGymLeader(viridianLeader);

GymLeader palletLeader = new GymLeader("Marlon", new Badge("SEN Badge"), palletTown);
Pokemon m = new Pokemon(PokemonData.WEEPINBELL);
m.setLevel(5);
m.setOwner(palletLeader);
palletLeader.setActivePokemon(m);
palletLeader.getPokemonCollection().add(m);
palletTown.setGymLeader(palletLeader);
    }

    public static void main(String[] args) {
        System.out.println("Welcome new trainer, what's your name?");
        String name = sc.nextLine();
        trainer = new Trainer(name, areas.get(0));
        System.out.println("Hi, " + trainer.getName());

        Pokemon firstPokemon = chooseFirstPokemon();
        firstPokemon.setOwner(trainer);
        trainer.getPokemonCollection().add(firstPokemon);
        System.out.println("You now have " + trainer.getPokemonCollection().size() + " pokemon in your collection!");

        // game loop
        while (true) {
            showGameOptions();
        }
    }

    private static void showGameOptions() {
        System.out.println("What do you want to do?");
        System.out.println("1 ) Find Pokemon");
        System.out.println("2 ) My Pokemon");
        System.out.println("3 ) Inventory");
        System.out.println("4 ) Badges");
        System.out.println("5 ) Challenge " + trainer.getCurrentArea().getName() + "'s Gym Leader");
        System.out.println("6 ) Travel");
        System.out.println("7 ) Visit Pokecenter");
        System.out.println("8 ) Exit game");
        int action = sc.nextInt();
        sc.nextLine(); // consume the newline character
        switch (action) {
        case 1:
            findAndBattlePokemon();
            break;
        case 2:
            trainer.showPokemonColletion();
            break;
        case 3:
            ItemType item = showInventory();
            if (item != null) {
                trainer.useItem(item, null);
            }
            break;
        case 4:
            trainer.showBadges();
            break;
        case 5:
            if (trainer.getCurrentArea().getGymLeader() != null) {
                startGymBattle();
            } else {
                System.out.println("No Gym Leader in this town!");
            }
            break;
        case 6:
            Area area = showTravel();
            if (area != null) {
                trainer.travel(area);
            }
            break;
        case 7:
            trainer.visitPokeCenter(trainer.getCurrentArea().getPokecenter());
            break;
        case 8:
            quit();
            break;
        default:
            System.out.println("Sorry, that's not a valid option");
            break;
        }
    }

    // TODO: US-PKM-O-6
    private static void findAndBattlePokemon() {
        Pokemon randomPokemon = trainer.findPokemon();
        Battle battle = trainer.battle(trainer.getActivePokemon(), randomPokemon);
        battle.start();
    }
    
    private static Area showTravel() {
        Area travelTo = null;
        int index = 1;
        List<Area> travelToAreas = new ArrayList<>();

        for (Area area : areas) {
            if (!area.equals(trainer.getCurrentArea()) && area.isUnlocked()
                    && ((area.getNextArea() != null && area.getNextArea().equals(trainer.getCurrentArea()))
                            || trainer.getCurrentArea().getNextArea() != null
                                    && trainer.getCurrentArea().getNextArea().equals(area))) {
                travelToAreas.add(area);
            }
        }
        for (Area a : travelToAreas) {
            System.out.println(index + ") " + a.getName());
            index++;
        }
        System.out.println(index + ") Back");
        int choice = sc.nextInt();
        sc.nextLine(); // consume the newline character
        if (choice != index) {
            travelTo = travelToAreas.get(choice - 1);
        }
        return travelTo;
    }

    private static ItemType showInventory() {
        HashMap<ItemType, Integer> items = trainer.getInventory().getItems();
        Set<Map.Entry<ItemType, Integer>> entries = items.entrySet();
        int index = 1;
        for (Map.Entry<ItemType, Integer> entry : entries) {
            System.out.println(index + ") " + entry.getKey() + " " + entry.getValue());
            index++;
        }
        System.out.println(index + ") Back");
        int choice = sc.nextInt();
        sc.nextLine(); // consume the newline character
        if (choice != index) {
            return ItemType.values()[choice - 1];
        }
        return null;
    }

    // TODO: US-PKM-O-1
    private static Pokemon chooseFirstPokemon() {
        System.out.println("Please choose one of these three pokemon");
        System.out.println("1 ) Charmander");
        System.out.println("2 ) Bulbasaur");
        System.out.println("3 ) Squirtle");

        int choice = sc.nextInt();


        Pokemon chosenPokemon;
        switch (choice) {
        case 1:
            chosenPokemon = new Pokemon(PokemonData.CHARMANDER);
            break;
        case 2:
            chosenPokemon = new Pokemon(PokemonData.BULBASAUR);
            break;
        case 3:
            chosenPokemon = new Pokemon(PokemonData.SQUIRTLE);
            break;
        default:
            return chooseFirstPokemon();
        }
        trainer.setActivePokemon(chosenPokemon);
        return chosenPokemon;
    }

    // TODO: US-PKM-O-8
    public static void startGymBattle() {
        Battle trainerBattle = trainer.challengeTrainer(trainer.getCurrentArea().getGymLeader(), null);
        if (trainerBattle != null && trainerBattle.getWinner().getOwner().equals(trainer)) {
            if (trainerBattle.getEnemy().getOwner().getClass().equals(GymLeader.class)) {
                GymLeader gymLeader = (GymLeader) trainerBattle.getEnemy().getOwner();
                gymLeader.setDefeated(true);                
                Area nextArea = trainer.getCurrentArea().getNextArea();
                awardBadge(gymLeader.getBadge().getName());                
                if (nextArea != null) {
                    nextArea.setUnlocked(true);
                }
            }
        }
    }

    // TODO: US-PKM-O-9
	public static void awardBadge(String badgeName) {
		Badge badge = new Badge(badgeName);
		trainer.addBadge(badge);
	}

    public static void gameOver(String message) {
        System.out.println(message);
        System.out.println("Game over");
        quit();
    }

    // TODO: US-PKM-O-2:
    private static void quit() {
        System.out.println("You quit the game");
        System.exit(8);

    }
}
