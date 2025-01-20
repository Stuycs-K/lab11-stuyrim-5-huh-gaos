import java.util.*;

public class Game {
  private static final int WIDTH = 80;
  private static final int HEIGHT = 30;
  private static final int BORDER_COLOR = Text.BLACK;
  private static final int BORDER_BACKGROUND = Text.WHITE + Text.BACKGROUND;

  private static final int MIDBAR = 50;

  private static String COMMANDLIST = "";

  private static List<String> NAME_LIST = new ArrayList<String>(
      Arrays.asList("John", "Linda", "Fred", "Kelly",
          "Carter", "Kat", "Jun", "Emile",
          "Jorge", "Miranda", "Johnson", "Douglas", "Jerome", "Alice",
          "Buck", "Locke", "Tanaka", "Vale"));

  static {
    // randomize name list
    Collections.shuffle(NAME_LIST);
  }

  private static int nameIndex = 0;

  private static final ArrayList<Adventurer> party = new ArrayList<Adventurer>();
  private static final ArrayList<Adventurer> enemies = new ArrayList<Adventurer>();

  public static void main(String[] args) {
    Text.clear();
    run();
  }

  // Display the borders of your screen that will not change.
  // Do not write over the blank areas where text will appear or parties will
  // appear.
  public static void drawBackground() {
    String border = " ";
    border = Text.colorize(border, BORDER_BACKGROUND);

    drawText(border.repeat(WIDTH), 0, 1);

    drawText(border.repeat(WIDTH), 6, 1);

    drawText(border.repeat(WIDTH), HEIGHT - 7, 1);

    for (int i = 1; i < HEIGHT - 1; i++) {
      drawText(border, i, 1);
      drawText(border, i, 80);

      if (6 < i && i < HEIGHT - 7) {
        drawText(border, i, MIDBAR);
      }
    }

    drawText(border.repeat(WIDTH), HEIGHT - 2, 1);

    String preprompt = "(a)ttack #; (sp)ecial #; (su)pport #; (q)uit";
    drawText(preprompt, 29, 1);
  }

  // Display a line of text starting at
  // (columns and rows start at 1 (not zero) in the terminal)
  // use this method in your other text drawing methods to make things simpler.
  public static void drawText(String s, int startRow, int startCol) {
    Text.go(startRow, startCol);
    System.out.print(s);
  }

  /*
   * Use this method to place text on the screen at a particular location.
   * When the length of the text exceeds width, continue on the next line
   * for up to height lines.
   * All remaining locations in the text box should be written with spaces to
   * clear previously written text.
   * 
   * @param row the row to start the top left corner of the text box.
   * 
   * @param col the column to start the top left corner of the text box.
   * 
   * @param width the number of characters per row
   * 
   * @param height the number of rows
   */
  public static void TextBox(int row, int col, int width, int height, String text) {
    Text.clear(row, col, width, height);
    String[] words = text.split(" ");
    int i = 0;
    int startRow = row;
    int currCol = col;
    int mark = 0;
    Text.go(row, col);
    for (i = 0; i < words.length; i++) {
      if (currCol + words[i].length() + 1 < col + width) {
        drawText(words[i] + " ", row, currCol);
        currCol += words[i].length() + 1;
      } else if (currCol + words[i].length() < col + width) {
        drawText(words[i], row, currCol);
        currCol += words[i].length();
      } else {
        if (row == startRow)
          mark = i;
        if (row == startRow + height - 1) {
          Text.wait(450);
          Text.clear(startRow, col, width, height);
          TextBox(startRow, col, width, height, combine(words, " ", mark));
          return;
        }
        row++;
        currCol = col;
        drawText(words[i] + " ", row, currCol);
        currCol += words[i].length() + 1;
      }
    }
    /*
     * if (text.length() % width != 0) {
     * if (row == startRow + height) {
     * Text.wait(450); // 300
     * Text.clear(startRow, col, width, height);
     * TextBox(startRow, col, width, height, text.substring(width));
     * return;
     * } else {
     * drawText(text.substring(i), row, col);
     * }
     * }
     */
    // drawBackground();
  }

