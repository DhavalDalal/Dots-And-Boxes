Feature: Playing Dots and Boxes
  * game begins with empty grid of dots
  * players take turns at adding a horizontal or vertical line
    between two un-joined adjacent dots.
  * a player who completes the fourth side of a 1x1 box earns a point,
    writes their initials in the box and gets another turn.
  * game is over when there are no more lines that can be placed
  * winner is the player with most boxes
  * the board can be of any size 2x2, 5x5 works well for experts
  * a grid of 1x2 with boxes would look like:

 {0,0} {0,1} {0,2}
     +---+---+
     | 1 | 2 |
     +---+---+
 {1,0} {1,1} {1,2}


  * a 2x2 grid with boxes would look like:

  {0,0} {0,1} {0,2}
      +---+---+
      | 1 | 2 |
 {1,0}+---+---+{1,2}
      | 3 | 4 |
      +---+---+
  {2,0} {2,1} {2,2}


  Scenario: Player joins two dots on the grid
    Given a grid of size 1x2 with players "foo, bar"
      | box |   d1  |  d2   |lineMarked|
      |  1  | {0,0} | {0,1} |   false  |
      |  1  | {0,1} | {1,1} |   false  |
      |  1  | {1,0} | {0,0} |   false  |
      | 1-2 | {1,1} | {1,0} |   false  |
      |  2  | {1,1} | {1,2} |   false  |
      |  2  | {1,2} | {0,2} |   false  |
      |  2  | {0,2} | {0,1} |   false  |
    When a player "foo" joins dots {1,1} and {1,0}
    Then the grid looks like:
      | box |   d1  |  d2   |lineMarked|
      |  1  | {0,0} | {0,1} |   false  |
      |  1  | {0,1} | {1,1} |   false  |
      |  1  | {1,0} | {0,0} |   false  |
      | 1-2 | {1,1} | {1,0} |   true   |
      |  2  | {1,1} | {1,2} |   false  |
      |  2  | {1,2} | {0,2} |   false  |
      |  2  | {0,2} | {0,1} |   false  |

  Scenario: Player completes fourth side of 1x2 box
    Given a grid of size 1x2 with players "foo, bar"
      | box |   d1  |  d2   |lineMarked|who|
      |  1  | {0,0} | {0,1} |   true   |foo|
      |  1  | {0,1} | {1,1} |   true   |bar|
      |  1  | {1,0} | {0,0} |   true   |foo|
      | 1-2 | {1,1} | {1,0} |   false  |bar|
      |  2  | {1,1} | {1,2} |   false  |   |
      |  2  | {1,2} | {0,2} |   false  |   |
      |  2  | {0,2} | {0,1} |   false  |   |
    And the players have scores {"foo" = 0, "bar" = 0}
    When a player "bar" joins dots {1,1} and {1,0}
    Then the grid looks like:
      | box |   d1  |  d2   |lineMarked|who|
      |  1  | {0,0} | {0,1} |   true   |foo|
      |  1  | {0,1} | {1,1} |   true   |bar|
      |  1  | {1,0} | {0,0} |   true   |foo|
      | 1-2 | {1,1} | {1,0} |   true   |bar|
      |  2  | {1,1} | {1,2} |   false  |   |
      |  2  | {1,2} | {0,2} |   false  |   |
      |  2  | {0,2} | {0,1} |   false  |   |
    And the players have scores {"foo" = 0, "bar" = 1}
    And player "bar" gets another chance

  Scenario: Winner is the player with most boxes
    Given a grid of size 1x3 with players "foo, bar"
      | box |   d1  |  d2   |lineMarked|who|
      |  1  | {0,0} | {0,1} |   true   |foo|
      |  1  | {1,0} | {0,0} |   true   |bar|
      | 1-2 | {1,1} | {1,0} |   true   |foo|
      |  2  | {1,1} | {1,2} |   true   |bar|
      |  1  | {0,1} | {1,1} |   true   |foo|
      | 2-3 | {1,2} | {0,2} |   true   |foo|
      |  2  | {0,2} | {0,1} |   true   |bar|
      |  3  | {1,2} | {1,3} |   true   |bar|
      |  3  | {1,3} | {0,3} |   true   |foo|
      |  3  | {0,3} | {0,2} |   false  |   |

    And the players have scores {"foo" = 1, "bar" = 1}
    When a player "bar" joins dots {0,3} and {0,2}
    Then the grid looks like:
      | box |   d1  |  d2   |lineMarked|who|
      |  1  | {0,0} | {0,1} |   true   |foo|
      |  1  | {1,0} | {0,0} |   true   |bar|
      | 1-2 | {1,1} | {1,0} |   true   |foo|
      |  2  | {1,1} | {1,2} |   true   |bar|
      |  1  | {0,1} | {1,1} |   true   |foo|
      | 2-3 | {1,2} | {0,2} |   true   |foo|
      |  2  | {0,2} | {0,1} |   true   |bar|
      |  3  | {1,2} | {1,3} |   true   |bar|
      |  3  | {1,3} | {0,3} |   true   |foo|
      |  3  | {0,3} | {0,2} |   true   |bar|

    And the players have scores {"foo" = 1, "bar" = 2}
    And the game is over
    And the winner is "bar"