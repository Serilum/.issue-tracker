/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.1, mod version: 2.45.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import com.natamus.collective.data.GlobalVariables;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HeadFunctions {
	public static ItemStack getPlayerHead(String playername, Integer amount) {
		// Head Data
		String data1 = DataFunctions.readStringFromURL(GlobalVariables.playerdataurl + playername.toLowerCase());
		if (data1 == "") {
			return null;
		}

		String[] sdata1 = data1.split("\":\"");
		String pname = sdata1[1].split("\"")[0];
		String pid = sdata1[2].split("\"")[0];
		
		String data2 = DataFunctions.readStringFromURL(GlobalVariables.skindataurl + pid);
		if (data2 == "") {
			return null;
		}
		
		String[] sdata2 = data2.replaceAll(" ", "").split("value\":\"");
		
		String tvalue = sdata2[1].split("\"")[0];
		String d = new String(Base64.getDecoder().decode((tvalue.getBytes())));
		
		String texture = new String(Base64.getEncoder().encodeToString((("{\"textures\"" + d.split("\"textures\"")[1]).getBytes())));
		String oldid = new UUID(texture.hashCode(), texture.hashCode()).toString();
		
		ItemStack playerhead = getTexturedHead(pname + "'s Head", texture, oldid, 1);
		return playerhead;
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
		
		TextComponent tcname = new TextComponent(headname);
		texturedhead.setHoverName(tcname);		
		return texturedhead;
	}
	
	public static boolean hasStandardHead(String mobname) {
		if (mobname.equals("creeper") || mobname.equals("zombie") || mobname.equals("skeleton")) {
			return true;
		}
		return false;
	}
}