  private static String combine(String[] parts, String pattern, int start) {
    String total = "";

    for (int i = start; i < parts.length; i++) {
      total += parts[i];
      if (i != parts.length - 1)
        total += pattern;
    }

    return total;
  }

  private static String returnRandomName() {
    String out = NAME_LIST.get(nameIndex);
    nameIndex++;
    return out;
  }

  // return a random adventurer (choose between all available subclasses)
  // feel free to overload this method to allow specific names/stats.
  public static Adventurer createRandomAdventurer() {
    double rN = Math.random();
    String name = returnRandomName();
    if (rN < .1) {
      return new Boss(name);
    } else if (rN < .4) {
      return new Pathfinder(name);
    } else if (rN < .7) {
      return new CodeWarrior(name);
    } else {
      return new Warrior(name);
    }
  }

  public static Adventurer createRandomAdventurer(boolean boss) {
    String name = returnRandomName();
    if (boss) {
      return new Boss(name);
    }
    double rN = Math.random();
    if (rN < .33) {
      return new Pathfinder(name);
    } else if (rN < .67) {
      return new CodeWarrior(name);
    } else {
      return new Warrior(name);
    }
  }

  public static void drawParty(Adventurer c, int startRow, int centerCol) {
    int startCol = centerCol - c.toString().length() / 2;

    if (c.status()) {
      String name = c.toString() + " (" + c.getClass().getSimpleName() + ")";
      drawText(name, startRow, startCol);
      drawText("HP: " + colorByPercent(c.getHP(), c.getmaxHP()), startRow + 1, startCol);
      drawText(c.getSpecialName() + ": " + c.getSpecial() + " / " + c.getSpecialMax(), startRow + 2, startCol);
    } else {
      String name = c.toString() + " (" + c.getClass().getSimpleName() + ")";
      drawText(Text.colorize(name, Text.RED), startRow, startCol);
      drawText(Text.colorize("DEAD", Text.RED), startRow + 2, startCol);
    }
  }

  /*
   * Display a List of 2-4 adventurers on the rows row through row+3 (4 rows max)
   * Should include Name HP and Special on 3 separate lines.
   * Note there is one blank row reserved for your use if you choose.
   * Format:
   * Bob Amy Jun
   * HP: 10 HP: 15 HP:19
   * Caffeine: 20 Mana: 10 Snark: 1
   * ***THIS ROW INTENTIONALLY LEFT BLANK***
   */
  public static void drawParty(ArrayList<Adventurer> party, int startRow) {
    Text.clear(startRow, 2, 78, 4);
    if (party.size() == 1) {
      drawParty(party.get(0), startRow, 40);
    } else if (party.size() == 2) {
      drawParty(party.get(0), startRow, 20);
      drawParty(party.get(1), startRow, 50);
    } else if (party.size() == 3) {
      drawParty(party.get(0), startRow, 6);
      drawParty(party.get(1), startRow, 33);
      drawParty(party.get(2), startRow, 60);
    }

  }
  
