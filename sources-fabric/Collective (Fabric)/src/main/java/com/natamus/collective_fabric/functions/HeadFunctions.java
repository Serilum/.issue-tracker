/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.63.
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

package com.natamus.collective_fabric.functions;

import java.awt.*;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import com.natamus.collective_fabric.data.GlobalVariables;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HeadFunctions {
	public static ItemStack getPlayerHead(String playername, Integer amount) {
		// Head Data
		String data1 = DataFunctions.readStringFromURL(GlobalVariables.playerdataurl + playername.toLowerCase());
		if (data1.equals("")) {
			return null;
		}

		String[] sdata1 = data1.split("\":\"");
		String pname = sdata1[1].split("\"")[0];
		String pid = sdata1[2].split("\"")[0];
		
		String data2 = DataFunctions.readStringFromURL(GlobalVariables.skindataurl + pid);
		if (data2.equals("")) {
			return null;
		}
		
		String[] sdata2 = data2.replaceAll(" ", "").split("value\":\"");
		
		String tvalue = sdata2[1].split("\"")[0];
		String d = new String(Base64.getDecoder().decode((tvalue.getBytes())));
		
		String texture = Base64.getEncoder().encodeToString((("{\"textures\"" + d.split("\"textures\"")[1]).getBytes()));
		String oldid = new UUID(texture.hashCode(), texture.hashCode()).toString();

		return getTexturedHead(pname + "'s Head", texture, oldid, 1);
	}
	
	public static ItemStack getTexturedHead(String headname, String texture, String oldid, Integer amount) {
		ItemStack texturedhead = new ItemStack(Items.PLAYER_HEAD, amount);
		
		List<Integer> intarray = UUIDFunctions.oldIdToIntArray(oldid);
		
		CompoundTag skullOwner = new CompoundTag();
		skullOwner.putIntArray("Id", intarray);
		
		CompoundTag properties = new CompoundTag();
		ListTag textures = new ListTag();
		CompoundTag tex = new CompoundTag();
		tex.putString("Value", texture);
		textures.add(tex);

		properties.put("textures", textures);
		skullOwner.put("Properties", properties);
		texturedhead.addTagElement("SkullOwner", skullOwner);
		
		Component tcname = Component.literal(headname);
		texturedhead.setHoverName(tcname);		
		return texturedhead;
	}
	
	public static boolean hasStandardHead(String mobname) {
        return mobname.equals("creeper") || mobname.equals("zombie") || mobname.equals("skeleton");
    }
}
