/*
 * This is the latest source code of Nether Portal Spread.
 * Minecraft version: 1.19.2, mod version: 6.5.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.netherportalspread.util;

import java.util.ArrayList;
import java.util.List;

public class DefaultConfigs {
	public static List<String> getDefaultConfigForVersion(String version) {
		List<String> config = new ArrayList<String>();
		
		config.add("stone; [netherrack>10 + magma_block>2 + nether_quartz_ore>1],");
		config.add("grass; [nether_sprouts>1],");
		config.add("cobblestone; [blackstone>1],");
		config.add("grass_block; [crimson_nylium>1],");
		config.add("dirt; [netherrack>1],");
		config.add("coarse_dirt; [soul_soil>1],");
		config.add("sand; [soul_sand>1 + soul_soil>1],");
		config.add("red_sand; [soul_soil>1 + soul_sand>1],");
		config.add("sandstone; [blackstone>1],");
		config.add("cut_sandstone; [polished_blackstone>1],");
		config.add("chiseled_sandstone; [chiseled_polished_blackstone>1],");
		config.add("smooth_sandstone; [basalt>1],");
		config.add("smooth_red_sandstone; [basalt>1],");
		config.add("gravel; [soul_soil>1],");
		config.add("stone_bricks; [nether_bricks>2 + red_nether_bricks>1],");
		config.add("sea_lantern; [glowstone>1],");
		config.add("farmland; [soul_sand>1],");
		config.add("wheat; [nether_wart>1],");
		config.add("gold_ore; [nether_gold_ore>1],");
		config.add("fern; [nether_sprouts>1],");
		config.add("oak_leaves; [nether_wart_block>1],");
		config.add("spruce_leaves; [nether_wart_block>1],");
		config.add("birch_leaves; [warped_wart_block>1],");
		config.add("jungle_leaves; [warped_wart_block>1],");
		config.add("acacia_leaves; [nether_wart_block>1],");
		config.add("dark_oak_leaves; [warped_wart_block>1],");
		config.add("oak_log; [crimson_stem>1],");
		config.add("spruce_log; [crimson_stem>1],");
		config.add("birch_log; [warped_stem>1],");
		config.add("jungle_log; [warped_stem>1],");
		config.add("acacia_log; [crimson_stem>1],");
		config.add("dark_oak_log; [warped_stem>1],");		
		
		return config;
	}
}