  public static void drawInfo(int adv, int startRow, boolean partyTurn) {
	if (partyTurn) {
		drawText(party.get(adv).getName() + " (" + party.get(adv).getClass().getSimpleName() + ")", startRow, MIDBAR + 1);
		
		if (party.get(adv).getClass().isInstance(new CodeWarrior())) {
			drawText(Text.colorize("Attack: ", Text.CYAN), startRow + 2, MIDBAR + 1);
			TextBox(startRow + 3, MIDBAR + 3, 27, 3, "applies 2-7HP damage to opponent and restores self 2 special points.");
			
			drawText(Text.colorize("Special Attack: ", new int[] {245, 120, 0}) + party.get(adv).getSpecialName(), startRow + 6, MIDBAR + 1);
			TextBox(startRow + 7, MIDBAR + 3, 27, 3, "applies 3-27HP damage to opponent and drains self 8 special points.");
			
			drawText(Text.colorize("Support: ", new int[] {244, 202, 0}), startRow + 10, MIDBAR + 1);
			TextBox(startRow + 11, MIDBAR + 3, 27, 3, "restores ally 5 special points or restores self 1HP and 6 special points");
		} else if (party.get(adv).getClass().isInstance(new Warrior())) {
			drawText(Text.colorize("Attack: ", Text.CYAN), startRow + 2, MIDBAR + 1);
			TextBox(startRow + 3, MIDBAR + 3, 27, 3, "applies 2HP damage to opponent and restores self 4 special points.");
			
			drawText(Text.colorize("Special Attack: ", new int[] {245, 120, 0}) + party.get(adv).getSpecialName(), startRow + 6, MIDBAR + 1);
			TextBox(startRow + 7, MIDBAR + 3, 27, 4, "applies 90-10%HP damage to opponent dependent on self special points and drains self 1 special point.");
			
			drawText(Text.colorize("Support: ", new int[] {244, 202, 0}), startRow + 11, MIDBAR + 1);
			TextBox(startRow + 12, MIDBAR + 3, 27, 2, "restores selected 2 special points and 3HP.");
		} else if (party.get(adv).getClass().isInstance(new Pathfinder())) {
			drawText(Text.colorize("Attack: ", Text.CYAN), startRow + 2, MIDBAR + 1);
			TextBox(startRow + 3, MIDBAR + 3, 27, 2, "applies 2HP damage to opponent.");
			
			drawText(Text.colorize("Special Attack: ", new int[] {245, 120, 0}) + party.get(adv).getSpecialName(), startRow + 5, MIDBAR + 1);
			TextBox(startRow + 6, MIDBAR + 3, 27, 3, "applies 8HP damage to opponent and drains self 2 special points.");
			
			drawText(Text.colorize("Support: ", new int[] {244, 202, 0}), startRow + 9, MIDBAR + 1);
			TextBox(startRow + 10, MIDBAR + 3, 27, 3, "restores selected 5HP and drains self 1 special point.");
		}
	} else {
		TextBox(7, MIDBAR + 1, WIDTH - MIDBAR - 1, 2, "Enemy " + enemies.get(adv).getName() + " is up. Press enter to see enemy attack.");
	}
	
	String summary = "";
	for (int i = 0; i < party.size(); i++) {
		Adventurer a = party.get(i);
		if (a.status()) {
			if (i == adv && partyTurn) {
				summary += Text.colorize("O", Text.GREEN + Text.BACKGROUND);
			} else {
				summary += Text.colorize(" ", Text.GREEN + Text.BACKGROUND);
			}
		} else {
			summary += Text.colorize(" ", Text.RED + Text.BACKGROUND);
		}
	}
	drawText("your party: " + summary, 21, MIDBAR + 1);
	
	summary = "";
	for (int i = 0; i < enemies.size(); i++) {
		Adventurer a = enemies.get(i);
		if (a.status()) {
			if (i == adv && !partyTurn) {
				summary += Text.colorize("O", Text.GREEN + Text.BACKGROUND);
			} else {
				summary += Text.colorize(" ", Text.GREEN + Text.BACKGROUND);
			}
		} else {
			summary += Text.colorize(" ", Text.RED + Text.BACKGROUND);
		}
	}
	drawText("enemy party: " + summary, 22, MIDBAR + 1);
  }

  // Use this to create a colorized number string based on the % compared to the
  // max value.
  public static String colorByPercent(int hp, int maxHP) {
    double dHP = (double) hp / (double) maxHP;
    int textColor;

    String output = String.format("%2s", hp + "") + " / " + String.format("%2s", maxHP + "");

    if (dHP <= .25) {
      textColor = Text.RED;
    } else if (dHP <= .75) {
      textColor = Text.YELLOW;
    } else {
      textColor = Text.WHITE;
    }

    output = Text.colorize(output, textColor);
    // COLORIZE THE OUTPUT IF HIGH/LOW:
    // under 25% : red
    // under 75% : yellow
    // otherwise : white
    return output;
  }

