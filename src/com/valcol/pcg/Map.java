package com.valcol.pcg;

import java.util.Random;

/*
 * This algorithm use cellular automata to generate a random cave from a seed. 
 */

public class Map {
	
	//The map is a 2D array containing cell's id (for exemple 1=Rock, 0=empty space..).
	private static int[][] map = new int[400][250];
	private static Random rnd;
	private static  int waterLevel;
	private static long seed;

	public Map(long seed, int waterLevel) {
		
		this.seed = seed;
		System.out.println(seed);
		this.waterLevel = waterLevel;
		map = createMap();
	}

	//Create the map
	public int[][] createMap() {
		
		rnd = new Random(seed);
		createMessyMap(map);
		for (int i = 0; i < 8; i++)
			map = generate(map);
		setMineral(map);
		setWater(map);
		setGrass(map);

		
		return map;
	}
	
	//Create a basic map by randomly setting each cell empty or filled with Rock
	public static int[][] createMessyMap(int[][] map) {

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				map[x][y] = 0;
				if (rnd.nextInt() > rnd.nextInt())
					map[x][y] = 1;
			}
		}

		return map;
	}

	//Cellular automata algorithm
	public static int countEmptyCases(int[][] map, int x, int y) {
		int count = 0;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				int _x = x + i;
				int _y = y + j;

				if (i == 0 && j == 0) {

				} else if (_x < 0 || _y < 0
						|| _x >= map.length
						|| _y >= map[0].length) {
					count = count + 1;
				} else if (map[_x][_y] == 1) {
					count = count + 1;
				}
			}
		}

		return count;
	}

	public static int[][] generate(int[][] map) {
		int[][] newMap = new int[map.length][map[0].length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				int count = countEmptyCases(map, x, y);
				if (map[x][y] == 1) {
					if (count < 10) {
						newMap[x][y] = 0;
					} else {
						newMap[x][y] = 1;
					}
				} else {
					if (count > 15) {
						newMap[x][y] = 1;
					} else {
						newMap[x][y] = 0;
					}
				}
			}
		}
		return newMap;
	}

	//Create grass on the top of filled cases
	public static void setGrass(int[][] map) {
		for (int x = 0; x < map.length; x++) {
			for (int y = 1; y < map[0].length - 1; y++) {
				if (map[x][y] == 1 && map[x][y - 1] == 0 && map[x][y + 1] == 1)
					map[x][y] = 2;
			}
		}
	}
	
	//Create a mineral cell, then expand it
	public static void setMineral(int[][] map) {
		for (int x = 0; x < map.length; x++) {
			for (int y = 1; y < map[0].length; y++) {
				int k = Math.abs(rnd.nextInt());
				if (k < Math.abs(rnd.nextInt() / 30))
					createMineral(map, x, y, 8);
			}
		}
	}

	
	private static void createMineral(int[][] map, int x, int y, int p) {
		int k = Math.abs(rnd.nextInt() * p);
		if (map[x][y] == 1 && k <= p * Math.abs(rnd.nextInt())) {
			map[x][y] = 3;
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (x + i < map.length && y + j < map[0].length
							&& x + i > 0 && y + j > 0)
						createMineral(map, x + i, y + j, p - 1);
				}
			}
		}

	}
	
	//Create a water source
	public static void setWater(int[][] map) {
		for (int x = 0; x < map.length; x++) {
			for (int y = 1; y < map[0].length - 1; y++) {
				int k = Math.abs(rnd.nextInt());
				if (k < Math.abs(rnd.nextInt() / 100) && map[x][y + 1] == 1 && map[x][y] == 0)
					createWater(map, x, y);
			}
		}
	}

	/*Expand the water by checking if the left case or the right case is empty. If the case under the 
	*current case is empty, create a waterfall.
	**/
	public static boolean waterLevel(int[][] map, int x, int y, boolean b) {
		if (x + 1 < map.length && x - 1 > 2 && y + 1 < map[0].length
				&& y - 1 > 2) {
			if (b) {
				if (map[x][y] <= 0 && (map[x][y + 1] != 0 && map[x][y + 1] != -2 )) {
					map[x][y] = -1;
					return waterLevel(map, x - 1, y, b);
				} else if (map[x][y] > 0) {
					return true;
				} else if (map[x][y] <= 0 && map[x][y + 1] == 0) {
					createWater(map, x, y);
					return false;
				}
			} else {
				if (map[x][y] <= 0 && (map[x][y + 1] != 0 && map[x][y + 1] != -2)) {
					map[x][y] = -1;
					return waterLevel(map, x + 1, y, b);
				} else if (map[x][y] > 0) {
					return true;
				} else if (map[x][y] <= 0 && map[x][y + 1] == 0) {
					createWater(map, x, y);
					return false;
				}
			}

		}
		return false;

	}

	//Create a waterfall. If the case under the current case is empty, expand the water.
	private static void createWater(int[][] map, int x, int y) {
		
		if (x + 1 < map.length && x - 1 > 2 && y + 1 < map[0].length
				&& y - 1 > 2) {
			if (map[x][y + 1] == 0 || map[x][y + 1] == -2) {
				map[x][y] = -2;
				createWater(map, x, y + 1);
			} else if (map[x][y + 1] < 0) {
				map[x][y] = -1;
			} else {
				map[x][y] = -1;
				int t = 0;
				boolean b1 ;
				boolean b2 ;
				do {
					b1 = waterLevel(map, x, y, false);
					b2 = waterLevel(map, x, y, true);
					t++;
					y--;
				}while (b1 && b2 && t < waterLevel);
			}

		}
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public int getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(int waterLevel) {
		this.waterLevel = waterLevel;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}
	

}