  // Display the party and enemies
  // Do not write over the blank areas where text will appear.
  // Place the cursor at the place where the user will by typing their input at
  // the end of this method.
  public static void drawScreen() {

    drawBackground();

    drawParty(party, 24);

    drawParty(enemies, 2);

    // TextBox(7, 2, 48, 18, COMMANDLIST);

    Text.clear(7, 2, 48, 16);

    String[] listCMD = COMMANDLIST.split("\n");
    int numLines = 0;
    for (int i = 0; i < listCMD.length; i++) {
      String current = ">" + listCMD[i];
      if (current.length() > 2 * (MIDBAR - 2)) {
        numLines += 2;
      } else if (current.length() > MIDBAR - 2) {
        numLines++;
      }
      numLines++;
    }

    while (numLines > 15) {
      int index = COMMANDLIST.indexOf("\n");
      COMMANDLIST = COMMANDLIST.substring(index + 1);
      listCMD = Arrays.copyOfRange(listCMD, 1, listCMD.length);

      numLines = 0;
      for (int i = 0; i < listCMD.length; i++) {
        String current = ">" + listCMD[i];
        if (current.length() > 2 * (MIDBAR - 2)) {
          numLines += 2;
        } else if (current.length() > MIDBAR - 2) {
          numLines++;
        }
        numLines++;
      }
    }

    int row = 7;
    for (int i = 7; i < 7 + listCMD.length; i++) {
      // String out = listCMD[i - 7] + " ".repeat(47 - listCMD[i-7].length());
      String out = ">" + listCMD[i - 7];
      if (out.length() > 2 * (MIDBAR - 2)) {
        TextBox(row, 2, MIDBAR - 2, 3, out);
        row += 2;
      } else if (out.length() > (MIDBAR - 2)) {
        TextBox(row, 2, MIDBAR - 2, 2, out);
        row++;
      } else {
        drawText(out, row, 2);
      }
      row++;
    }

    Text.go(27, 2);

  }

  public static String userInput(Scanner in) {
    Text.go(27, 2);

    Text.showCursor();

    String input = in.nextLine();

    Text.clear(27, 2, input.length(), 1);

    return input;
  }

  public static void quit() {
    // draw quit screen
    Text.clear();
    drawText(Text.colorize("Game  Over", Text.RED), 7, 36);

    Text.wait(450);

    Text.reset();
    Text.showCursor();
    Text.go(32, 1);
  }

  public static void win() {
    // draw win screen
    Text.clear();
    drawText(Text.colorize("You  Win!", Text.GREEN), 7, 36);

    Text.wait(450);

    Text.reset();
    Text.showCursor();
    Text.go(32, 1);
  }

  public static void run() {

    // Clear and initialize
    Text.hideCursor();
    Text.clear();
    drawBackground();

    // Things to attack:
    // Make an ArrayList of Adventurers and add 1-3 enemies to it.
    // If only 1 enemy is added it should be the boss class.
    // start with 1 boss and modify the code to allow 2-3 adventurers later.

    // incomplete
    int enemyPartySize = (int) (Math.random() * 3) + 1;
    for (int i = 0; i < enemyPartySize; i++) { // random enemy party
      if (enemyPartySize == 1) { // boss class
        enemies.add(createRandomAdventurer(true));
      } else {
        enemies.add(createRandomAdventurer(false));
      }
    }

    drawText("Enter a number 2-3 for the size of your party.", 30, 0);
    Text.go(27, 2);
    String partySize = userInput(new Scanner(System.in));
    while (partySize.compareTo("2") < 0 || partySize.compareTo("3") > 0) { // user determined party size
      if (partySize.startsWith("q") || partySize.startsWith("quit")) {
        return;
      }

      Text.clear(30, 1, 80, 1);
      drawText("Invalid entry. Enter a number 2-3 for the size of your party.", 30, 1);
      Text.clear(27, 2, 78, 1);
      partySize = userInput(new Scanner(System.in));
    }
    Text.clear(30, 1, 80, 1);

    drawText("Enter \"custom\" for a custom team or \"random\" for a randomly generated team.", 30, 1);
    String partyType = userInput(new Scanner(System.in)).toLowerCase().trim();
    while (!(partyType.startsWith("custom") || partyType.startsWith("random") || partyType.length() == 0)) { // user
                                                                                                             // determine
                                                                                                             // random
                                                                                                             // party or
                                                                                                             // custom
                                                                                                             // party
      if (partyType.startsWith("q") || partyType.startsWith("quit")) {
        return;
      }

      Text.clear(30, 1, 80, 1);
      drawText("Invalid entry. Enter \"custom\", \"random\", or press enter.", 30, 1);
      Text.clear(27, 2, 78, 1);
      partyType = userInput(new Scanner(System.in)).toLowerCase().trim();
    }
    Text.clear(30, 1, 80, 1);

    if (partyType.startsWith("random") || partyType.length() == 0) {
      for (int i = 0; i < Integer.valueOf(partySize); i++) { // player party
        party.add(createRandomAdventurer(false));
      }
    } else {
      for (int i = 0; i < Integer.valueOf(partySize); i++) {
        drawText("Enter \"Code Warrior\", \"Warrior\", or \"Pathfinder\" followed by member " + i + "'s name.", 30, 1);
        String advType = userInput(new Scanner(System.in)).toLowerCase().trim();
        while (!(advType.startsWith("code warrior") || advType.startsWith("warrior")
            || advType.startsWith("pathfinder"))) { // user determine random party or custom party
          if (advType.startsWith("q") || advType.startsWith("quit")) {
            return;
          }

          Text.clear(30, 1, 80, 1);
          drawText("Invalid entry. Enter \"Code Warrior\", \"Warrior\", or \"Pathfinder\".", 30, 1);
          Text.clear(27, 2, 78, 1);
          advType = userInput(new Scanner(System.in)).toLowerCase().trim();
        }
        Text.clear(30, 1, 80, 1);

        if (advType.indexOf("code warrior") != -1) {
          String name = advType.substring(12).trim();
          name = name.substring(0, 1).toUpperCase() + name.substring(1); // capitalize first letter
          party.add(new CodeWarrior(name));
        } else if (advType.indexOf("warrior") != -1) {
          String name = advType.substring(7).trim();
          name = name.substring(0, 1).toUpperCase() + name.substring(1); // capitalize first letter
          party.add(new Warrior(name));
        } else if (advType.indexOf("pathfinder") != -1) {
          String name = advType.substring(10).trim();
          name = name.substring(0, 1).toUpperCase() + name.substring(1); // capitalize first letter
          party.add(new Pathfinder(name));
        }
      }
    }

    boolean partyTurn = true;
    int whichPlayer = 0;
    int turn = 0;
    String input = ""; // blank to get into the main loop.
    Scanner in = new Scanner(System.in);
    // Draw the window border

    // You can add parameters to draw screen!
    drawScreen();// initial state.

    // Main loop
    while (!(input.equalsIgnoreCase("q") || input.equalsIgnoreCase("quit"))) {
      Text.clear(27, 2, 78, 1);
	  Text.clear(7, 51, 29, 16);
	  
	  /* 
      // example debug statment
      TextBox(18, 51, 48, 4,
      "input: " + input + " partyTurn:" + partyTurn + " whichPlayer=" + whichPlayer
      + " whichOpp=" + whichOpponent); */
	  
	  
      int totalPlayers = party.size() + enemies.size();
      int nextPlayer = whichPlayer % totalPlayers;
	  
      if (nextPlayer < party.size()) {
        // currently ally
		partyTurn = true;
		drawInfo(nextPlayer, 7, partyTurn);
      } else {
        // last turn was the last ally. it's now enemy
        partyTurn = false;
		drawInfo(nextPlayer - party.size(), 7, partyTurn);
      }

      /*
       * TextBox(7, 51, 20, 1,
       * "whichPlayer=" + whichPlayer);
       * TextBox(8, 51, 20, 1,
       * "nextPlayer=" + nextPlayer);
       * TextBox(15, 51, 20, 1, "nextPlayer: " + nextPlayer);
       * TextBox(16, 51, 20, 1, "partySize: " + party.size());
       * TextBox(17, 51, 20, 1, "totalPlayers: " + totalPlayers);
       */

      if (nextPlayer < party.size()) {
        // next one is ally
        Adventurer nextAdv = party.get(nextPlayer);
        if (!nextAdv.status()) { // skip
          // TextBox(11, 51, 20, 1, nextAdv.getName() + " is dead.");
          whichPlayer++;
          continue;
        } else {
          Text.clear(30, 1, 80, 1);
          drawText(Text.colorize("Enter command for " + party.get(nextPlayer) + ": attack/special/quit", Text.MAGENTA),
              30, 1);
        }
      } else {
        // next one is enemy
        Adventurer nextAdv = enemies.get(nextPlayer - party.size());
        if (!nextAdv.status()) {
          // TextBox(11, 51, 20, 1, nextAdv.getName() + " is dead.");
          whichPlayer++;
          continue;
        } else {
          Text.clear(30, 1, 80, 1);
          drawText(Text.colorize("Press enter to see enemy turn.", Text.RED), 30, 1);
        }
      }

      // Read user input
      input = userInput(in).toLowerCase().trim();

      // display event based on last turn's input
      if (partyTurn) {

        String[] splitInput = input.split(" ");

        /*
         * if (party.get(whichPlayer).status()) {
         * whichPlayer++;
         * continue;
         * }
         */

        try {
          // Process user input for the last Adventurer:
          if (input.startsWith("attack ") || input.startsWith("a ")) {
            if (splitInput.length < 2) { // no target
              Text.clear(30, 1, 80, 1);
              drawText("Must specify a target. Try again.", 30, 1);
			  Text.go(27, 2);
              Text.wait(1000);
            } else {
              String target = splitInput[1];
              // TextBox(10, 51, 20, 1, target); // not sure how important this is --sandra
              Adventurer ally = party.get(nextPlayer);
              Adventurer enemy = enemies.get(Integer.valueOf(target));

              if (!enemy.status()) { // checks if not alive
                COMMANDLIST += ally.getName() + "  tried to attack " + enemy.getName() + " but enemy is dead. "
                    + ally.getName() + " tries again." + "\n";
                drawScreen();
                continue;
              }

              COMMANDLIST += ally.attack(enemy) + "\n";
              whichPlayer++;
            }
          } else if (input.startsWith("special ") || input.startsWith("sp ")) {
            if (splitInput.length < 2) { // no target
              Text.clear(30, 1, 80, 1);
              drawText("Must specify a target. Try again.", 30, 1);
			  Text.go(27, 2);
              Text.wait(1000);
            } else {
              String target = splitInput[1];
              Adventurer ally = party.get(nextPlayer);
              Adventurer enemy = enemies.get(Integer.valueOf(target));

              if (!enemy.status()) { // checks if not alive
                COMMANDLIST += ally.getName() + "  tried to special attack " + enemy.getName() + " but enemy is dead. "
                    + ally.getName() + " tries again." + "\n";
                drawScreen();
                continue;
              }

              COMMANDLIST += ally.specialAttack(enemy) + "\n";
              whichPlayer++;
            }
          } else if (input.startsWith("su ") || input.startsWith("support ")) {
            // "support 0" or "su 0" or "su 2" etc.
            // assume the value that follows su is an integer.

            if (splitInput.length < 2) { // self support
              COMMANDLIST += party.get(nextPlayer).support() + "\n";
              whichPlayer++;
            } else {
              String target = splitInput[1];
              Adventurer current = party.get(nextPlayer);
              Adventurer suTarget = party.get(Integer.valueOf(target));

              if (!suTarget.status()) {
                COMMANDLIST += current.getName() + "  tried to support " + suTarget.getName() + " but ally is dead. "
                    + current.getName() + " tries again." + "\n";
                drawScreen();
                continue;
              }

              COMMANDLIST += current.support(suTarget) + "\n";
              whichPlayer++;
            }
          } else if (input.startsWith("quit") || input.startsWith("q")) {
            quit();
            return;
          } else {
            Text.clear(30, 1, 80, 1);
            drawText("Invalid input; try again.", 30, 1);
            Text.wait(1000);
          }
        } catch (NumberFormatException e) {
          Text.clear(30, 1, 80, 1);
          drawText("Enter an number.", 30, 1);
          Text.go(27, 2);
          Text.wait(1000);
        } catch (IndexOutOfBoundsException e) {
          Text.clear(30, 1, 80, 1);
          drawText("Invalid entry: number entered out of enemy party size. Try again.", 30, 1);
          Text.go(27, 2);
          Text.wait(1000);
        }

        /*
         * nextPlayer %= totalPlayers
         * if (nextPlayer < party.size()) {
         * // This is a player turn.
         * // Decide where to draw the following prompt:
         * if (party.get(nextPlayer).status()) {
         * String prompt = "Enter command for " + party.get(whichPlayer) +
         * ": (a)ttack/(sp)ecial/(q)uit";
         * drawText(prompt, 30, 1);
         * } else {
         * continue;
         * }
         * } else {
         * // This is after the player's turn, and allows the user to see the enemy turn
         * // Decide where to draw the following prompt:
         * String prompt = "press enter to see monster's turn";
         * drawText(prompt, 30, 1);
         * 
         * partyTurn = false;
         * whichOpponent = 0;
         * }
         * // done with one party member
         */
      } else {
        // not the party turn!

        // enemy attacks a randomly chosen person with a randomly chosen attack
        // Enemy action choices go here!

        int whichOpponent = nextPlayer - party.size();
        Adventurer enemy = enemies.get(whichOpponent);
        /*
         * if (enemy.getHP() <= 0) {
         * whichOpponent++;
         * continue;
         * }
         */

        double rN = Math.random();
        int picked = (int) (Math.random() * party.size());
        Adventurer ally = party.get(picked);

        if (!ally.status()) {
          COMMANDLIST += enemy.getName() + " attack on " + ally.getName() + " failed; target dead." + "\n";
          drawScreen();
          continue;
        } else {
          if (rN < .1) {
            int other = (int) (Math.random() * enemies.size());
            if (other == whichOpponent) {
              COMMANDLIST += enemy.support(enemy) + "\n";
            } else {
              if (enemies.get(other).status()) {
                COMMANDLIST += enemy.support(enemies.get(other)) + "\n";
              } else {
                COMMANDLIST += enemy.getName() + " support on " + enemies.get(other).getName()
                    + " failed; adventurer dead." + "\n";
              }
            }
          } else if (rN < .75) {
            COMMANDLIST += enemy.attack(ally) + "\n";
          } else {
            COMMANDLIST += enemy.specialAttack(ally) + "\n";
          }

          whichPlayer++;
        }
      } // end of one enemy.

      /*
       * // modify this if statement.
       * if (!partyTurn && nextPlayer == enemies.size() - 1)
       * 
       * {
       * // THIS BLOCK IS TO END THE ENEMY TURN
       * // It only triggers after the last enemy goes.
       * whichPlayer = 0;
       * turn++;
       * partyTurn = true;
       * // display this prompt before player's turn
       * String instr = "Enter command for " + party.get(whichPlayer) +
       * ": attack/special/quit";
       * drawText(instr, 30, 1);
       * }
       */

      // display the updated screen after input has been processed.
      drawScreen();

      boolean alliesDead = true;
      for (int i = 0; i < party.size(); i++) {
        if (party.get(i).status()) {
          alliesDead = false;
        }
      }
      if (alliesDead) {
        Text.wait(1000);
        quit();
        break;
      }

      boolean enemiesDead = true;
      for (int i = 0; i < enemies.size(); i++) {
        if (enemies.get(i).status()) {
          enemiesDead = false;
        }
      }
      if (enemiesDead) {
        Text.wait(1000);
        win();
        break;
      }
    }
    // end of main game loop

    // After quit reset things:
    return;
  }

}
